package com.pinyougou.seller.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.SpecificationEntity;
import com.pinyougou.mapper.TbSpecificationDao;
import com.pinyougou.mapper.TbSpecificationOptionDao;
import com.pinyougou.pojo.*;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.seller.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private TbSpecificationDao specificationMapper;
    @Autowired
    private TbSpecificationOptionDao specificationOptionDao;

    /**
     * 查询全部
     */
    @Override
    public List<TbSpecification> findAll() {
        return specificationMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult<TbSpecification> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(SpecificationEntity specificationEntity) {
        //插入规格
        TbSpecification specification = specificationEntity.getSpecification();
        //插入规格项
        specificationMapper.insert(specification);
        List<TbSpecificationOption> specificationOptionList = specificationEntity.getSpecificationOptionList();
        if (!specificationOptionList.isEmpty()) {
            specificationOptionList.forEach(option->option.setSpecId(specification.getId()));
            specificationOptionDao.insertList(specificationOptionList);
        }

    }


    /**
     * 修改
     */
    @Override
    public void update(SpecificationEntity specificationEntity) {
        //修改规格
        TbSpecification specification = specificationEntity.getSpecification();
        specificationMapper.updateByPrimaryKeySelective(specification);
        //先删除规格项再插入
        List<TbSpecificationOption> specificationOptionList = specificationEntity.getSpecificationOptionList();
        specificationOptionList.forEach(option->option.setSpecId(specification.getId()));
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(specification.getId());
        specificationOptionDao.deleteByExample(example);
        if (!specificationOptionList.isEmpty()) {
            specificationOptionDao.insertList(specificationOptionList);
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public SpecificationEntity findOne(Long id) {
        SpecificationEntity entity = new SpecificationEntity();
        entity.setSpecification(specificationMapper.selectByPrimaryKey(id));
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(id);
        List<TbSpecificationOption> specificationOptions = specificationOptionDao.selectByExample(example);
        entity.setSpecificationOptionList(specificationOptions);
        return entity;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        TbSpecificationExample specExample = new TbSpecificationExample();
        Criteria specCriteria = specExample.createCriteria();
        TbSpecificationOptionExample specOptionExample = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria optionCriteria = specOptionExample.createCriteria();
        if (ids != null && ids.length > 0) {
            specCriteria.andIdIn(Arrays.asList(ids));
            specificationMapper.deleteByExample(specExample);
            optionCriteria.andSpecIdIn(Arrays.asList(ids));
            specificationOptionDao.deleteByExample(specOptionExample);
        }
    }


    @Override
    public PageResult<TbSpecification> findPage(TbSpecification specification, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbSpecificationExample example = new TbSpecificationExample();
        Criteria criteria = example.createCriteria();

        if (Objects.nonNull(specification)) {
            if (!Strings.isNullOrEmpty(specification.getSpecName())) {
                criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
            }
        }
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

}
