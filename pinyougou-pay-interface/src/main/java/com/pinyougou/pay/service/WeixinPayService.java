package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {

	/**
	 * 生成二维码
	 * @param outTradeNo
	 * @param totalFee
	 * @return
	 */
	public Map createNative(String outTradeNo, String totalFee);
	
	/**
	 * 查询支付订单状态
	 * @param outTradeNo
	 * @return
	 */
	public Map queryPayStatus(String outTradeNo);
	
	
	/**
	 * 关闭订单
	 * @param outTradeNo
	 * @return
	 */
	public Map closePay(String outTradeNo);
	
}
