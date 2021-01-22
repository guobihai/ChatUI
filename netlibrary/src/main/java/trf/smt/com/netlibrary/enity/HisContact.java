package trf.smt.com.netlibrary.enity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gbh on 2018/4/9  13:52.
 *
 * @describe 最近聊天记录
 */

@Entity
public class HisContact implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id(autoincrement = true)
    private Long Id;
    @Keep
    private String userName;//用户名
    @Keep
    private String nickName;//昵称
    @Keep
    private String conRemark;//备注
    @Keep
    private String iconUrl;//头像路径
    @Keep
    private int type;//消息类型

    @Keep
    private String lastMsg;//最后的消息

    @Keep
    private String createTime;//时间

    @Keep
    private String loginUserId;//当前登录ID

    @Keep
    private int orderCode;//排序

    @Keep
    private int msgCount;//未读消息

    @Keep
    private int memberId;//会员ID

    @Keep
    private int code;//备用

    @Keep
    private String codeName;//备用


    @Generated(hash = 2146897022)
    public HisContact(Long Id, String userName, String nickName, String conRemark,
                      String iconUrl, int type, String lastMsg, String createTime, String loginUserId,
                      int orderCode, int msgCount,int memberId,int code,String codeName) {
        this.Id = Id;
        this.userName = userName;
        this.nickName = nickName;
        this.conRemark = conRemark;
        this.iconUrl = iconUrl;
        this.type = type;
        this.lastMsg = lastMsg;
        this.createTime = createTime;
        this.loginUserId = loginUserId;
        this.orderCode = orderCode;
        this.msgCount = msgCount;
        this.memberId =memberId;
        this.code = code;
        this.codeName=codeName;
    }

    @Generated(hash = 1566279858)
    public HisContact() {
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setConRemark(String conRemark) {
        this.conRemark = conRemark;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickName() {
        return TextUtils.isEmpty(nickName) ? "" : nickName;
    }

    public String getConRemark() {
        return TextUtils.isEmpty(conRemark) ? getNickName() : conRemark;
    }

    public String getIconUrl() {
        return TextUtils.isEmpty(iconUrl) ? "" : iconUrl;
    }

    public int getType() {
        return type;
    }


    @Override
    public String toString() {
        return "HisContact{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", conRemark='" + conRemark + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", type=" + type +
                '}';
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getUserName() {
        return this.userName;
    }

    public static HisContact getDefaultChartRoom(String loginUserId) {
        HisContact hisContact = new HisContact();
        hisContact.setConRemark("门店聊天室");
        hisContact.setIconUrl("");
        hisContact.setNickName("门店聊天室");
        hisContact.setType(-1);
        hisContact.setUserName("@chartRoom");
        hisContact.setCreateTime(System.currentTimeMillis() + "");
        hisContact.setLoginUserId(loginUserId);
        hisContact.setOrderCode(0);
        return hisContact;
    }

    public static HisContact personToHisContact(Person person, String loginUserId) {
        HisContact hisContact = new HisContact();
        hisContact.setConRemark(person.getConRemark());
        hisContact.setIconUrl(person.getIconUrl());
        hisContact.setNickName(person.getNickName());
        hisContact.setType(1);
        hisContact.setUserName(person.getUserName());
        hisContact.setCreateTime(getTime());
        hisContact.setLoginUserId(loginUserId);
        hisContact.setOrderCode(1);
        return hisContact;
    }

    public static Person hisContactToPerson(HisContact person) {
        Person hisContact = new Person();
        hisContact.setConRemark(person.getConRemark());
        hisContact.setIconUrl(person.getIconUrl());
        hisContact.setNickName(person.getNickName());
        hisContact.setType(person.getType());
        hisContact.setUserName(person.getUserName());
        return hisContact;
    }

    public String getLastMsg() {
        return TextUtils.isEmpty(this.lastMsg) ? "" : this.lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getCreateTime() {
        return TextUtils.isEmpty(this.createTime) ? "" : this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public String getLoginUserId() {
        return this.loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public int getOrderCode() {
        return this.orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    public int getMsgCount() {
        return this.msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int menberId) {
        this.memberId = menberId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
