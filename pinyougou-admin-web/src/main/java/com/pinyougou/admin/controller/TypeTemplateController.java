package com.pinyougou.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.common.utils.ValidateUtil;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.seller.service.TypeTemplateService;
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
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public ResponseResult<List<TbTypeTemplate>> findAll() {
        try {
            List<TbTypeTemplate> tbTypeTemplateList = typeTemplateService.findAll();
            return ResponseResult.ok(tbTypeTemplateList);
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
    public ResponseResult<PageResult<TbTypeTemplate>> findPage(int page, int rows) {
        try {
            PageResult<TbTypeTemplate> pageResult = typeTemplateService.findPage(page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页查询失败");
        }
    }

    /**
     * 增加
     *
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/add")
    public ResponseResult<String> add(@RequestBody @Validated TbTypeTemplate typeTemplate, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(ValidateUtil.parseError(result));
        }
        try {
            typeTemplateService.add(typeTemplate);
            return ResponseResult.ok("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("增加失败");
        }
    }

    /**
     * 修改
     *
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/update")
    public ResponseResult<String> update(@RequestBody @Validated TbTypeTemplate typeTemplate, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(ValidateUtil.parseError(result));
        }
        try {
            typeTemplateService.update(typeTemplate);
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
    public ResponseResult<TbTypeTemplate> findOne(Long id) {
        try {
            TbTypeTemplate typeTemplate = typeTemplateService.findOne(id);
            return ResponseResult.ok(typeTemplate);
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
            typeTemplateService.delete(ids);
            return ResponseResult.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param typeTemplate
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public ResponseResult<PageResult<TbTypeTemplate>> search(@RequestBody TbTypeTemplate typeTemplate, int page, int rows) {
        try {
            PageResult<TbTypeTemplate> pageResult = typeTemplateService.findPage(typeTemplate, page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页搜索失败");
        }
    }

}
