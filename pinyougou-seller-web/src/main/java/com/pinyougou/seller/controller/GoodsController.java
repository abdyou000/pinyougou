package com.pinyougou.seller.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.GoodsEntity;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.common.utils.ValidateUtil;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.seller.service.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public ResponseResult<List<TbGoods>> findAll() {
        try {
            List<TbGoods> goodsList = goodsService.findAll();
            return ResponseResult.ok(goodsList);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            return ResponseResult.error("查询失败");
        }
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public ResponseResult<PageResult<TbGoods>> findPage(int page, int rows) {
        try {
            PageResult<TbGoods> pageResult = goodsService.findPage(page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页查询失败");
        }
    }

    /**
     * 增加
     *
     * @param goodsEntity
     * @return
     */
    @RequestMapping("/add")
    public ResponseResult<String> add(@RequestBody @Validated GoodsEntity goodsEntity, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(ValidateUtil.parseError(result));
        }
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goodsEntity.getGoods().setSellerId(sellerId);
        try {
            goodsService.add(goodsEntity);
            return ResponseResult.ok("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goodsEntity
     * @return
     */
    @RequestMapping("/update")
    public ResponseResult<String> update(@RequestBody @Validated GoodsEntity goodsEntity, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(ValidateUtil.parseError(result));
        }
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        GoodsEntity goodsEntity1 = goodsService.findOne(goodsEntity.getGoods().getId());
        if (!Objects.equals(sellerId,goodsEntity1.getGoods().getSellerId()) ||
            !Objects.equals(sellerId,goodsEntity.getGoods().getSellerId())) {
            return ResponseResult.error("非法操作");
        }
        try {
            goodsService.update(goodsEntity);
            return ResponseResult.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("修改失败");
        }
    }

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
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            goods.setSellerId(sellerId);
            PageResult<TbGoods> pageResult = goodsService.findPage(goods, page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页搜索失败");
        }
    }
}
