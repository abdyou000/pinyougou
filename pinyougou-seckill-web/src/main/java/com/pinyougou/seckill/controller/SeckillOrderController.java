package com.pinyougou.seckill.controller;
import java.util.List;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.ResponseResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {

	@Reference
	private SeckillOrderService seckillOrderService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeckillOrder> findAll(){			
		return seckillOrderService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){
		return seckillOrderService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param seckillOrder
	 * @return
	 */
	@RequestMapping("/add")
	public ResponseResult add(@RequestBody TbSeckillOrder seckillOrder){
		try {
			seckillOrderService.add(seckillOrder);
			return ResponseResult.ok("增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param seckillOrder
	 * @return
	 */
	@RequestMapping("/update")
	public ResponseResult update(@RequestBody TbSeckillOrder seckillOrder){
		try {
			seckillOrderService.update(seckillOrder);
			return ResponseResult.ok("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbSeckillOrder findOne(Long id){
		return seckillOrderService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public ResponseResult delete(Long [] ids){
		try {
			seckillOrderService.delete(ids);
			return ResponseResult.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param seckillOrder
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSeckillOrder seckillOrder, int page, int rows  ){
		return seckillOrderService.findPage(seckillOrder, page, rows);		
	}
	
	@RequestMapping("/submitOrder")
	public ResponseResult submitOrder(Long seckillId){
		//提取当前用户
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if("anonymousUser".equals(username)){
			return ResponseResult.error("当前用户未登录");
		}
				
		try {
			seckillOrderService.submitOrder(seckillId, username);
			return ResponseResult.ok("提交订单成功");
			
		}catch (RuntimeException e) {
			e.printStackTrace();
			return ResponseResult.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("提交订单失败");
		}
		
	}
	
}
