package com.pinyougou.cart.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.github.wxpay.sdk.WXPayXmlUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.order.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private WXPay wxPay;
    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private OrderService orderService;

    @Reference
    private PayLogService payLogService;

    @RequestMapping("/createNative")
    public ResponseResult<Map<String, String>> createNative() {
        //1.获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //2.提取支付日志（从缓存 ）
        TbPayLog payLog = orderService.searchPayLogFromRedis(username);
        //3.调用微信支付接口
        if (payLog != null) {
            Map<String, String> respMap = weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee().toString());
            return ResponseResult.ok(respMap);
        } else {
            return ResponseResult.ok(Maps.newHashMap());
        }
    }


    @RequestMapping("/queryPayStatus")
    public ResponseResult queryPayStatus(String outTradeNo) {
        try {
            //调用查询
            boolean success = weixinPayService.queryPayStatus(outTradeNo);
            return success?ResponseResult.ok("支付成功"):ResponseResult.error("支付失败");
        } catch (Exception e) {
           e.printStackTrace();
            return ResponseResult.error("支付失败");
        }


    }

    @RequestMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) {

        try {
            byte[] bytes = ByteStreams.toByteArray(request.getInputStream());
            String content = new String(bytes);
            Map<String, String> respMap = WXPayUtil.xmlToMap(content);
            if (wxPay.isPayResultNotifySignatureValid(respMap)) {
                if (Objects.equals("SUCCESS", respMap.get("return_code"))) {
                    //更新订单状态
                    String outTradeNo = respMap.get("out_trade_no");
                    TbPayLog payLog = payLogService.findOne(outTradeNo);
                    if (Objects.equals("1", payLog.getTradeState())) {
                        //表示已支付，通知已经处理过
                        String respXml = WXPayUtil.mapToXml(ImmutableMap.of(
                                "return_code","SUCCESS",
                                "return_msg","OK"
                        ));
                        response.getWriter().println(respXml);
                    } else {
                        orderService.updateOrderStatus(outTradeNo,respMap.get("transaction_id"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
