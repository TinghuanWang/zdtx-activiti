package com.zdtx.process.utils;

/***
 * 响应信息枚举类
 * @author zdtx
 */
public enum ResultEnum {

    UNKNOWN_ERROR("unknown error", -1),
    SUCCESS("success", 1),
    FAIL("Request is failed", 0),
    TOKEN_INVALID("Token is null or invalid", 40001),
    ACCESS_DENIED("Access denied", 40003),
    FAIL4DELETE("Delete failed", 50001),
    FAIL4UPDATE("Update failed", 50002);


    public String msg;
    public int code;


    ResultEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;

    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
