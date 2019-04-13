package com.pinyougou.order.service;

import com.pinyougou.common.pojo.OrderVO;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbPayLog;

import java.util.List;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface PayLogService {
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	TbPayLog findOne(String id);
}
