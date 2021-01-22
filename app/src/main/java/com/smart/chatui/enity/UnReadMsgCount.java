package com.smart.chatui.enity;

import java.io.Serializable;

/**
 * Created by gbh on 2018/5/16  16:33.
 *
 * @describe
 */

public class UnReadMsgCount implements Serializable{
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
