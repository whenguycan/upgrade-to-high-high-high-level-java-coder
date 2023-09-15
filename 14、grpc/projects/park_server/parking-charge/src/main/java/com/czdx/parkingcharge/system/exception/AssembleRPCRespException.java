package com.czdx.parkingcharge.system.exception;

/**
 *
 * description: 组装RPC返回异常类
 * @author mingchenxu
 * @date 2023/3/15 13:30
 */
public class AssembleRPCRespException extends RuntimeException{

    public AssembleRPCRespException(String message) {
        super(message);
    }
}
