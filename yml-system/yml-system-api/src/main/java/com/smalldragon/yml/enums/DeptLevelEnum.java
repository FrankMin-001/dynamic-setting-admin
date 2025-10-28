package com.smalldragon.yml.enums;

/**
 * @author YML
 */

public enum DeptLevelEnum {

    ALL_DEPT(1, "全部部门数据数据权限"),
    CURRENT_DEPT_AND_SUB(2, "当前部门及以下数据权限"),
    CURRENT_DEPT(3, "当前部门数据权限"),
    CUSTOM_DEPT(4, "自定义部门数据权限"),
    BY_SELF(5, "仅限于本人数据权限");

    private final int code;
    private final String description;

    // 构造函数
    DeptLevelEnum(int code, String description) {
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
    public static DeptLevelEnum getByCode(int code) {
        for (DeptLevelEnum DeptLevelEnum : DeptLevelEnum.values()) {
            if (DeptLevelEnum.getCode() == code) {
                return DeptLevelEnum;
            }
        }
        throw new IllegalArgumentException("无效的编码: " + code);
    }

    // 通过编码返回中文描述
    public static String getValueByCode(int code) {
        return getByCode(code).getDescription();
    }
}
