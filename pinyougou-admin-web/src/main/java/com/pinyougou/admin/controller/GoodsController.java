package com.pinyougou.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.GoodsEntity;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.common.utils.ValidateUtil;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.search.service.ItemSearchService;
import com.pinyougou.seller.service.GoodsService;
import com.pinyougou.seller.service.ItemCatService;
import com.pinyougou.seller.service.ItemService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public ResponseResult<GoodsEntity> findOne(Long id) {
        try {
            GoodsEntity goodsEntity = goodsService.findOne(id);
            return ResponseResult.ok(goodsEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("查询失败");
        }
    }
    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public ResponseResult<String> delete(Long[] ids) {
        try {
            goodsService.delete(ids);

            return ResponseResult.ok("删除成功");
        } catch (IllegalArgumentException e) {
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param goods
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public ResponseResult<PageResult<TbGoods>> search(@RequestBody TbGoods goods, int page, int rows) {
        try {
            PageResult<TbGoods> pageResult = goodsService.findPage(goods, page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页搜索失败");
        }
    }

    @RequestMapping("/updateStatus")
    public ResponseResult<String> updateStatus(Long[] ids,String status) {
        try {
            goodsService.updateStatus(ids,status);

            return ResponseResult.ok("审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("审核出错");
        }
    }
}
