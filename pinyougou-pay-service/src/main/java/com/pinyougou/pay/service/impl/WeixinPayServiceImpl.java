package com.pinyougou.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeixinPayService;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    private WXPay wxPay;

    @Override
    public Map createNative(String out_trade_no, String total_fee) {

        return null;
    }

    @Override
    public Map queryPayStatus(String out_trade_no) {
        return null;
    }

    @Override
    public Map closePay(String out_trade_no) {
        return null;
    }

}
