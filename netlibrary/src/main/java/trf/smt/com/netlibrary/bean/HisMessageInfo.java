package trf.smt.com.netlibrary.bean;

import android.text.TextUtils;

import java.util.List;

import trf.smt.com.netlibrary.enity.MessageInfo;

/**
 * Created by gbh on 2018/5/15  17:11.
 *
 * @describe
 */

public class HisMessageInfo {


    private String msg;
    private String code;
    private DataBeanX data;

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

    public boolean isSuccess() {
        if (TextUtils.isEmpty(code)) return false;
        return code.equals("0000");
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {

        private int total;
        private List<MessageInfo> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<MessageInfo> getData() {
            return data;
        }

        public void setData(List<MessageInfo> data) {
            this.data = data;
        }

    }
}
