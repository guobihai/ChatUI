package com.smart.chatui.util;

import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.IOException;

import trf.smt.com.netlibrary.utils.LogUtils;

/**
 * Created by gbh on 2018/6/6  10:48.
 *
 * @describe
 */

public class MediaUtils {
    /**
     * 获取音频时长
     *
     * @param url
     * @return
     */
    public static int getMediaDuration(String url) {
        if (TextUtils.isEmpty(url) || !url.endsWith(".mp3")) return 0;
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            int duration = mediaPlayer.getDuration();
            LogUtils.sysout("=====duration======"+duration);
            mediaPlayer.release();
            return duration ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
