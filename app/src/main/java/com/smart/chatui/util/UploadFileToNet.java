package com.smart.chatui.util;

import android.text.TextUtils;

import com.smart.chatui.enity.ResFile;
import com.smart.chatui.enity.SendFiles;
import com.smart.chatui.https.UpLoadFileUtils;
import com.smart.chatui.https.listener.impl.UIProgressListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.LogUtils;

import static com.smart.chatui.util.MsgType.SEND_TYPE_IMG;
import static com.smart.chatui.util.MsgType.SEND_TYPE_VIDEO;
import static com.smart.chatui.util.MsgType.SEND_TYPE_VOICE;

/**
 * Created by gbh on 2018/5/10  09:45.
 *
 * @describe 文件上传
 */

public class UploadFileToNet {
    private static final String IMAGE = "image";//图片
    private static final String VOICE = "audio";//语音
    private static final String VIDEO = "video";//视频
    private static final String TEXT = "text";//文件

    /**
     * 文件上传
     *
     * @param filePath
     * @param devicesId
     * @param type
     */
    public static void uploadFile(String baseUrl, String filePath, String devicesId, final int type) {
//        final String baseUrl = "http://" + url + ":" + port + "/";
        String url = baseUrl + "/upload/api/single/attachment";
//        String url = "http://192.168.5.251:80" + "/upload/api/single/attachment";
        String formTag = "";
        if (TextUtils.isEmpty(filePath)) return;
        switch (type) {
            case SEND_TYPE_IMG:
                formTag = IMAGE;
                break;
            case SEND_TYPE_VOICE:
                formTag = VOICE;
                break;
            case SEND_TYPE_VIDEO:
                formTag = VIDEO;
                break;
            default:
                formTag = TEXT;
                break;
        }
        Map<String, String> formData = new HashMap<>();
        formData.put("imei", devicesId);
        formData.put("fileType", formTag);
        formData.put("channel", "weixin");
        if (type == SEND_TYPE_VOICE) {
            Map<String, String> bean = new HashMap();
            bean.put("decoder", "Mp3ToAmr");
            formData.put("handle", JsonUtils.serialize(bean));
        }
        UpLoadFileUtils.upLoadFileToNet(url, filePath, formData, new UIProgressListener() {
            @Override
            public void onUIProgress(long currentBytes, long contentLength, boolean done) {

            }

            @Override
            public void onResultMsg(String msg) {
                super.onResultMsg(msg);
                LogUtils.sysout("upload file " + msg);
                if (!TextUtils.isEmpty(msg)) {
                    ResFile resFile = JsonUtils.deserialize(msg, ResFile.class);
                    if (resFile.isSuccess()) {
                        if (null == resFile.getData() || resFile.getData().size() == 0) {
                            SendFiles messageInfo = new SendFiles();
                            messageInfo.setFileUrl("");
                            messageInfo.setMsgType(type);
                            messageInfo.setSuccess(false);
                            EventBus.getDefault().post(messageInfo);
                            return;
                        }
                        ResFile.DataBean dataBean = resFile.getData().get(0);
                        SendFiles messageInfo = new SendFiles();
                        messageInfo.setFileUrl(dataBean.getServerUrl() + dataBean.getFileUrl());
                        messageInfo.setMsgType(type);
                        messageInfo.setSuccess(true);
                        EventBus.getDefault().post(messageInfo);
                    } else {
                        SendFiles messageInfo = new SendFiles();
                        messageInfo.setFileUrl("");
                        messageInfo.setMsgType(type);
                        messageInfo.setSuccess(false);
                        EventBus.getDefault().post(messageInfo);
                    }
                }
            }
        });
    }
}
