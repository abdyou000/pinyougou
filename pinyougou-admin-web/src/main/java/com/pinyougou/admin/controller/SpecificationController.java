package com.pinyougou.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.common.pojo.SpecificationEntity;
import com.pinyougou.common.utils.ValidateUtil;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.seller.service.SpecificationService;
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
@RequestMapping("/specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public ResponseResult<List<TbSpecification>> findAll() {
        try {
            List<TbSpecification> brandList = specificationService.findAll();
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
    public ResponseResult<PageResult<TbSpecification>> findPage(int page, int rows) {
        try {
            PageResult<TbSpecification> pageResult = specificationService.findPage(page,rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页查询失败");
        }
    }

    /**
     * 增加
     *
     * @param specificationEntity
     * @return
     */
    @RequestMapping("/add")
    public ResponseResult<String> add(@RequestBody @Validated SpecificationEntity specificationEntity, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(ValidateUtil.parseError(result));
        }
        try {
            specificationService.add(specificationEntity);
            return ResponseResult.ok("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("增加失败");
        }
    }

    /**
     * 修改
     *
     * @param specificationEntity
     * @return
     */
    @RequestMapping("/update")
    public ResponseResult<String> update(@RequestBody @Validated SpecificationEntity specificationEntity, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(ValidateUtil.parseError(result));
        }
        try {
            specificationService.update(specificationEntity);
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
    public ResponseResult<SpecificationEntity> findOne(Long id) {
        try {
            SpecificationEntity entity = specificationService.findOne(id);
            return ResponseResult.ok(entity);
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
            specificationService.delete(ids);
            return ResponseResult.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param specification
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public ResponseResult<PageResult<TbSpecification>> search(@RequestBody TbSpecification specification, int page, int rows) {
        try {
            PageResult<TbSpecification> pageResult = specificationService.findPage(specification, page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页搜索失败");
        }
    }

}
