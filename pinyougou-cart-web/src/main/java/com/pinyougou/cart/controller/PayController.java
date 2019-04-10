package com.pinyougou.cart.controller;

import java.util.HashMap;
import java.util.Map;

import com.pinyougou.common.pojo.ResponseResult;
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

    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private OrderService orderService;

    @RequestMapping("/createNative")
    public Map createNative() {
        //1.获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //2.提取支付日志（从缓存 ）
        TbPayLog payLog = orderService.searchPayLogFromRedis(username);
        //3.调用微信支付接口
        if (payLog != null) {
            return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
        } else {
            return new HashMap<>();
        }
    }


    @RequestMapping("/queryPayStatus")
    public ResponseResult queryPayStatus(String out_trade_no) {
        ResponseResult result = null;
        int x = 0;
        while (true) {

            Map<String, String> map = weixinPayService.queryPayStatus(out_trade_no);//调用查询
            if (map == null) {
                result = ResponseResult.error("支付发生错误");
                break;
            }
            if (map.get("trade_state").equals("SUCCESS")) {//支付成功
                result = ResponseResult.ok("支付成功");
                orderService.updateOrderStatus(out_trade_no, map.get("transaction_id"));//修改订单状态
                break;
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            x++;
            if (x >= 100) {
                result = ResponseResult.error("二维码超时");
                break;
            }

        }
        return result;
    }

    @RequestMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) {

    }
}
