package com.smalldragon.yml.enums;

/**
 * @author YML
 */

public enum ResourceTypeEnum {

    FUNCTION ("function", "功能权限"),
    MENU("menu", "菜单权限");

    private final String code; // 编码
    private final String description; // 中文描述

    // 构造函数
    ResourceTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // 获取编码
    public String getCode() {
        return code;
    }

    // 获取中文描述
    public String getDescription() {
        return description;
    }

    // 通过编码返回枚举值
    public static ResourceTypeEnum getByCode(int code) {
        for (ResourceTypeEnum resourceTypeEnum : ResourceTypeEnum.values()) {
            if (resourceTypeEnum.getCode() .equals(code)) {
                return resourceTypeEnum;
            }
        }
        throw new IllegalArgumentException("无效的性别编码: " + code);
    }

    // 通过编码返回中文描述
    public static String getValueByCode(int code) {
        return getByCode(code).getDescription();
    }

}
