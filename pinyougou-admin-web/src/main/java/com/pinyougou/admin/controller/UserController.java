package com.pinyougou.admin.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/showName")
    public Map<String, String> getUserName() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ImmutableMap.of("username", username);
    }
}
