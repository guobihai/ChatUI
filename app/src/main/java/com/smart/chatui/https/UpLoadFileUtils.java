package com.smart.chatui.https;


import com.smart.chatui.https.helper.ProgressHelper;
import com.smart.chatui.https.listener.impl.UIProgressListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gbh on 17/5/5  上午11:04.
 *
 * @describe 文件上传
 */

public class UpLoadFileUtils {
    private static UpLoadFileUtils mInstance;
    private OkHttpUtils mOkHttpUtils;

    UpLoadFileUtils() {
        mOkHttpUtils = OkHttpUtils.getInstance();
    }

    /**
     * 创建一个单例
     *
     * @return
     */
    private static UpLoadFileUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (null == mInstance)
                    mInstance = new UpLoadFileUtils();
            }
        }
        return mInstance;
    }

    /**
     * 上传文件
     *
     * @param url      服务端上传路径
     * @param filePath 本地文件路径
     * @param formData 表单数据
     */
    public static void upLoadFileToNet(String url, String filePath, Map<String, String> formData, UIProgressListener uiProgressRequestListener) {
        getInstance().upLoadFile(url, filePath, formData, uiProgressRequestListener);
    }

    /**
     * 上传文件
     *
     * @param url      服务端上传路径
     * @param filePath 本地文件路径
     * @param formData 表单数据
     */
    private void upLoadFile(String url, String filePath, Map<String, String> formData, UIProgressListener uiProgressRequestListener) {
        Request request = buildPostFileRequest(url, filePath, formData, uiProgressRequestListener);
        upLoadFileCallResult(request, uiProgressRequestListener);
    }

    /**
     * 上传文件
     *
     * @param url      服务端上传路径
     * @param filePath 本地文件路径
     * @param formData 表单数据
     * @return
     */
    private Request buildPostFileRequest(String url, String filePath, Map<String, String> formData, UIProgressListener uiProgressRequestListener){
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                throw new FileNotFoundException("文件找不到");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (Map.Entry<String, String> entry : formData.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        //构造上传请求，类似web表单
        builder.addFormDataPart("file", file.getName(), fileBody);
        //进行包装，使其支持进度回调
        return new Request.Builder().url(url).post(ProgressHelper.addProgressRequestListener(builder.build(), uiProgressRequestListener)).build();
    }


    /**
     * 上传文件返回信息
     *
     * @param request
     * @param uiProgressRequestListener
     */
    private void upLoadFileCallResult(Request request, final UIProgressListener uiProgressRequestListener) {
        mOkHttpUtils.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null != uiProgressRequestListener)
                    uiProgressRequestListener.onErrorMsg(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != uiProgressRequestListener)
                    uiProgressRequestListener.onResultMsg(response.body().string());
            }
        });

    }


}
