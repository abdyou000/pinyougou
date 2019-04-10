package com.pinyougou.cart.controller;
import java.util.List;

import com.pinyougou.common.pojo.Cart;
import com.pinyougou.common.pojo.OrderVO;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.order.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbOrder;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/order")
public class OrderController {

	@Reference
	private OrderService orderService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbOrder> findAll(){			
		return orderService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){
		return orderService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param orderVO
	 * @return
	 */
	@RequestMapping("/add")
	public ResponseResult add(@RequestBody OrderVO orderVO){
		
		//获取当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
        orderVO.getOrder().setUserId(username);
        orderVO.getOrder().setSourceType("2");//订单来源  PC
		
		try {
			orderService.add(orderVO);
			return ResponseResult.ok("增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("增加失败");
		}
	}
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbOrder findOne(Long id){
		return orderService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public ResponseResult delete(Long [] ids){
		try {
			orderService.delete(ids);
			return ResponseResult.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error( "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param order
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbOrder order, int page, int rows  ){
		return orderService.findPage(order, page, rows);		
	}
	
}
