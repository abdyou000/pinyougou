package com.pinyougou.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.common.utils.ValidateUtil;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.seller.service.BrandService;
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
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public ResponseResult<PageResult<TbItemCat>> findPage(int page, int rows) {
        try {
            PageResult<TbItemCat> pageResult = itemCatService.findPage(page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页查询失败");
        }
    }

    /**
     * 增加
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/add")
    public ResponseResult<String> add(@RequestBody @Validated TbItemCat itemCat, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(ValidateUtil.parseError(result));
        }
        try {
            itemCatService.add(itemCat);
            return ResponseResult.ok("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("增加失败");
        }
    }

    /**
     * 修改
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/update")
    public ResponseResult<String> update(@RequestBody @Validated TbItemCat itemCat, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(ValidateUtil.parseError(result));
        }
        try {
            itemCatService.update(itemCat);
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
    public ResponseResult<TbItemCat> findOne(Long id) {
        try {
            TbItemCat itemCat = itemCatService.findOne(id);
            return ResponseResult.ok(itemCat);
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
            itemCatService.delete(ids);
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
     * @param itemCat
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public ResponseResult<PageResult<TbItemCat>> search(@RequestBody TbItemCat itemCat, int page, int rows) {
        try {
            PageResult<TbItemCat> pageResult = itemCatService.findPage(itemCat, page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页搜索失败");
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
