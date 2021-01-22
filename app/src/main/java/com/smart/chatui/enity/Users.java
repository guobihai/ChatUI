package com.smart.chatui.enity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gbh on 2018/5/3  17:49.
 *
 * @describe 用户登录
 */

public class Users implements Serializable {


    /**
     * data : {"USER_ORG_ID":-9999,"LOGIN_ID":27052,"USER_ID":4211,"COMPANY_ID":1,"IP_TABLE":{"companyId":1,"domain":"mess.csosm.com","port":62280},"MAX_ROLE":5,"USER_ORG_ST_NAME":null,"USER_COMPANY_NAME":"动感曲线","DEVICE_ID":"351824070515901","USER_ORG_CODE":"-9999","USER_DOMAIN":"http://svr02.csosm.com:6001","ORG_DEEP":0,"USER_STORE_ID":1117,"USER_IP":"116.21.163.38","USER_COMPANY_ID":1,"ALL_STORES":[1117,1117],"SUB_STORES":[1117],"USER_NAME":"测试导购一","USER_ORG_NAME":"NO_ORG_NAME","USER_ORG_LEVEL":-1,"USER_ROLE_IDS":[7,5]}
     * code : 0000
     * msg :
     */

    private DataBean data;
    private String code;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean implements Serializable {
        /**
         * USER_ORG_ID : -9999
         * LOGIN_ID : 27052
         * USER_ID : 4211
         * COMPANY_ID : 1
         * IP_TABLE : {"companyId":1,"domain":"mess.csosm.com","port":62280}
         * MAX_ROLE : 5
         * USER_ORG_ST_NAME : null
         * USER_COMPANY_NAME : 动感曲线
         * DEVICE_ID : 351824070515901
         * USER_ORG_CODE : -9999
         * USER_DOMAIN : http://svr02.csosm.com:6001
         * ORG_DEEP : 0
         * USER_STORE_ID : 1117
         * USER_IP : 116.21.163.38
         * USER_COMPANY_ID : 1
         * ALL_STORES : [1117,1117]
         * SUB_STORES : [1117]
         * USER_NAME : 测试导购一
         * USER_ORG_NAME : NO_ORG_NAME
         * USER_ORG_LEVEL : -1
         * USER_ROLE_IDS : [7,5]
         */

        private int USER_ORG_ID;
        private int LOGIN_ID;
        private int USER_ID;
        private int COMPANY_ID;
        private IPTABLEBean IP_TABLE;
        private int MAX_ROLE;
        private Object USER_ORG_ST_NAME;
        private String USER_COMPANY_NAME;
        private String DEVICE_ID;
        private String USER_ORG_CODE;
        private String USER_DOMAIN;
        private int ORG_DEEP;
        private int USER_STORE_ID;
        private String USER_IP;
        private int USER_COMPANY_ID;
        private String USER_NAME;
        private String USER_ORG_NAME;
        private int USER_ORG_LEVEL;
        private List<Integer> ALL_STORES;
        private List<Integer> SUB_STORES;
        private List<Integer> USER_ROLE_IDS;

        public int getUSER_ORG_ID() {
            return USER_ORG_ID;
        }

        public void setUSER_ORG_ID(int USER_ORG_ID) {
            this.USER_ORG_ID = USER_ORG_ID;
        }

        public int getLOGIN_ID() {
            return LOGIN_ID;
        }

        public void setLOGIN_ID(int LOGIN_ID) {
            this.LOGIN_ID = LOGIN_ID;
        }

        public int getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(int USER_ID) {
            this.USER_ID = USER_ID;
        }

        public int getCOMPANY_ID() {
            return COMPANY_ID;
        }

        public void setCOMPANY_ID(int COMPANY_ID) {
            this.COMPANY_ID = COMPANY_ID;
        }

        public IPTABLEBean getIP_TABLE() {
            return IP_TABLE;
        }

        public void setIP_TABLE(IPTABLEBean IP_TABLE) {
            this.IP_TABLE = IP_TABLE;
        }

        public int getMAX_ROLE() {
            return MAX_ROLE;
        }

        public void setMAX_ROLE(int MAX_ROLE) {
            this.MAX_ROLE = MAX_ROLE;
        }

        public Object getUSER_ORG_ST_NAME() {
            return USER_ORG_ST_NAME;
        }

        public void setUSER_ORG_ST_NAME(Object USER_ORG_ST_NAME) {
            this.USER_ORG_ST_NAME = USER_ORG_ST_NAME;
        }

        public String getUSER_COMPANY_NAME() {
            return USER_COMPANY_NAME;
        }

        public void setUSER_COMPANY_NAME(String USER_COMPANY_NAME) {
            this.USER_COMPANY_NAME = USER_COMPANY_NAME;
        }

        public String getDEVICE_ID() {
            return DEVICE_ID;
        }

        public void setDEVICE_ID(String DEVICE_ID) {
            this.DEVICE_ID = DEVICE_ID;
        }

        public String getUSER_ORG_CODE() {
            return USER_ORG_CODE;
        }

        public void setUSER_ORG_CODE(String USER_ORG_CODE) {
            this.USER_ORG_CODE = USER_ORG_CODE;
        }

        public String getUSER_DOMAIN() {
            return USER_DOMAIN;
        }

        public void setUSER_DOMAIN(String USER_DOMAIN) {
            this.USER_DOMAIN = USER_DOMAIN;
        }

        public int getORG_DEEP() {
            return ORG_DEEP;
        }

        public void setORG_DEEP(int ORG_DEEP) {
            this.ORG_DEEP = ORG_DEEP;
        }

        public int getUSER_STORE_ID() {
            return USER_STORE_ID;
        }

        public void setUSER_STORE_ID(int USER_STORE_ID) {
            this.USER_STORE_ID = USER_STORE_ID;
        }

        public String getUSER_IP() {
            return USER_IP;
        }

        public void setUSER_IP(String USER_IP) {
            this.USER_IP = USER_IP;
        }

        public int getUSER_COMPANY_ID() {
            return USER_COMPANY_ID;
        }

        public void setUSER_COMPANY_ID(int USER_COMPANY_ID) {
            this.USER_COMPANY_ID = USER_COMPANY_ID;
        }

        public String getUSER_NAME() {
            return USER_NAME;
        }

        public void setUSER_NAME(String USER_NAME) {
            this.USER_NAME = USER_NAME;
        }

        public String getUSER_ORG_NAME() {
            return USER_ORG_NAME;
        }

        public void setUSER_ORG_NAME(String USER_ORG_NAME) {
            this.USER_ORG_NAME = USER_ORG_NAME;
        }

        public int getUSER_ORG_LEVEL() {
            return USER_ORG_LEVEL;
        }

        public void setUSER_ORG_LEVEL(int USER_ORG_LEVEL) {
            this.USER_ORG_LEVEL = USER_ORG_LEVEL;
        }

        public List<Integer> getALL_STORES() {
            return ALL_STORES;
        }

        public void setALL_STORES(List<Integer> ALL_STORES) {
            this.ALL_STORES = ALL_STORES;
        }

        public List<Integer> getSUB_STORES() {
            return SUB_STORES;
        }

        public void setSUB_STORES(List<Integer> SUB_STORES) {
            this.SUB_STORES = SUB_STORES;
        }

        public List<Integer> getUSER_ROLE_IDS() {
            return USER_ROLE_IDS;
        }

        public void setUSER_ROLE_IDS(List<Integer> USER_ROLE_IDS) {
            this.USER_ROLE_IDS = USER_ROLE_IDS;
        }

        public static class IPTABLEBean implements Serializable {
            /**
             * companyId : 1
             * domain : mess.csosm.com
             * port : 62280
             */

            private int companyId;
            private String domain;
            private int port;
            private String baseUrl;

            public int getCompanyId() {
                return companyId;
            }

            public void setCompanyId(int companyId) {
                this.companyId = companyId;
            }

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public String getBaseUrl() {
                String url = TextUtils.isEmpty(baseUrl) ? "http://113.106.222.250:9001" : baseUrl;
                if (url.endsWith("/"))
                    return url.substring(0, url.lastIndexOf("/"));
                return url;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }
        }
    }
}
