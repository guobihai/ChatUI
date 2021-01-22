package trf.smt.com.netlibrary.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gbh on 2018/5/10  20:09.
 *
 * @describe
 */

public class UnCliamMsg {

    private List<ListuserinfoBean> listuserinfo;

    public List<ListuserinfoBean> getListuserinfo() {
        return listuserinfo;
    }

    public void setListuserinfo(List<ListuserinfoBean> listuserinfo) {
        this.listuserinfo = listuserinfo;
    }

    /**
     * 转为为map 集合，key:wx --username value:count
     *
     * @return
     */
    public Map<String, String> getListUserMapInfo() {
        Map<String, String> map = new HashMap<>();
        if (null == listuserinfo || listuserinfo.size() == 0) return map;
        for (ListuserinfoBean bean : listuserinfo) {
            map.put(bean.getUserName(), String.valueOf(bean.getCount()));
        }
        return map;
    }

    /**
     * 获取消息总数
     *
     * @return
     */
    public String getMsgCount() {
        if (null == listuserinfo || listuserinfo.size() == 0) return "0";
        int count = 0;
        for (ListuserinfoBean bean : listuserinfo) {
            count += bean.getCount();
        }
        return String.valueOf(count);
    }

    public static class ListuserinfoBean implements Serializable {
        /**
         * userName : wxid_8sylpdoul2fa22
         * count : 1
         */

        private String userName;
        private int count;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
