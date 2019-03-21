package com.pinyougou.seller.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.TbItemCatDao;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemCatExample;
import com.pinyougou.pojo.TbItemCatExample.Criteria;
import com.pinyougou.seller.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatDao itemCatMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbItemCat> findAll() {
        return itemCatMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbItemCat itemCat) {
        itemCatMapper.insert(itemCat);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbItemCat itemCat) {
        itemCatMapper.updateByPrimaryKeySelective(itemCat);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne(Long id) {
        return itemCatMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdIn(Arrays.asList(ids));
        List<TbItemCat> itemCatList = itemCatMapper.selectByExample(example);
        if (itemCatList.size() > 0) {
            throw new IllegalArgumentException("包含子类目，不能删除");
        }
        if (ids != null && ids.length > 0) {
            criteria.andIdIn(Arrays.asList(ids));
            itemCatMapper.deleteByExample(example);
        }
    }


    @Override
    public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();

        if (itemCat != null) {
            if (itemCat.getName() != null && itemCat.getName().length() > 0) {
                criteria.andNameLike("%" + itemCat.getName() + "%");
            }

        }

        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<TbItemCat> findByParentId(Long parentId) {

        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> itemCatList = itemCatMapper.selectByExample(example);
        return itemCatList;
    }

}
