package trf.smt.com.netlibrary.bean;

import java.util.Map;

/**
 * Created by gbh on 2018/5/3  19:41.
 *
 * @describe
 */

public class ResBean<T> {
    private String msg;
    private String code;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return code.equals("0000");
    }


}
