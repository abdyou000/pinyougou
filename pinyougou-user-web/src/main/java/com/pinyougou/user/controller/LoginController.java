package com.pinyougou.user.controller;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.pinyougou.common.pojo.ResponseResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/name")
    public ResponseResult<Map<String,String>> showName() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,String> map = Maps.newHashMap();
        map.put("loginName", name);
        return ResponseResult.ok(map);
    }

}
