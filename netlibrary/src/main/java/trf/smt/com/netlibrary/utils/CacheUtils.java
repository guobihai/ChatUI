package trf.smt.com.netlibrary.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trf.smt.com.netlibrary.enity.Contacts;

/**
 * Created by gbh on 2018/5/9  10:06.
 *
 * @describe 认领缓存计数器
 */

public class CacheUtils {
    private static List<Contacts> sContactsList = new ArrayList<>();
    private static Map<String, String> sMap = new HashMap<>();
    //不是微信好友，不缓存
    private static final String chartRoom = "@chartRoom";

    /**
     * 缓存当前被认领的wx用户
     *
     * @param key   wx usrename
     * @param value 消息未读数量
     */
    public static void putClaim(String key, String value) {
        if (TextUtils.isEmpty(key) || key.endsWith(chartRoom)) return;
        if (!sMap.containsKey(key))
            sMap.put(key, value);
        else {
            int count = Integer.parseInt(sMap.get(key)) + 1;
            sMap.put(key, String.valueOf(count));
        }
    }

    /**
     * 删除认领信息
     *
     * @param key
     */
    public static void removeClaim(String key) {
        sMap.remove(key);
    }

    /**
     * 增加数据未认领数据
     *
     * @param map wx map
     */
    public static void putAllClaim(Map<String, String> map) {
        sMap.putAll(map);
    }

    /**
     * 获取未认领消息总数
     *
     * @return
     */
    public static String getMsgCount() {
        int count = 0;
        for (Map.Entry<String, String> entry : sMap.entrySet()) {
            count += Integer.parseInt(entry.getValue());
        }
        return String.valueOf(count);
    }

    /**
     * 获取微信username 组合
     *
     * @return
     */
    public static String getGroupWxUserName() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : sMap.entrySet()) {
            stringBuffer.append(entry.getKey() + ",");
        }
        String str = stringBuffer.toString();
        if (str.lastIndexOf(",") > 0)
            str = str.substring(0, str.lastIndexOf(","));
        return str;
    }


    /**
     * 获取微信username 组合
     *
     * @return
     */
    public static String getGroupDbWxUserName() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : sMap.entrySet()) {
            stringBuffer.append("'" + entry.getKey() + "'" + ",");
        }
        //'555','45454'
        String str = stringBuffer.toString();
        if (str.lastIndexOf(",") > 0)
            str = str.substring(0, str.lastIndexOf(","));
        return str;
    }

    /**
     * 获取当前key 的消息总数
     *
     * @param key
     * @return
     */
    public static String getMsgCount(String key) {
        return TextUtils.isEmpty(sMap.get(key)) ? "0" : sMap.get(key);
    }

    /**
     * 判断当前的
     *
     * @param key         当前wx 用户ID
     * @param loginUserId 当前登录用户
     * @return true 有权限，false 无权限
     */
    public static boolean isClaimPermission(String key, String loginUserId) {
        return sMap.get(key).equals(loginUserId);
    }

    public static boolean isTrue() {
        return sMap.size() > 0 ? true : false;
    }


    /**
     * 增加通讯录缓存
     *
     * @param contactsList
     */
    public static void putAllContacts(List<Contacts> contactsList) {
        sContactsList.clear();
        sContactsList.addAll(contactsList);
    }

    /**
     * 获取通讯录缓存
     *
     * @return
     */
    public static List<Contacts> getContactsList() {
        return sContactsList;
    }
}
