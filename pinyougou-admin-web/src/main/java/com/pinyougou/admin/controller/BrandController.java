package com.pinyougou.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.pinyougou.common.PageResult;
import com.pinyougou.common.ResponseResult;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.seller.service.BrandService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public ResponseResult<List<TbBrand>> findAll() {
        try {
            List<TbBrand> brandList = brandService.findAll();
            return ResponseResult.ok(brandList);
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
    public ResponseResult<PageResult<TbBrand>> findPage(int page, int rows) {
        try {
            PageResult<TbBrand> pageResult = brandService.findPage(page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页查询失败");
        }
    }

    /**
     * 增加
     *
     * @param brand
     * @return
     */
    @RequestMapping("/add")
    public ResponseResult<String> add(@RequestBody @Validated TbBrand brand, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(parseError(result));
        }
        try {
            brandService.add(brand);
            return ResponseResult.ok("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("增加失败");
        }
    }

    /**
     * 修改
     *
     * @param brand
     * @return
     */
    @RequestMapping("/update")
    public ResponseResult<String> update(@RequestBody @Validated TbBrand brand, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(parseError(result));
        }
        try {
            brandService.update(brand);
            return ResponseResult.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("修改失败");
        }
    }

    private String parseError(BindingResult result) {
        List<Map<String, String>> fieldErrors = result.getFieldErrors()
                .stream()
                .map(x -> {
                    Map<String, String> map = Maps.newHashMap();
                    map.put("field", x.getField());
                    map.put("message", x.getDefaultMessage());
                    return map;
                })
                .collect(Collectors.toList());
        return JSON.toJSONString(fieldErrors);
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public ResponseResult<TbBrand> findOne(Long id) {
        try {
            TbBrand tbBrand = brandService.findOne(id);
            return ResponseResult.ok(tbBrand);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("删除失败");
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
            brandService.delete(ids);
            return ResponseResult.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param brand
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public ResponseResult<PageResult<TbBrand>> search(@RequestBody TbBrand brand, int page, int rows) {
        try {
            PageResult<TbBrand> pageResult = brandService.findPage(brand, page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页搜索失败");
        }
    }

}
