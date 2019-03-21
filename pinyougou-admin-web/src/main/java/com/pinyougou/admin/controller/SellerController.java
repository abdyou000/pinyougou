package com.pinyougou.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.common.utils.ValidateUtil;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.seller.service.SellerService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    /**
     * 修改
     *
     * @param seller
     * @return
     */
    @RequestMapping("/update")
    public ResponseResult<String> update(@RequestBody @Validated TbSeller seller, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(ValidateUtil.parseError(result));
        }
        try {
            sellerService.update(seller);
            return ResponseResult.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("修改失败");
        }
    }
    /**
     * 修改
     *
     * @return
     */
    @RequestMapping("/updateStatus")
    public ResponseResult<String> updateStatus(Long id,String status) {
        try {
            sellerService.updateStatus(id, status);
            return ResponseResult.ok("修改成功");
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
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
    public ResponseResult<TbSeller> findOne(Long id) {
        try {
            TbSeller seller = sellerService.findOne(id);
            return ResponseResult.ok(seller);
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
            sellerService.delete(ids);
            return ResponseResult.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param seller
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public ResponseResult<PageResult<TbSeller>> search(@RequestBody TbSeller seller, int page, int rows) {
        try {
            PageResult<TbSeller> pageResult = sellerService.findPage(seller, page, rows);
            return ResponseResult.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("分页搜索失败");
        }
    }

}
