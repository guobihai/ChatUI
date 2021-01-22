package trf.smt.com.netlibrary.bean;

import android.text.TextUtils;

import trf.smt.com.netlibrary.utils.JsonUtils;


/**
 * Created by gbh on 2018/5/26  17:45.
 *
 * @describe 发送朋友圈信息
 */

public class FriendsInfo {
    private Long id;
    private String content;
    private String fileUrl;

    public FriendsInfo() {
    }

    public FriendsInfo(String content, String fileUrl) {
        this.id = 1111l;
        this.content = content;
        this.fileUrl = fileUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return TextUtils.isEmpty(content) ? "hello" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }
}
