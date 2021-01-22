package trf.smt.com.netlibrary.enity;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by gbh on 2018/5/11  15:32.
 *
 * @describe 待回复
 */

public class Cliams {

    /**
     * msg :
     * code : 0000
     * data : [{"recieveTime":"2018-05-11 14:05:03","content":"9687","fromuser":"wxid_9228942288022","msgType":1}]
     */

    private String msg;
    private String code;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        if(TextUtils.isEmpty(code))return false;
        return code.equals("0000");
    }

    public static class DataBean {
        /**
         * recieveTime : 2018-05-11 14:05:03
         * content : 9687
         * fromuser : wxid_9228942288022
         * msgType : 1
         */

        private String recieveTime;
        private String content;
        private String fromuser;
        private int msgType;
        private String msgCount;
        private String iconUrl;
        private String nikeName;

        public String getRecieveTime() {
            return TextUtils.isEmpty(recieveTime)?"":recieveTime.replaceAll("-","/");
        }

        public void setRecieveTime(String recieveTime) {
            this.recieveTime = recieveTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFromuser() {
            return fromuser;
        }

        public void setFromuser(String fromuser) {
            this.fromuser = fromuser;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public String getMsgCount() {
            return msgCount;
        }

        public void setMsgCount(String msgCount) {
            this.msgCount = msgCount;
        }

        public String getIconUrl() {
            return TextUtils.isEmpty(iconUrl) ? "" : iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getNikeName() {
            return TextUtils.isEmpty(nikeName) ? "" : nikeName;
        }

        public void setNikeName(String nikeName) {
            this.nikeName = nikeName;
        }


        public Person dataToPerson(DataBean dataBean) {
            Person person = new Person();
            person.setType(1);
            person.setIconUrl(dataBean.getIconUrl());
            person.setNickName(dataBean.getNikeName());
            person.setUserName(dataBean.getFromuser());
            person.setConRemark(dataBean.getNikeName());
            return person;
        }
    }
}
