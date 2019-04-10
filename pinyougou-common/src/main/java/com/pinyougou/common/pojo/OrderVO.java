package com.pinyougou.common.pojo;

import com.pinyougou.pojo.TbOrder;

public class OrderVO {
    private TbOrder order;
    private Cart criteriaCart;

    public TbOrder getOrder() {
        return order;
    }

    public void setOrder(TbOrder order) {
        this.order = order;
    }

    public Cart getCriteriaCart() {
        return criteriaCart;
    }

    public void setCriteriaCart(Cart criteriaCart) {
        this.criteriaCart = criteriaCart;
    }
}
