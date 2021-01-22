package com.smart.chatui.enity;

import java.io.Serializable;

/**
 * Created by gbh on 2018/5/10  10:18.
 *
 * @describe 发送文件
 */

public class SendFiles implements Serializable {
    private String fileUrl;
    private int msgType;
    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
