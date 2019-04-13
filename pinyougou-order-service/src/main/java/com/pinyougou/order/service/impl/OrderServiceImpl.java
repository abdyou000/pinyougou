package com.pinyougou.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.common.pojo.*;
import com.pinyougou.common.utils.IdWorker;
import com.pinyougou.mapper.TbOrderDao;
import com.pinyougou.mapper.TbOrderItemDao;
import com.pinyougou.mapper.TbPayLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderExample;
import com.pinyougou.pojo.TbOrderExample.Criteria;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.order.service.OrderService;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderDao orderMapper;

    @Autowired
    private TbPayLogDao payLogMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbOrder> findAll() {
        return orderMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbOrder> page = (Page<TbOrder>) orderMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbOrderItemDao orderItemMapper;

    @Reference
    private CartService cartService;

    /**
     * 增加
     */
    @Override
    public void add(OrderVO orderVO) {

        //1.从redis中提取购物车列表
        TbOrder order = orderVO.getOrder();
        Cart criteriaCart = orderVO.getCriteriaCart();
        Cart cart = cartService.findSelectedCart(order.getUserId(), criteriaCart);

        List<String> orderIdList = Lists.newArrayList();//订单ID集合
        List<TbOrder> orderList = Lists.newArrayList();//订单ID集合
        BigDecimal totalMoney = BigDecimal.ZERO;//总金额
        List<TbOrderItem> orderItemList = Lists.newArrayList();
        for (CartItem cartItem : cart.getCartItemList()) {
            TbOrder tbOrder = createOrder(order);
            //循环购物车中每条明细记录
            BigDecimal paymeny = BigDecimal.ZERO;
            for (CartProduct product : cartItem.getProductList()) {
                TbOrderItem orderItem = createOrderItem(product, tbOrder.getOrderId());
                paymeny = paymeny.add(orderItem.getTotalFee());
                orderItemList.add(orderItem);
            }
            BigDecimal totalPrice = orderItemList.stream()
                    .map(TbOrderItem::getTotalFee)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            tbOrder.setPayment(totalPrice);//合计
            orderList.add(tbOrder);
            orderIdList.add(tbOrder.getOrderId().toString());
            totalMoney = totalMoney.add(paymeny);
        }
        orderItemMapper.insertList(orderItemList);
        orderMapper.insertList(orderList);
        //添加支付日志
        if ("1".equals(order.getPaymentType())) {
            TbPayLog payLog = createPayLog(order,
                    Joiner.on(',').join(orderIdList).toString(),
                    totalMoney.multiply(BigDecimal.TEN).longValue()
            );
            payLogMapper.insert(payLog);
            redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);//放入缓存
        }
        //3.清除redis中的购物车
        redisTemplate.boundHashOps("cart").delete(order.getUserId());
    }

    private TbPayLog createPayLog(TbOrder order, String orderIdList, Long totalMoney) {
        TbPayLog payLog = new TbPayLog();
        payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));//支付订单号
        payLog.setCreateTime(new Date());
        payLog.setUserId(order.getUserId());//用户ID
        payLog.setOrderList(orderIdList.toString().replace("[", "").replace("]", ""));//订单ID串
        payLog.setTotalFee(totalMoney);//金额（分）
        payLog.setTradeState("0");//交易状态
        payLog.setPayType("1");//微信
        return payLog;
    }

    private TbOrderItem createOrderItem(CartProduct product, Long orderId) {
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setId(idWorker.nextId());//主键
        orderItem.setOrderId(orderId);//订单编号
        orderItem.setSellerId(product.getSellerId());//商家ID
        orderItem.setGoodsId(product.getGoodsId());
        orderItem.setItemId(product.getItemId());
        orderItem.setNum(product.getNum());
        orderItem.setPicPath(product.getPicPath());
        orderItem.setPrice(product.getPrice());
        orderItem.setTotalFee(product.getTotalFee());
        orderItem.setTitle(product.getTitle());
        return orderItem;
    }

    private TbOrder createOrder(TbOrder order) {
        TbOrder tbOrder = new TbOrder();
        tbOrder.setOrderId(idWorker.nextId());
        tbOrder.setPaymentType(order.getPaymentType());//支付类型
        tbOrder.setStatus("1");//未付款
        tbOrder.setCreateTime(new Date());//下单时间
        tbOrder.setUpdateTime(new Date());//更新时间
        tbOrder.setUserId(order.getUserId());//当前用户
        tbOrder.setReceiverAreaName(order.getReceiverAreaName());//收货人地址
        tbOrder.setReceiverMobile(order.getReceiverMobile());//收货人电话
        tbOrder.setReceiver(order.getReceiver());//收货人
        tbOrder.setSourceType(order.getSourceType());//订单来源
        tbOrder.setSellerId(order.getSellerId());//商家ID
        return tbOrder;
    }

    /**
     * 修改
     */
    @Override
    public void update(TbOrder order) {
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbOrder findOne(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            orderMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbOrderExample example = new TbOrderExample();
        Criteria criteria = example.createCriteria();

        if (order != null) {
            if (order.getPaymentType() != null && order.getPaymentType().length() > 0) {
                criteria.andPaymentTypeLike("%" + order.getPaymentType() + "%");
            }
            if (order.getPostFee() != null && order.getPostFee().length() > 0) {
                criteria.andPostFeeLike("%" + order.getPostFee() + "%");
            }
            if (order.getStatus() != null && order.getStatus().length() > 0) {
                criteria.andStatusLike("%" + order.getStatus() + "%");
            }
            if (order.getShippingName() != null && order.getShippingName().length() > 0) {
                criteria.andShippingNameLike("%" + order.getShippingName() + "%");
            }
            if (order.getShippingCode() != null && order.getShippingCode().length() > 0) {
                criteria.andShippingCodeLike("%" + order.getShippingCode() + "%");
            }
            if (order.getUserId() != null && order.getUserId().length() > 0) {
                criteria.andUserIdLike("%" + order.getUserId() + "%");
            }
            if (order.getBuyerMessage() != null && order.getBuyerMessage().length() > 0) {
                criteria.andBuyerMessageLike("%" + order.getBuyerMessage() + "%");
            }
            if (order.getBuyerNick() != null && order.getBuyerNick().length() > 0) {
                criteria.andBuyerNickLike("%" + order.getBuyerNick() + "%");
            }
            if (order.getBuyerRate() != null && order.getBuyerRate().length() > 0) {
                criteria.andBuyerRateLike("%" + order.getBuyerRate() + "%");
            }
            if (order.getReceiverAreaName() != null && order.getReceiverAreaName().length() > 0) {
                criteria.andReceiverAreaNameLike("%" + order.getReceiverAreaName() + "%");
            }
            if (order.getReceiverMobile() != null && order.getReceiverMobile().length() > 0) {
                criteria.andReceiverMobileLike("%" + order.getReceiverMobile() + "%");
            }
            if (order.getReceiverZipCode() != null && order.getReceiverZipCode().length() > 0) {
                criteria.andReceiverZipCodeLike("%" + order.getReceiverZipCode() + "%");
            }
            if (order.getReceiver() != null && order.getReceiver().length() > 0) {
                criteria.andReceiverLike("%" + order.getReceiver() + "%");
            }
            if (order.getInvoiceType() != null && order.getInvoiceType().length() > 0) {
                criteria.andInvoiceTypeLike("%" + order.getInvoiceType() + "%");
            }
            if (order.getSourceType() != null && order.getSourceType().length() > 0) {
                criteria.andSourceTypeLike("%" + order.getSourceType() + "%");
            }
            if (order.getSellerId() != null && order.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + order.getSellerId() + "%");
            }

        }

        Page<TbOrder> page = (Page<TbOrder>) orderMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public TbPayLog searchPayLogFromRedis(String userId) {
        return (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
    }

    @Override
    public void updateOrderStatus(String outTradeNo, String transactionId) {
        //1.修改支付日志的状态及相关字段
        TbPayLog payLog = payLogMapper.selectByPrimaryKey(outTradeNo);
        payLog.setPayTime(new Date());//支付时间
        payLog.setTradeState("1");//交易成功
        payLog.setTransactionId(transactionId);//微信的交易流水号
        payLogMapper.updateByPrimaryKey(payLog);//修改
        //2.修改订单表的状态
        String orderList = payLog.getOrderList();// 订单ID 串

        List<String> orderIdList = Splitter.on(orderList).omitEmptyStrings().splitToList(",");
        orderIdList.forEach(orderId->{
            TbOrder order = orderMapper.selectByPrimaryKey(Long.valueOf(orderId));
            order.setStatus("2");//已付款状态
            order.setPaymentTime(new Date());//支付时间
            orderMapper.updateByPrimaryKey(order);
        });
        //3.清除缓存中的payLog
        redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());

    }

}
