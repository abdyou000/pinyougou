package com.pinyougou.seller.service;

import com.pinyougou.common.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface BrandService {

    /**
     * 返回全部列表
     *
     * @return
     */
    List<TbBrand> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    PageResult<TbBrand> findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    void add(TbBrand brand);


    /**
     * 修改
     */
    void update(TbBrand brand);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    TbBrand findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageResult findPage(TbBrand brand, int pageNum, int pageSize);

}
