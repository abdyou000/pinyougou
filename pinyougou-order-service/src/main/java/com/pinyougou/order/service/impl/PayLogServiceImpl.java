package com.pinyougou.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.common.pojo.*;
import com.pinyougou.common.utils.IdWorker;
import com.pinyougou.mapper.TbOrderDao;
import com.pinyougou.mapper.TbOrderItemDao;
import com.pinyougou.mapper.TbPayLogDao;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.order.service.PayLogService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderExample;
import com.pinyougou.pojo.TbOrderExample.Criteria;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbPayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class PayLogServiceImpl implements PayLogService {

    @Autowired
    private TbPayLogDao payLogMapper;
    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbPayLog findOne(String id) {
        return payLogMapper.selectByPrimaryKey(id);
    }

}
