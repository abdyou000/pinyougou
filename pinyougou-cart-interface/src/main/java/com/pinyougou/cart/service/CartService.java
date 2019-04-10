package com.pinyougou.cart.service;

import com.pinyougou.common.pojo.Cart;
import com.pinyougou.common.pojo.CartDTO;
import com.pinyougou.common.pojo.CartItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 购物车服务接口
 *
 * @author Administrator
 */
public interface CartService {

    /**
     * 添加商品到购物车
     *
     * @param itemId
     * @param num
     * @return
     */
    void addGoodsToCart(CartDTO cartDTO,
                        Long itemId,
                        Integer num);

    Cart findCart(CartDTO cartDTO);

    Cart findSelectedCart(CartDTO cartDTO);

    Cart findSelectedCart(CartDTO cartDTO,Cart cart);
    Cart findSelectedCart(String username,Cart cart);

    void selectCart(CartDTO cartDTO, Boolean checked);

    void selectCartItem(CartDTO cartDTO,String sellerId, Boolean checked);

    void selectCartProduct(CartDTO cartDTO,Long itemId, Boolean checked);
}
