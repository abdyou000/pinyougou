package com.pinyougou.seller.controller;

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
import java.util.Map;

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

    @RequestMapping("/findSpecList")
    public ResponseResult findSpecList(Long id) {
        try {
            List<Map<String,Object>> mapList = typeTemplateService.findSpecList(id);
            return ResponseResult.ok(mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("查询失败");
        }
    }
}
