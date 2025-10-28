package com.smalldragon.yml.enums;

/**
 * @author YML
 */

public enum SexyEnum {
    MALE(0, "男"),
    FEMALE(1, "女");

    private final int code; // 编码
    private final String description; // 中文描述

    // 构造函数
    SexyEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    // 获取编码
    public int getCode() {
        return code;
    }

    // 获取中文描述
    public String getDescription() {
        return description;
    }

    // 通过编码返回枚举值
    public static SexyEnum getByCode(int code) {
        for (SexyEnum sexyEnum : SexyEnum.values()) {
            if (sexyEnum.getCode() == code) {
                return sexyEnum;
            }
        }
        throw new IllegalArgumentException("无效的性别编码: " + code);
    }

    // 通过编码返回中文描述
    public static String getValueByCode(int code) {
        return getByCode(code).getDescription();
    }

}
