package com.ruoyi.common.constant;

/**
 * 缓存的key 常量
 *
 * @author ruoyi
 */
public class CacheConstants {
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 岗亭和通道绑定 redis key
     */
    public static final String SENTRYBOX_PASSAGE_KEY = "sentrybox_passage:";

    /**
     * 车场和通道绑定 redis key
     */
    public static final String PARKNO_PASSAGE_KEY = "parkno_passage:";

    /**
     * 车场空位 redis key
     */
    public static final String PARKNO_ACCOUNT_KEY = "parkno_account:";

    /**
     * 预支付
     */
    public static final String ADVANCE_PAYMENT = "advance_payment:";

    /**
     * 缓存的订单信息(包含预支付 与 岗亭支付的订单) redis key
     */
    public static final String PARKING_ORDER = "parking_order:";

    /**
     * 停车订单缓存时长 - 分钟
     */
    public static final Integer PARKING_ORDER_EXPIRE_DURATION_MINUTES = 15;
}
