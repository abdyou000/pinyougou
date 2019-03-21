package com.pinyougou.seller.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.SpecificationEntity;
import com.pinyougou.pojo.TbSpecification;

import java.util.List;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface SpecificationService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbSpecification> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult<TbSpecification> findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(SpecificationEntity specificationEntity);


    /**
     * 修改
     */
    public void update(SpecificationEntity specificationEntity);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public SpecificationEntity findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult<TbSpecification> findPage(TbSpecification specification, int pageNum, int pageSize);

}