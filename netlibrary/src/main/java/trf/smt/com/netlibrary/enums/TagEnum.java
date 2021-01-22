package trf.smt.com.netlibrary.enums;

/**
 * Created by gbh on 2018/4/8  15:10.
 *
 * @describe
 */

public enum TagEnum {
    LOGIN("login", "登陆"), LOGOUT("logout", "登出"), HEART("heart", "心跳包"), MSG("msg", "消息"),
    CONTACT("contact", "通讯录"), SINGLE_CONTACT("single_contact", "单个通讯录"), ACK("ack", "应答"), ACKMSG("ackmsg", "消息应答"),
    SETTING("setting", "设置参数"), ERROR("error", "错误日志"), WXSTATE("wxstate", "微信状态"), CLAIM("claim", "导购认领"), UNCLAIM("un_claim", "导购取消认领"), UNCLAIM_MSG("unclaim_msg", "未认领会员信息"), FRIENDS_VERB("friends_verb", "发朋友圈");

    private String tag;
    private String desc;

    TagEnum() {
    }

    TagEnum(String tag, String desc) {
        this.tag = tag;
        this.desc = desc;
    }

    public String getTag() {
        return tag;
    }

    public String getDesc() {
        return desc;
    }
}
