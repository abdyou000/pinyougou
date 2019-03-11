package com.pinyougou.seller.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.TbBrandDao;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.seller.auth.BrandService;
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
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandDao brandMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult<TbBrand> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbBrand brand) {
        brandMapper.insert(brand);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbBrand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        TbBrandExample example = new TbBrandExample();
        Criteria criteria = example.createCriteria();
        if (ids != null && ids.length > 0) {
            criteria.andIdIn(Arrays.asList(ids));
            brandMapper.deleteByExample(example);
        }
    }


    @Override
    public PageResult<TbBrand> findPage(TbBrand brand, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbBrandExample example = new TbBrandExample();
        Criteria criteria = example.createCriteria();

        if (Objects.nonNull(brand)) {
            if (!Strings.isNullOrEmpty(brand.getName())) {
                criteria.andNameLike("%" + brand.getName() + "%");
            }
            if (!Strings.isNullOrEmpty(brand.getFirstChar())) {
                criteria.andFirstCharLike("%" + brand.getFirstChar() + "%");
            }

        }
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

}
