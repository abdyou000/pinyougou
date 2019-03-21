package com.pinyougou.seller.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.ImmutableMap;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.common.utils.ValidateUtil;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.seller.service.SellerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
     * 增加
     *
     * @param seller
     * @return
     */
    @RequestMapping("/add")
    public ResponseResult<String> add(@RequestBody @Validated TbSeller seller, BindingResult result) {
        if (result.hasFieldErrors()) {
            return ResponseResult.error(ValidateUtil.parseError(result));
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        seller.setPassword(encoder.encode(seller.getPassword()));
        try {
            sellerService.add(seller);
            return ResponseResult.ok("增加成功");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("增加失败");
        }
    }

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
     * 读取用户名
     * @return
     */
    @RequestMapping("/showName")
    public ResponseResult<Map<String, String>> showName() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseResult.ok(ImmutableMap.of("username", username));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("读取用户名失败");
        }
    }
}
