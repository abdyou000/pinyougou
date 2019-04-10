package com.pinyougou.mapper;

import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbOrderItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbOrderItemDao {
    long countByExample(TbOrderItemExample example);

    int deleteByExample(TbOrderItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbOrderItem record);
    int insertList(List<TbOrderItem> orderItemList);

    int insertSelective(TbOrderItem record);

    List<TbOrderItem> selectByExample(TbOrderItemExample example);

    TbOrderItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbOrderItem record, @Param("example") TbOrderItemExample example);

    int updateByExample(@Param("record") TbOrderItem record, @Param("example") TbOrderItemExample example);

    int updateByPrimaryKeySelective(TbOrderItem record);

    int updateByPrimaryKey(TbOrderItem record);
}