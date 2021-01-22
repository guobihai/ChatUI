package com.smart.chatui.ui.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.smart.chatui.base.MyApplicationLike;
import com.smart.chatui.enity.ResBean;
import com.smart.chatui.https.OkHttpUtils;

import java.lang.reflect.Type;
import java.util.List;

import trf.smt.com.netlibrary.enity.Person;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;


/**
 * Created by gbh on 2018/5/16  09:32.
 *
 * @describe 加载联系人
 */

public class LoadDataServices extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        loadData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadData();
        return super.onStartCommand(intent, flags, startId);
    }

    private void loadData() {
        String shopId = PreferenceUtils.getString(this, "shopId", "");
        String url = PreferenceUtils.getString(this, "BASEURL", "")
                + "/api/webchat/" + shopId + "/contacts.json";
        OkHttpUtils.get(url, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
//                LogUtils.sysout(response);
                try {
                    Type type = new TypeToken<ResBean<List<Person>>>() {

                    }.getType();
                    ResBean<List<Person>> listResBean = JsonUtils.deserialize(response, type);
                    if (!listResBean.isSuccess() || null == listResBean.getData())
                        return;
                    if (listResBean.getData().size() == 0) return;
                    LogUtils.sysout("res contact:" + listResBean.getData().size());
                    if (null != MyApplicationLike.getDaoSession()) {
                        MyApplicationLike.getDaoSession().getPersonDao().deleteAll();
                        MyApplicationLike.getDaoSession().getPersonDao().insertOrReplaceInTx(listResBean.getData());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
