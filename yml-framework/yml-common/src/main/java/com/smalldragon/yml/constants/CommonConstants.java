package com.smalldragon.yml.constants;

/**
 * @Author YML
 * @Date 2025/3/13 17:54
 **/
public class CommonConstants {

    public static final String ADMIN = "admin";

    // 图形验证码 外置key
    public static final String CAPTCHA_CODE = "captcha::code";
    // 角色权限列表
    public static final String ROLE_PERMISSION_CACHE = "role::permission::cache";
    // 2分钟
    public static final long SECONDS_OF120 = 120;
    // 5分钟
    public static final long SECONDS_OF300 = 300;
    // 30分钟
    public static final long HALF_AN_HOUR = 1800;
    // 2小时
    public static final long TWO_HOURS = 7200;
    // 聊天房间后缀
    public static final String ROOM_END = "::chatroom";

    // BASE64编码前缀
    public static final String BASE64_PREFIX = "data:image/png;base64,";

    // 完成状态
    public static final String QUEUE_STATUS = "status";
    // 未完成
    public static final String QUEUE_UNFINISHED = "0";
    // 已完成
    public static final String QUEUE_FINISHED = "1";
    // 用户ID
    public static final String USER_ID = "userId";
    // 创建时间
    public static final String CREATE_TIME = "createTime";
    // 打卡消息队列
    public static final String SIGN_IN_STREAM = "SignInStream::Queue";
    // 打卡消息队列消费组
    public static final String SIGN_IN_CONSUMER_GROUP = "SignInConsumer::Group";
    // 打卡消息队列消费组-打卡消费者(如果还有添加积分的功能,那么就再加一个添加积分的功能)
    public static final String SIGN_IN_CONSUMER_DOING = "SignInConsumer::Doing";
}
