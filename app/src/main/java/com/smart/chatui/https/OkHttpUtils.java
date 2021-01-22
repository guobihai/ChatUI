package com.smart.chatui.https;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;
import com.smart.chatui.https.ssl.TrustAllCerts;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * guobihai
 * OkHttp 网络请求封装类
 * Created by gbh on 16/6/25.
 * application/xhtml+xml ：XHTML格式
 * application/xml ： XML数据格式
 * application/atom+xml ：Atom XML聚合格式
 * application/json ： JSON数据格式
 * application/pdf ：pdf格式
 * application/msword ： Word文档格式
 * application/octet-stream ： 二进制流数据（如常见的文件下载）
 * application/x-www-form-urlencoded ： <form encType=””>中默认的encType
 * multipart/form-data
 */
public class OkHttpUtils {
    private static String TAG = "OkHttpUtils";

    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private final int SECONDS = 30;

    private String sessionId = "";
    private Map<String, Call> mCallMap;

    private Map<String, List<Cookie>> mapCookie;


    protected OkHttpUtils() {
        try {
            mapCookie = new HashMap<>();
            mOkHttpClient = new OkHttpClient.Builder()
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
                            if (cookies != null) {
                                for (Cookie cookie : cookies) {
                                    System.out.println("==cookies=name=" + cookie.name());
                                    System.out.println("==cookies=value=" + cookie.value());
                                }
                            }
                            mapCookie.put(httpUrl.host(), cookies);
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
//                            List<Cookie> cookies = cookieStore.get(httpUrl);-
                            return mapCookie.get(url.host()) != null ? mapCookie.get(url.host()) : new ArrayList<Cookie>();
                        }
                    })
                    .connectTimeout(SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(SECONDS, TimeUnit.SECONDS)
                    .sslSocketFactory(createSSLSocketFactory())
                    .cache(null)
                    .readTimeout(SECONDS, TimeUnit.SECONDS).build();
            mDelivery = new Handler(Looper.getMainLooper());
            mCallMap = new HashMap<>();
//            if (null != SmtApplication.getSmtApplication()) {
//                int cacheSize = 10 * 1024 * 1024; // 10 MiB
//                File cacheDirectory = new File(SmtApplication.getSmtApplication().getCacheDir(), "OkHttpCache");
//                Cache cache = new Cache(cacheDirectory, cacheSize);
//                mOkHttpClient.cache();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    protected OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 创建一个单例
     *
     * @return
     */
    protected static OkHttpUtils getInstance() {
//        if (mInstance == null) {
//            synchronized (OkHttpUtils.class) {
//                if (null == mInstance)
        mInstance = new OkHttpUtils();
//            }
//        }
        return mInstance;
    }


    /**
     * get请求
     *
     * @param url      请求url
     * @param callback 请求回调
     */
    public static void get(String url, ResultCallBack callback) {
        getInstance().getRequest(url, callback);
    }

    /**
     * post请求
     *
     * @param url      请求url
     * @param params   请求参数
     * @param callback 请求回调
     */
    public static void post(String url, List<Param> params, final ResultCallBack callback) {
        getInstance().postRequest(url, params, callback);
    }

    /**
     * Post 请求
     *
     * @param url
     * @param body     数据体
     * @param callback
     */
    public static void post(String url, String body, final ResultCallBack callback) {
        getInstance().postRequest(url, body, callback);
    }

    /**
     * Post 请求,返回数据流
     *
     * @param url
     * @param body     数据体
     * @param callback
     */
    public static void postResultStream(String url, String body, final ResultCallBack callback) {
        getInstance().postRequestStream(url, body, callback);
    }

    /**
     * 取消请求
     *
     * @param url
     */
    public static void cancle(String url) {
        Call call = getInstance().mCallMap.get(url);
        if (null == call) return;
        if (call.isCanceled()) return;
        call.cancel();
    }

    /**
     * get 请求
     *
     * @param url
     * @param callBack
     */
    private void getRequest(String url, final ResultCallBack callBack) {
//        httpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("Content-Type", "application/json");
//        httpPost.setHeader("charset","utf-8");
//        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Accept-Language","en-US,en;q=0.8");
//        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        final Request request = new Request.Builder()
                .addHeader("charset", "utf-8")
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Language", "en-US,en;q=0.8")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .url(url).build();
        deliverResult(url, callBack, request);
    }

    /**
     * post 请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    private void postRequest(String url, List<Param> params, final ResultCallBack callBack) {
        Request request = buildPostRequest(url, params);
        deliverResult(url, callBack, request);
    }

    /**
     * post 请求
     *
     * @param url
     * @param body
     * @param callBack
     */
    private void postRequest(String url, String body, final ResultCallBack callBack) {
        deliverResult(url, callBack, buildPostRequest(url, body));
    }

    /**
     * post 请求
     *
     * @param url
     * @param body
     * @param callBack
     */
    private void postRequestStream(String url, String body, final ResultCallBack callBack) {
        deliverResultStream(url, callBack, buildPostWebRequest(url, body));
    }

    /**
     * 任务请求
     *
     * @param callBack
     * @param request
     */
    private void deliverResult(String url, final ResultCallBack callBack, Request request) {
        Call call = mOkHttpClient.newCall(request);
        mCallMap.put(url, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    sendSuccessCallBack(callBack, response.body().string());
                } catch (final Exception e) {
                    e.printStackTrace();
                    sendFailCallback(callBack, e);
                }
            }
        });
    }


    /**
     * webservice 服务请求
     *
     * @param callBack inputStream
     * @param request
     */
    private void deliverResultStream(String url, final ResultCallBack callBack, Request request) {
        Call call = mOkHttpClient.newCall(request);
        mCallMap.put(url, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callBack, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    sendSuccessCallBack(callBack, response.body().byteStream());
                } catch (final Exception e) {
                    e.printStackTrace();
                    sendFailCallback(callBack, e);
                }

            }
        });
    }


    /**
     * 创建Post request 参数对象
     *
     * @param url
     * @param params
     * @return
     */
    private Request buildPostRequest(String url, List<Param> params) {
//        FormBody.Builder formBody = new FormBody.Builder();
//        for (Param p : params) {
//            formBody.add(p.key, p.value);
//        }
//        FormBody body = formBody.build();

        MediaType FORM_CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        StringBuffer sb = new StringBuffer();
        //设置表单参数
        for (int i = 0; i < params.size(); i++) {
            Param p = params.get(i);
            if (i < params.size() - 1)
                sb.append(p.key + "=" + p.value + "&");
            else
                sb.append(p.key + "=" + p.value);
        }

        RequestBody body = RequestBody.create(FORM_CONTENT_TYPE, sb.toString());

        return new Request.Builder().url(url)

                .addHeader("Connection", "close")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.0.2; SM-A5000 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Crosswalk/23.53.589.4 Mobile Safari/537.36")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Origin", "file://")
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
//                .addHeader("Accept-Language", "zh-cn")
//                .addHeader("Accept-Encoding", "gzip,deflate")
                .post(body).build();

    }


    /**
     * @param url
     * @param body
     * @return
     */
    private Request buildPostRequest(String url, String body) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, body);
        return new Request.Builder()
                .addHeader("Cookie", sessionId)
                .post(requestBody).url(url).build();

    }

    /**
     * 提交webserver 数据体
     *
     * @param url
     * @param body
     * @return
     */
    private Request buildPostWebRequest(String url, String body) {
        MediaType xml = MediaType.parse("application/soap+xml; charset=utf-8");
        RequestBody requestBody = RequestBody.create(xml, body);
        return new Request.Builder().addHeader("Cookie", sessionId)
                .post(requestBody).url(url).build();
    }


    /**
     * 失败回调
     *
     * @param callback
     * @param e
     */
    protected void sendFailCallback(final ResultCallBack callback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    /**
     * 成功回调
     *
     * @param callback
     * @param obj
     */
    protected void sendSuccessCallBack(final ResultCallBack callback, final Object obj) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(obj);
                }
            }
        });
    }

    /**
     * http请求回调类,回调方法在UI线程中执行
     *
     * @param <T>
     */
    public static abstract class ResultCallBack<T> {
        Type mType;

        public ResultCallBack() {
        }

        static Type getSupperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getSuperclass();
            if (subclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功回调
         *
         * @param response
         */
        public abstract void onSuccess(T response);

        /**
         * 请求失败回调
         *
         * @param e
         */
        public abstract void onFailure(Exception e);

    }


    /**
     * post请求参数类
     */
    public static class Param {

        String key;
        Object value;

        public Param(String key, Object value) {
            this.key = key;
            this.value = value;
        }

    }


}
