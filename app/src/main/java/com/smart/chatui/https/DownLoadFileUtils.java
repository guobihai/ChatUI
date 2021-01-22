package com.smart.chatui.https;

import android.support.annotation.NonNull;

import com.smart.chatui.https.helper.ProgressHelper;
import com.smart.chatui.https.listener.impl.UIProgressListener;
import com.smart.chatui.https.utils.FileOprUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import trf.smt.com.netlibrary.utils.LogUtils;

/**
 * Created by gbh on 17/5/5  下午3:39.
 *
 * @describe 单文件下载
 */

public class DownLoadFileUtils {
    static DownLoadFileUtils mInstance;
    private OkHttpUtils mOkHttpUtils;
    private String floder;//
    private Map<String, Call> mCallMap;

    public DownLoadFileUtils() {
        mOkHttpUtils = OkHttpUtils.getInstance();
        mCallMap = new HashMap<>();
    }

    /**
     * 单例
     *
     * @return
     */
    protected static DownLoadFileUtils getInstance() {
        if (null == mInstance) {
            synchronized (DownLoadFileUtils.class) {
                if (null == mInstance)
                    mInstance = new DownLoadFileUtils();
            }
        }
        return mInstance;
    }

    /**
     * 设置下载文件目录
     *
     * @param path
     */
    public void setDownLoadFloder(@NonNull String path) {
        floder = path;
    }

    /**
     * 下载
     *
     * @param url                       下载路径
     * @param uiProgressRequestListener 下载进度
     */
    public static void startDownLoadFile(final String url, final UIProgressListener uiProgressRequestListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getInstance().buildDownLoadFileRequest(url, null, uiProgressRequestListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 下载
     *
     * @param url                       下载路径
     * @param floder                    存储文件目录
     * @param uiProgressRequestListener 下载进度
     */
    public static void startDownLoadFile(final String url, final String floder, final UIProgressListener uiProgressRequestListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getInstance().buildDownLoadFileRequest(url, floder, uiProgressRequestListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 停止下载
     *
     * @param url
     */
    public static void stopDownLoad(String url) {
        getInstance().cancle(url);
    }

    /**
     * 取消下载
     *
     * @param url
     */
    protected void cancle(String url) {
        Call call = mCallMap.get(url);
        if (null == call) return;
        if (call.isCanceled()) return;
        call.cancel();
    }

    /**
     * 构建下载请求
     *
     * @param url
     * @param uiProgressRequestListener
     */
    protected void buildDownLoadFileRequest(final String url, String floder, final UIProgressListener uiProgressRequestListener) throws Exception {
        if (mCallMap.get(url) != null) return;//判断是否重新下载
        setDownLoadFloder(floder);
        final long downloadLength = FileOprUtils.getFileLength(url, floder);//已经下载好的长度
        final long contentLength = getContentLength(url);//文件的总长度
        long tempDownloadLength = downloadLength == contentLength ? 0 : downloadLength;
        LogUtils.sysout("====tempDownloadLength===", tempDownloadLength);
        if (downloadLength != contentLength)
            uiProgressRequestListener.onProgress(downloadLength, contentLength, false);
        //构造请求
        final Request request = new Request.Builder().addHeader("Range", "bytes=" + tempDownloadLength + "-" + contentLength).
                url(url).build();
        //包装Response使其支持进度回调
        OkHttpClient okHttpClient = ProgressHelper.addProgressResponseListener(mOkHttpUtils.getOkHttpClient(), uiProgressRequestListener);
        Call call = okHttpClient.newCall(request);
        mCallMap.put(url, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null != uiProgressRequestListener)
                    uiProgressRequestListener.onResultMsg(e.toString());
                mCallMap.remove(url);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                if (null != uiProgressRequestListener)
                    uiProgressRequestListener.onResultMsg(saveFile(url, inputStream, downloadLength, contentLength));
                else
                    saveFile(url, inputStream, downloadLength, contentLength);
            }
        });
    }


    /**
     * 获取下载长度
     *
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl) {
        Request request = new Request.Builder().url(downloadUrl).build();
        try {
            Response response = mOkHttpUtils.getOkHttpClient().newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
                return contentLength == 0 ? -1 : contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据文件类型存储文件
     *
     * @param url
     */
    private String saveFile(String url, InputStream inputStream, long downloadLength, long contentLength) {
        OutputStream output = null;
        File file = null;
        try {
            if (downloadLength == contentLength)
                file = FileOprUtils.getCreateFile(url, floder);
            else
                file = FileOprUtils.getHisFile(url, floder);

            output = new FileOutputStream(file, true);
            //读取大文件
            byte[] buff = new byte[1024];
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 1024)) > 0) {
                output.write(buff, 0, rc);
            }
            output.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != output)
                    output.close();
                mCallMap.remove(url);
                System.out.println("success");
            } catch (IOException e) {
                System.out.println("fail");
                e.printStackTrace();
            }
        }
        return file == null ? "" : file.getAbsolutePath();
    }


}
