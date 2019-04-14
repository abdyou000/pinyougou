package com.pinyougou.pay.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.pinyougou.common.pojo.ResponseResult;
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
    public Map<String, String> createNative(String outTradeNo, String totalFee) {
        Map<String, String> reqMap = ImmutableMap.<String, String>builder()
                .put("body", "品优购")
                .put("out_trade_no", outTradeNo)
                .put("device_info", "")
                .put("fee_type", "CNY")
                .put("total_fee", totalFee)
                .put("spbill_create_ip", "127.0.0.1")
                .put("trade_type", "NATIVE")
                .put("product_id", "12")
                .build();
        Map<String, String> respMap = null;
        try {
            respMap = wxPay.unifiedOrder(reqMap);
            HashMap<String, String> resultMap = Maps.newHashMap();
            resultMap.put("code_url", respMap.get("code_url"));
            resultMap.put("out_trade_no", outTradeNo);
            resultMap.put("total_fee", totalFee);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Maps.newHashMap();
    }

    @Override
    public Boolean queryPayStatus(String outTradeNo) {
        Map<String, String> data = ImmutableMap.<String, String>builder()
                .put("out_trade_no", outTradeNo)
                .build();
        try {
            Map<String, String> respMap = wxPay.orderQuery(data);
            System.out.println(respMap);
            return Objects.equals("SUCCESS",respMap.get("trade_state"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, String> closePay(String out_trade_no) {
        return null;
    }

    @Override
    public boolean isPayResultNotifySignatureValid(Map<String, String> reqMap) {
        try {
            return wxPay.isPayResultNotifySignatureValid(reqMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
