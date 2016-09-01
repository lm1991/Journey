package com.mesor.journey.model;

/**
 * Created by Limeng on 2016/8/27.
 */
public class InfoMapResult {
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    private String info;
    private int _id;
}
