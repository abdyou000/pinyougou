package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {

	/**
	 * 生成二维码
	 * @param outTradeNo
	 * @param totalFee
	 * @return
	 */
	Map<String,String> createNative(String outTradeNo, String totalFee);
	
	/**
	 * 查询支付订单状态
	 * @param outTradeNo
	 * @return
	 */
	Boolean queryPayStatus(String outTradeNo);
	
	
	/**
	 * 关闭订单
	 * @param outTradeNo
	 * @return
	 */
	Map<String,String> closePay(String outTradeNo);
	
}
