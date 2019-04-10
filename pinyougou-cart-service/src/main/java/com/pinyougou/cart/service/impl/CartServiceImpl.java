package com.pinyougou.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.pinyougou.common.pojo.Cart;
import com.pinyougou.common.pojo.CartDTO;
import com.pinyougou.common.pojo.CartItem;
import com.pinyougou.common.pojo.CartProduct;
import com.pinyougou.common.utils.CookieUtil;
import com.pinyougou.mapper.TbItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemDao itemMapper;

    @Override
    public void addGoodsToCart(CartDTO cartDTO,
                               Long itemId,
                               Integer num) {
        Cart cart = findCart(cartDTO);
        cart = addCart(cart, itemId, num);
        saveCart(cartDTO, cart);
    }


    private Cart addCart(Cart cart, Long itemId, Integer num) {

        List<CartItem> cartItemList = cart.getCartItemList();
        if (Objects.isNull(cartItemList)) {
            cart.setCartItemList(Lists.newArrayList());
        }
        //1.根据skuID查询商品明细SKU的对象
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!item.getStatus().equals("1")) {
            throw new RuntimeException("商品状态不合法");
        }
        //2.根据SKU对象得到商家ID
        String sellerId = item.getSellerId();//商家ID

        //3.根据商家ID在购物车列表中查询购物车明细对象
        CartItem cartItem = searchCartItemBySellerId(cart.getCartItemList(), sellerId);

        if (Objects.isNull(cartItem)) {//4.如果购物车列表中不存在该商家的购物车
            //4.1 创建一个新的购物车对象
            cartItem = createCartItem(item, num);
            //4.2将新的购物车对象添加到购物车列表中
            cartItemList.add(cartItem);
        } else {//5.如果购物车列表中存在该商家的购物车
            // 判断该商品是否在该购物车的明细列表中存在
            List<CartProduct> productList = cartItem.getProductList();
            CartProduct product = searchProductByItemId(productList, itemId);
            if (Objects.isNull(product)) {
                //5.1  如果不存在  ，创建新的购物车明细对象，并添加到该购物车的明细列表中
                product = createProduct(item, num);
                productList.add(product);
            } else {
                //5.2 如果存在，在原有的数量上添加数量 ,并且更新金额
                product.setNum(product.getNum() + num);//更改数量
                //金额
                product.setTotalFee(BigDecimal.valueOf(product.getNum()).multiply(product.getPrice()));
                //当明细的数量小于等于0，移除此明细
                if (product.getNum() <= 0) {
                    productList.remove(product);
                }
                //当购物车的明细数量为0，在购物车列表中移除此购物车
                if (productList.size() == 0) {
                    cartItemList.remove(cartItem);
                }
            }
        }
        return cart;
    }

    /**
     * 根据商家ID在购物车查询购物车明细对象
     *
     * @param cartItemList
     * @param sellerId
     * @return
     */
    private CartItem searchCartItemBySellerId(List<CartItem> cartItemList, String sellerId) {
        return cartItemList
                .stream()
                .filter(cartItem -> Objects.equals(cartItem.getSellerId(), sellerId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据skuID在购物车明细列表中查询购物车明细对象
     *
     * @param productList
     * @param itemId
     * @return
     */
    private CartProduct searchProductByItemId(List<CartProduct> productList, Long itemId) {
        return productList.stream()
                .filter(product -> Objects.equals(product.getItemId(), itemId))
                .findFirst()
                .orElse(null);
    }

    private CartItem createCartItem(TbItem item, Integer num) {
        CartItem cartItem = new CartItem();
        cartItem.setSellerId(item.getSellerId());//商家ID
        cartItem.setSellerName(item.getSeller());//商家名称
        List<CartProduct> productList = Lists.newArrayList();//创建购物车明细列表
        CartProduct product = createProduct(item, num);
        productList.add(product);
        cartItem.setProductList(productList);
        cartItem.setChecked(false);
        return cartItem;
    }

    /**
     * 创建购物车明细对象
     *
     * @param item
     * @param num
     * @return
     */
    private CartProduct createProduct(TbItem item, Integer num) {
        //创建新的购物车明细对象
        CartProduct product = new CartProduct();
        product.setGoodsId(item.getGoodsId());
        product.setItemId(item.getId());
        product.setNum(num);
        product.setPicPath(item.getImage());
        product.setPrice(item.getPrice());
        product.setSellerId(item.getSellerId());
        product.setTitle(item.getTitle());
        product.setTotalFee(item.getPrice().multiply(BigDecimal.valueOf(num)));
        product.setChecked(false);
        return product;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    private Cart findCartFromRedis(String username) {
        return (Cart) redisTemplate.boundHashOps("cart").get(username);
    }

    private void saveCartToRedis(String username, Cart cart) {
        redisTemplate.boundHashOps("cart").put(username, cart);
    }

    private Cart mergeCart(Cart cookieCart, Cart redisCart) {
        // cartList1.addAll(cartList2);  不能简单合并
        for (CartItem cartItem : cookieCart.getCartItemList()) {
            for (CartProduct product : cartItem.getProductList()) {
                redisCart = addCart(cookieCart, product.getItemId(), product.getNum());
            }
        }
        return redisCart;
    }

    @Override
    public Cart findCart(CartDTO cartDTO) {
        HttpServletRequest request = cartDTO.getRequest();
        HttpServletResponse response = cartDTO.getResponse();
        String username = cartDTO.getUsername();
        String cartString = CookieUtil.getCookieValue(request, "cart", "UTF-8");
        if (Strings.isNullOrEmpty(cartString)) {
            cartString = "{}";
        }
        Cart cookieCart = JSON.parseObject(cartString, Cart.class);
        if (Objects.isNull(cookieCart.getChecked())) {
            cookieCart.setChecked(false);
        }
        if (logged(username)) {
            //获取redis购物车
            Cart redisCart = findCartFromRedis(username);
            if (Objects.nonNull(cookieCart.getCartItemList())) {//判断当本地购物车中存在数据
                //得到合并后的购物车
                redisCart = mergeCart(cookieCart, redisCart);
                //将合并后的购物车存入redis
                saveCartToRedis(username, redisCart);
                //本地购物车清除
                CookieUtil.deleteCookie(request, response, "cart");
            }
            return redisCart;
        } else {
            return cookieCart;
        }
    }

    @Override
    public Cart findSelectedCart(CartDTO cartDTO) {
        Cart cart = findCart(cartDTO);
        if (cart.getChecked()) {
            return cart;
        }
        Cart selectedCart = new Cart();
        for (CartItem cartItem : cart.getCartItemList()) {
            for (CartProduct product : cartItem.getProductList()) {
                selectedCart = addCart(selectedCart, product.getItemId(), product.getNum());
            }
        }
        return selectedCart;
    }

    @Override
    public Cart findSelectedCart(CartDTO cartDTO, Cart criteriaCart) {
        Cart cart = findCart(cartDTO);
        Cart selectedCart = new Cart();

        for (CartItem cartItem : criteriaCart.getCartItemList()) {
            for (CartProduct product : cartItem.getProductList()) {
                CartItem cartItem1 = searchCartItemBySellerId(cart.getCartItemList(), cartItem.getSellerId());
                CartProduct cartProduct = searchProductByItemId(cartItem1.getProductList(), product.getItemId());
                selectedCart = addCart(selectedCart, cartProduct.getItemId(), cartProduct.getNum());
            }
        }
        return selectedCart;
    }

    @Override
    public Cart findSelectedCart(String username,Cart criteriaCart) {
        Cart cart = findCartFromRedis(username);
        Cart selectedCart = new Cart();

        for (CartItem cartItem : criteriaCart.getCartItemList()) {
            for (CartProduct product : cartItem.getProductList()) {
                CartItem cartItem1 = searchCartItemBySellerId(cart.getCartItemList(), cartItem.getSellerId());
                CartProduct cartProduct = searchProductByItemId(cartItem1.getProductList(), product.getItemId());
                selectedCart = addCart(selectedCart, cartProduct.getItemId(), cartProduct.getNum());
            }
        }
        return selectedCart;
    }

    @Override
    public void selectCart(CartDTO cartDTO, Boolean checked) {
        Cart cart = findCart(cartDTO);
        cart.setChecked(checked);
        cart.getCartItemList()
                .forEach(cartItem -> {
                    cartItem.setChecked(checked);
                    cartItem.getProductList()
                            .forEach(product -> product.setChecked(checked));
                });
        saveCart(cartDTO, cart);
    }

    @Override
    public void selectCartItem(CartDTO cartDTO, String sellerId, Boolean checked) {
        Cart cart = findCart(cartDTO);
        cart.getCartItemList().stream()
                .filter(cartItem -> Objects.equals(sellerId, cartItem.getSellerId()))
                .forEach(cartItem -> {
                    cartItem.setChecked(checked);
                    cartItem.getProductList()
                            .forEach(product -> product.setChecked(checked));
                });
        boolean allChecked = cart.getCartItemList().stream().anyMatch(CartItem::getChecked);
        boolean allNotChecked = cart.getCartItemList().stream().anyMatch(cartItem -> !cartItem.getChecked());
        if (allChecked) {
            cart.setChecked(allChecked);
        }
        if (allNotChecked) {
            cart.setChecked(allNotChecked);
        }
        saveCart(cartDTO, cart);
    }

    @Override
    public void selectCartProduct(CartDTO cartDTO, Long itemId, Boolean checked) {
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        Cart cart = findCart(cartDTO);
        cart.getCartItemList().stream()
                .filter(cartItem -> Objects.equals(item.getSellerId(), cartItem.getSellerId()))
                .forEach(cartItem -> {
                    cartItem.getProductList()
                            .stream()
                            .filter(product -> Objects.equals(itemId, product.getItemId()))
                            .forEach(product -> product.setChecked(checked));
                    boolean allChecked = cartItem.getProductList().stream().anyMatch(CartProduct::getChecked);
                    boolean allNotChecked = cartItem.getProductList().stream().anyMatch(product -> !product.getChecked());
                    if (allChecked) {
                        cartItem.setChecked(allChecked);
                    }
                    if (allNotChecked) {
                        cartItem.setChecked(allNotChecked);
                    }
                });

        boolean allChecked = cart.getCartItemList().stream().anyMatch(CartItem::getChecked);
        boolean allNotChecked = cart.getCartItemList().stream().anyMatch(cartItem -> !cartItem.getChecked());
        if (allChecked) {
            cart.setChecked(allChecked);
        }
        if (allNotChecked) {
            cart.setChecked(allNotChecked);
        }
        saveCart(cartDTO, cart);
    }

    private boolean logged(String username) {
        return !Objects.equals("anonymousUser", username);
    }

    private void saveCart(CartDTO cartDTO, Cart cart) {
        HttpServletRequest request = cartDTO.getRequest();
        HttpServletResponse response = cartDTO.getResponse();
        String username = cartDTO.getUsername();
        if (logged(username)) {
            saveCartToRedis(username, cart);
        } else {
            //将新的购物车存入cookie
            String cartString = JSON.toJSONString(cart);
            CookieUtil.setCookie(request,
                    response,
                    "cart",
                    cartString,
                    3600 * 24,
                    "UTF-8");
        }
    }
}
