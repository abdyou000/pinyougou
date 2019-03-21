package com.pinyougou.admin.controller;

import com.google.common.collect.ImmutableMap;
import com.pinyougou.common.pojo.ResponseResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/showName")
    public ResponseResult<Map<String, String>> getUserName() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseResult.ok(ImmutableMap.of("username", username));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("读取用户名失败");
        }
    }
}
