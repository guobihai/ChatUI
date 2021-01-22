package trf.smt.com.netlibrary.bean;


import android.text.TextUtils;

import trf.smt.com.netlibrary.utils.JsonUtils;

/**
 * Created by gbh on 2018/4/8  14:02.
 *
 * @describe
 */

public class Send<T> {
    //1、通信录 ： username ,nikeName,remarkName,iconUrl:http:xxxx,,
    //2 登陆
    // loginout  fromDeviceId  toDeviceId
    //  心跳包

    private String tag;//消息类型 : txt(文本), heat
    private int len;//数据长度
    private T value;//数据体

    private String fromDevicesId;//发送设备ID号

    private String toDevicesId;//接收设备ID(手动设置) FFFFFFFF

    private String action;//行为

    private String ruleId;//规则ID


    public Send(String tag, T value, String fromDeviceId, String toDevicesId) {
        this.tag = tag;
        this.value = value;
        this.fromDevicesId = fromDeviceId;
        this.toDevicesId = toDevicesId;
        setLen(JsonUtils.serialize(value).length());
    }

    public String getFromDevicesId() {
        return fromDevicesId;
    }

    public String getTag() {
        return tag;
    }


    public void setLen(int len) {
        this.len = len;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }


    public String getAction() {
        return TextUtils.isEmpty(action) ? "" : action;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public String getRuleId() {
        return TextUtils.isEmpty(ruleId) ? "" : ruleId;
    }


    public boolean isSingleChart() {
        return getAction().equals("3") && getRuleId().equals("3");
    }

    public boolean isGroupChart() {
        return getAction().equals("1") && getRuleId().equals("2");
    }

    @Override
    public String toString() {
//        "{tag:"03","len":"16",value:"{"fromuser:"123",toUser:"777""}"}"
        return JsonUtils.serialize(this);
    }
}
