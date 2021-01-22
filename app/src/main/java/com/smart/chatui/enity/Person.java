//package com.smart.chatui.enity;
//
//import android.text.TextUtils;
//
//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Generated;
//import org.greenrobot.greendao.annotation.Id;
//import org.greenrobot.greendao.annotation.Keep;
//
//import java.io.Serializable;
//
///**
// * Created by gbh on 2018/4/9  13:52.
// *
// * @describe
// */
//
//@Entity
//public class Person implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    @Id(autoincrement = true)
//    private Long Id;
//    @Keep
//    private String userName;//微信ID
//    @Keep
//    private String nickName;//昵称
//    @Keep
//    private String conRemark;//备注
//    @Keep
//    private String iconUrl;//图片URL
//    @Keep
//    private int type;//类型
//
//    @Keep
//    private String letters;
//
//
//    @Generated(hash = 1881915470)
//    public Person(Long Id, String userName, String nickName, String conRemark,
//                  String iconUrl, int type, String letters) {
//        this.Id = Id;
//        this.userName = userName;
//        this.nickName = nickName;
//        this.conRemark = conRemark;
//        this.iconUrl = iconUrl;
//        this.type = type;
//        this.letters = letters;
//    }
//
//    @Generated(hash = 1024547259)
//    public Person() {
//    }
//
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public void setNickName(String nickName) {
//        this.nickName = nickName;
//    }
//
//    public void setConRemark(String conRemark) {
//        this.conRemark = conRemark;
//    }
//
//    public void setIconUrl(String iconUrl) {
//        this.iconUrl = iconUrl;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public String getNickName() {
//        return nickName;
//    }
//
//    public String getConRemark() {
//        return TextUtils.isEmpty(conRemark) ? getNickName() : conRemark;
//    }
//
//    public String getIconUrl() {
//        return iconUrl;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    /**
//     * 判断是否微信用户
//     *
//     * @return
//     */
//    public boolean isWxUser() {
//        return type != -1;
//    }
//
//    @Override
//    public String toString() {
//        return "Person{" +
//                "userName='" + userName + '\'' +
//                ", nickName='" + nickName + '\'' +
//                ", conRemark='" + conRemark + '\'' +
//                ", iconUrl='" + iconUrl + '\'' +
//                ", type=" + type +
//                '}';
//    }
//
//    public Long getId() {
//        return this.Id;
//    }
//
//    public void setId(Long Id) {
//        this.Id = Id;
//    }
//
//    public String getUserName() {
//        return this.userName;
//    }
//
//    public String getLetters() {
//        return letters;
//    }
//
//    public void setLetters(String letters) {
//        this.letters = letters;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Person person = (Person) o;
//
//        return userName != null ? userName.equals(person.userName) : person.userName == null;
//    }
//
//    @Override
//    public int hashCode() {
//        return userName.hashCode();
//    }
//}
