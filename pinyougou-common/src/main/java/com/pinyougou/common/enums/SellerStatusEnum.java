package com.pinyougou.common.enums;

public enum  SellerStatusEnum {
    NOTAUDIT("未审核","0"),
    AUDITPASS("审核通过","1"),
    AUDITFAIL("审核未通过","2"),
    CLOSED("店铺已关闭","3");

    private String type;
    private String code;
    SellerStatusEnum(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public static SellerStatusEnum valueFrom(String code) {
        for (SellerStatusEnum statusEnum : values()) {
            if (statusEnum.code.equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}

