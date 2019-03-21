package com.pinyougou.seller.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.common.utils.ValidateUtil;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.seller.service.ItemCatService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public ResponseResult<List<TbItemCat>> findAll() {
        try {
            List<TbItemCat> itemCatList = itemCatService.findAll();
            return ResponseResult.ok(itemCatList);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            return ResponseResult.error("查询失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public ResponseResult<TbItemCat> findOne(Long id) {
        try {
            TbItemCat itemCat = itemCatService.findOne(id);
            return ResponseResult.ok(itemCat);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("查询失败");
        }
    }


    @RequestMapping("/findByParentId")
    public ResponseResult<List<TbItemCat>> findByParentId(Long parentId) {
        try {
            List<TbItemCat> itemCatList = itemCatService.findByParentId(parentId);
            return ResponseResult.ok(itemCatList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("查询失败");
        }
    }
}
