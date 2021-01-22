package com.smart.chatui.enity;

/**
 * Created by gbh on 2018/5/3  17:50.
 *
 * @describe
 */

public class ErrMsg {

    /**
     * msg : 密码错误.
     * code : 9998
     * detail : 密码错误.
     */

    private String msg;
    private String code;
    private String detail;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
