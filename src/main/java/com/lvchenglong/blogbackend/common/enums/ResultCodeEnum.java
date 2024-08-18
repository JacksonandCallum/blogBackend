package com.lvchenglong.blogbackend.common.enums;

public enum ResultCodeEnum {

    SUCCESS("200", "成功"),

    PARAM_ERROR("400", "参数异常"),
    TOKEN_INVALID_ERROR("401", "无效的token"),
    TOKEN_CHECK_ERROR("401", "token验证失败，请重新登录"),
    PARAM_LOST_ERROR("4001", "参数缺失"),

    SYSTEM_ERROR("500", "系统异常"),
    USER_EXIST_ERROR("5001", "用户名已存在"),
    USER_NOT_LOGIN("5002", "用户未登录"),
    USER_ACCOUNT_ERROR("5003", "账号或密码错误"),
    USER_NOT_EXIST_ERROR("5004", "用户不存在"),
    PARAM_PASSWORD_ERROR("5005", "原密码输入错误"),
    ACTIVITY_SIGN_ERROR("5006", "活动已报名"),
    EMAIL_ERROR("5007", "邮箱格式不正确"),
    CODE_ERROR("5008", "验证码不正确"),
    EMAIL_ALREADY_EXIST("6001", "邮箱已被注册"),
    PASSWORD_INCONSISTENT("6002", "密码不一致"),
    PARAM_ILLEGAL("6003", "参数不合法"),
    ILLEGAL_OPERATION("6005", "非法操作"),
    PASSWORD_ERROR("6006", "密码错误"),
    EMAIL_ALREADY_EXISTS("6007","邮箱已存在"),
    UPLOAD_ERROR("6008","上传失败");


    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
