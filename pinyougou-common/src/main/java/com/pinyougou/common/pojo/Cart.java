package com.pinyougou.common.pojo;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    private Boolean checked;



    private List<CartItem> cartItemList;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }
}
