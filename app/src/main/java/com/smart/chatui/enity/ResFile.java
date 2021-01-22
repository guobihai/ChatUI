package com.smart.chatui.enity;

import java.util.List;

/**
 * Created by gbh on 2018/4/17  14:47.
 *
 * @describe 文件上传返回信息
 */

public class ResFile {


    /**
     * msg :
     * code : 0000
     * data : [{"file":"c67ab679-c8fc-48ff-88ce-65fab4cc8253.png","serverUrl":"http://192.168.5.251:5050/upload/files/","fileUrl":"weixin/123/20180419/image/c67ab679-c8fc-48ff-88ce-65fab4cc8253.png","id":"c67ab679-c8fc-48ff-88ce-65fab4cc8253"}]
     */

    private String msg;
    private String code;
    private List<DataBean> data;

    private boolean isSuccess;

    public boolean isSuccess() {
        return getCode().equals("0000");
    }

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

    public static class DataBean {
        /**
         * file : c67ab679-c8fc-48ff-88ce-65fab4cc8253.png
         * serverUrl : http://192.168.5.251:5050/upload/files/
         * fileUrl : weixin/123/20180419/image/c67ab679-c8fc-48ff-88ce-65fab4cc8253.png
         * id : c67ab679-c8fc-48ff-88ce-65fab4cc8253
         */

        private String file;
        private String serverUrl;
        private String fileUrl;
        private String id;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getServerUrl() {
            return serverUrl;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
