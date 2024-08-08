package org.franco.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"Success"),
    // 登录
    NEED_LOGIN(401,"Login required"),
    NO_OPERATOR_AUTH(403,"Not authorised"),
    SYSTEM_ERROR(500,"Error"),
    USERNAME_EXIST(409,"Username already exists"),
    NICKNAME_EXIST(409,"Nickname already exists"),
    PHONENUMBER_EXIST(409,"Phone number already exists"),
    EMAIL_EXIST(409, "Email already exists"),
    REQUIRE_USERNAME(401, "Username required"),
    LOGIN_ERROR(401,"Wrong username or password"),
    EMPTY_CONTENT_ERROR(400, "Empty comment"),
    FILE_TYPE_ERROR(422, "File type not supported"),
    EMPTY_USERNAME(400, "Username is empty"),
    EMPTY_EMAIL(400, "Email is empty"),
    EMPTY_PASSWORD(400, "Password is empty"),
    EMPTY_NICKNAME(400, "Nickname is empty"),

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
