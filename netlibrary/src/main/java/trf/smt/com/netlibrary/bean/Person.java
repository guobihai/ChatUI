package trf.smt.com.netlibrary.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by gbh on 2018/4/9  13:52.
 *
 * @describe
 */

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long Id;
    private String userName;//微信ID
    private String nickName;//昵称
    private String conRemark;//备注
    private String iconUrl;//图片URL
    private int type;//类型

    private String letters;


    public Person(Long Id, String userName, String nickName, String conRemark,
                  String iconUrl, int type, String letters) {
        this.Id = Id;
        this.userName = userName;
        this.nickName = nickName;
        this.conRemark = conRemark;
        this.iconUrl = iconUrl;
        this.type = type;
        this.letters = letters;
    }

    public Person() {
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
        return nickName;
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

    /**
     * 判断是否微信用户
     *
     * @return
     */
    public boolean isWxUser() {
        return type != -1;
    }

    @Override
    public String toString() {
        return "Person{" +
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

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return userName != null ? userName.equals(person.userName) : person.userName == null;
    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }
}
