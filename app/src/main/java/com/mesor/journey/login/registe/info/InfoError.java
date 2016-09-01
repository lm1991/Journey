package com.mesor.journey.login.registe.info;

/**
 * Created by Limeng on 2016/8/28.
 */
public class InfoError {
    /**
     * 用户名已存在:202
     * email已存在:203
     * mobilePhoneNumber已存在：214
     */
    private int code;
    private String error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
