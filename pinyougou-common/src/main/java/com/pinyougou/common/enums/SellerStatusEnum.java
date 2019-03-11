package com.pinyougou.common.enums;

public enum  SellerStatusEnum {
    NOTAUDIT("未审核"),
    AUDITPASS("审核通过"),
    AUDITFAIL("审核未通过"),
    CLOSED("店铺已关闭");

    private String type;
    SellerStatusEnum(String type) {
        this.type = type;
    }
}

