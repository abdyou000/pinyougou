package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    public ResponseResult<Map<String,Object>> search(@RequestBody Map<String,Object> param) {
        try {
            Map<String, Object> map = itemSearchService.search(param);
            return ResponseResult.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("搜索出错");
        }
    }
}
