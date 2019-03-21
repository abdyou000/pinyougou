package com.pinyougou.common.enums;

public enum GoodsStatusEnum {
    NOTAUDIT("未审核","0"),
    AUDITPASS("审核通过","1"),
    AUDITFAIL("审核未通过","2"),
    CLOSED("已关闭","3");

    private String type;
    private String code;
    GoodsStatusEnum(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public static GoodsStatusEnum valueFrom(String code) {
        for (GoodsStatusEnum statusEnum : values()) {
            if (statusEnum.code.equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}

