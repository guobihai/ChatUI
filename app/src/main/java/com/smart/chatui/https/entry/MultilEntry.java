package com.smart.chatui.https.entry;

import android.text.TextUtils;

import java.io.File;

/**
 * Created by gbh on 17/5/8  下午5:18.
 *
 * @describe 文件处理类
 */

public class MultilEntry {
    private String path;//路径
    private String fileName;//文件名
    private long contentLenght;//总长度
    private long progress;//当前进度
    private boolean isDone;//是否完成

    public MultilEntry(String path) {
        this.path = path;
        if (!TextUtils.isEmpty(path)) {
            fileName = path.substring(path.lastIndexOf(path), path.length());
            if (!path.startsWith("http")) {
                File file = new File(path);
                if (file.isFile()) {
                    contentLenght = file.length();
                }
            }
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        if (!TextUtils.isEmpty(path)) {
            fileName = path.substring(path.lastIndexOf(path), path.length());
            if (!path.startsWith("http")) {
                File file = new File(path);
                if (file.isFile()) {
                    contentLenght = file.length();
                }
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getContentLenght() {
        return contentLenght;
    }

    public void setContentLenght(long contentLenght) {
        this.contentLenght = contentLenght;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
