package com.czdx.parkingcharge.system.exception;

/**
 *
 * description: 不支持的计费规则异常类
 * @author mingchenxu
 * @date 2023/3/15 13:30
 */
public class UnsupportedChargeRuleException extends RuntimeException{

    public UnsupportedChargeRuleException(String message) {
        super(message);
    }
}
