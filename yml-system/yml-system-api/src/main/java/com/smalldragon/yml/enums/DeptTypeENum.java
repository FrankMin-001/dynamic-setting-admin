package com.smalldragon.yml.enums;

public enum DeptTypeENum {

    ALl_DEPT(1, "全部数据权限"),
    SELF_DEPT(2, "本部门数据权限"),
    SELF_SUB_DEPT(3, "本部门及以下数据权限"),
    CUSTOM_DEPT(4, "自定义部门数据权限"),
    SELF_USER(5, "仅限于用户权限");

    private final int code; // 编码
    private final String description; // 中文描述

    // 构造函数
    DeptTypeENum(int code, String description) {
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
    public static DeptTypeENum getByCode(int code) {
        for (DeptTypeENum deptTypeENum : DeptTypeENum.values()) {
            if (deptTypeENum.getCode() == code) {
                return deptTypeENum;
            }
        }
        throw new IllegalArgumentException("无效的性别编码: " + code);
    }

    // 通过编码返回中文描述
    public static String getValueByCode(int code) {
        return getByCode(code).getDescription();
    }

}
