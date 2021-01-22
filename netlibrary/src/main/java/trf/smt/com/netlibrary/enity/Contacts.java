package trf.smt.com.netlibrary.enity;


import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by gbh on 2018/5/25  16:15.
 *
 * @describe 通讯录
 */

public class Contacts implements Serializable {
    private boolean isSelect;

    private String id;
    private String weixinId;
    private String nickName;
    private String iconUrl;
    private String userId;
    private String userName;
    private String label;
    private String memberId;
    private String memberName;
    private int type;
    private String conRemark;

    private String letters;//排序

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getConRemark() {
        return conRemark;
    }

    public void setConRemark(String conRemark) {
        this.conRemark = conRemark;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMemberId() {
        return TextUtils.isEmpty(memberId)?"-1":memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }


    public Person convertToPerson(Contacts contacts){
        Person person = new Person();
        person.setConRemark(contacts.getConRemark());
        person.setIconUrl(contacts.getIconUrl());
        person.setType(contacts.getType());
        person.setMemberId(Integer.parseInt(contacts.getMemberId()));
        person.setUserName(contacts.getUserName());
        person.setNickName(contacts.getNickName());
        return person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contacts person = (Contacts) o;

        return getUserName() != null ? getUserName().equals(person.getUserName()) : person.getUserName() == null;
    }

    @Override
    public int hashCode() {
        return getUserName().hashCode();
    }

}
