package org.franco.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"Success"),
    // 登录
    NEED_LOGIN(401,"Login required"),
    NO_OPERATOR_AUTH(403,"Not authorised"),
    SYSTEM_ERROR(500,"Error"),
    USERNAME_EXIST(501,"Username already exists"),
    PHONENUMBER_EXIST(502,"Phone number already exists"),
    EMAIL_EXIST(503, "Email already exists"),
    REQUIRE_USERNAME(401, "Username required"),
    LOGIN_ERROR(401,"Wrong username or password"),
    EMPTY_CONTENT_ERROR(400, "Empty comment")
    ;
    int code;
    String msg;
    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
