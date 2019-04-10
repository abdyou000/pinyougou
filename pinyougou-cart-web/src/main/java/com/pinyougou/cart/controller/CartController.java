package com.pinyougou.cart.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.pinyougou.common.pojo.Cart;
import com.pinyougou.common.pojo.CartDTO;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.common.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/findCart")
    public ResponseResult<Cart> findCart() {
        //当前登录人账号
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录人：" + username);
        try {
            Cart cart = cartService.findCart(new CartDTO(request, response, username));
            return ResponseResult.ok(cart);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("查询购物车出错");
        }

    }

    @RequestMapping("/findSelectedCart")
    public ResponseResult<Cart> findSelectedCart() {
        //当前登录人账号
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录人：" + username);
        try {
            Cart cart = cartService.findSelectedCart(new CartDTO(request, response, username));
            return ResponseResult.ok(cart);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("查询购物车出错");
        }

    }
    @RequestMapping("/addGoodsToCart")
    @CrossOrigin(origins = "http://localhost:9105")
    public ResponseResult<String> addGoodsToCartList(Long itemId, Integer num) {

        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");//可以访问的域(当此方法不需要操作cookie)
        //response.setHeader("Access-Control-Allow-Credentials", "true");//如果操作cookie，必须加上这句话
        //当前登录人账号
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录人：" + username);
        try {
            cartService.addGoodsToCart(new CartDTO(request, response, username), itemId, num);
            return ResponseResult.ok("存入购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("存入购物车失败");
        }
    }

    @RequestMapping("/selectCart")
    public ResponseResult<String> selectCart(Boolean checked) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            cartService.selectCart(new CartDTO(request, response, username),checked);
            return ResponseResult.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("修改出错");
        }
    }

    @RequestMapping("/selectCartItem")
    public ResponseResult<String> selectCartItem(String sellerId,Boolean checked) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            cartService.selectCartItem(new CartDTO(request, response, username),sellerId,checked);
            return ResponseResult.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("修改出错");
        }
    }
    @RequestMapping("/selectCartProduct")
    public ResponseResult<String> selectCartProduct(Long itemId,Boolean checked) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            cartService.selectCartProduct(new CartDTO(request, response, username),itemId,checked);
            return ResponseResult.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("修改出错");
        }
    }
}
