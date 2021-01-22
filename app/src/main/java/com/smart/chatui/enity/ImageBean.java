package com.smart.chatui.enity;

/**
 * Created by gbh on 17/4/18.
 * 拍照图片存储类
 */

public class ImageBean {

    public static final int TAKEPHOTO = 1;//拍照相机
    public static final int PHOTO = 2;//图片

    private String id;//图片上传后返回的ID
    private String path;//本地路径
    private int type; //类型
    private int imageId;//图片本地资源ID
    private int fromType;//图片来源
    private boolean isUpload;//是否已上传

    public ImageBean() {
        
    }

    public ImageBean(int type, String path, int fromType) {
        this.type = type;
        this.path = path;
        this.fromType = fromType;
    }

    public ImageBean(int type, int imageId) {
        this.type = type;
        this.imageId = imageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }
}
