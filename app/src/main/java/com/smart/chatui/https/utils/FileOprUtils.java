package com.smart.chatui.https.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by gbh on 17/5/9  下午2:38.
 *
 * @describe 文件操作类
 */

public class FileOprUtils {
    /**
     * 创建文件
     *
     * @param url
     * @return
     * @throws IOException
     */
    @NonNull
    public static File getCreateFile(String url, String floder) throws IOException {
        String fileName = url.substring(url.lastIndexOf("/"), url.length());
        String path = getFolderPath(floder) + fileName;
        File file = new File(path);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        int index = 0;
        while (file.exists()) {
            index++;
            String endName = url.substring(url.lastIndexOf("."), url.length());
            fileName = url.substring(url.lastIndexOf("/"), url.lastIndexOf("."));
            path = getFolderPath(floder) + fileName + "(" + index + ")" + endName;
            file = new File(path);
        }
        file.createNewFile();
        return file;
    }

    /**
     * 获取历史下载的文件
     *
     * @param url
     * @return
     * @throws IOException
     */
    @NonNull
    public static File getHisFile(String url, String floder) throws IOException {
        String fileName = url.substring(url.lastIndexOf("/"), url.length());
        String path = getFolderPath(floder) + fileName;
        File file = new File(path);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        return file;
    }

    /**
     * 创建路径
     *
     * @return
     */
    protected static String getFolderPath(String floder) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile", "SD card is not avaiable/writeable right now.");
            throw new IllegalStateException("SD card is not avaiable/writeable right now");
        }
        //本业务文件主目录
        StringBuilder pathBuilder = new StringBuilder();
        if (TextUtils.isEmpty(floder))
            floder = "/" + "Download" + "/file/";
        else {
            if (!floder.startsWith("/")) floder = "/" + floder;
            if (!floder.endsWith("/")) floder = floder + "/";
        }
        pathBuilder.append(Environment.getExternalStorageDirectory() + floder);
        return pathBuilder.toString();
    }


    /**
     * 获取已有文件长度
     *
     * @param url
     * @return
     */
    public static long getFileLength(String url, String floder) throws IOException {
        String fileName = url.substring(url.lastIndexOf("/"), url.length());
        String path = getFolderPath(floder) + fileName;
        File file = new File(path);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (file.exists())
            return file.length();
        return 0;
    }

}
