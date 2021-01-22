package com.smart.chatui.base;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.smart.chatui.R;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.List;

import trf.smt.com.netlibrary.base.DbAppliaction;
import trf.smt.com.netlibrary.greendao.DaoSession;
import trf.smt.com.netlibrary.utils.PreferenceUtils;

/**
 */
public class MyApplicationLike extends Application {
    private static MyApplicationLike mInstance;
    public static Context mContext;
    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;

    private static trf.smt.com.netlibrary.greendao.DaoSession sDaoSession;

    private static SoundPool pool;
    private static List<Object> listPool = new ArrayList<Object>();

//    public MyApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
//        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
//    }
//
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        mContext = base;
//        // you must install multiDex whatever tinker is installed!
//        MultiDex.install(base);
//
//        // 安装tinker
//        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
//        Beta.installTinker(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MultiDex.install(this);
        mContext = this;
        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
//        Beta.installTinker(this);
        Bugly.init(mContext, "b52d582880", false);
        initScreenSize();
//        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext.getApplicationContext(), "smartinfo.db");
//        SQLiteDatabase sqLiteDatabase = devOpenHelper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
//        sDaoSession = daoMaster.newSession();
        DbAppliaction.initDb(mContext);
        initPool();
    }

    public static DaoSession getDaoSession() {
        return DbAppliaction.getDaoSession();
    }


    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }

    public static void initPool() {
        pool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        listPool.add(pool.load(mContext, R.raw.time_limit, 0));
        listPool.add(pool.load(mContext, R.raw.qrcode_completed, 0));
    }

    /**
     * 播放消息声音
     */
    public static void playMsg() {
        boolean b = PreferenceUtils.getBoolean(mContext.getApplicationContext(), "voice_switch", true);
        if (!b) return;
        if (null != pool) {
            int index = (Integer) listPool.get(0);
            pool.play(index, 1, 1, 0, 0, 1);
        }
    }

    /**
     * 播放二维码声音
     */
    public static void playErcodeMsg() {
        boolean b = PreferenceUtils.getBoolean(mContext.getApplicationContext(), "voice_switch", true);
        if (b) return;
        if (null != pool) {
            int index = (Integer) listPool.get(1);
            pool.play(index, 1, 1, 0, 0, 1);
        }
    }

}
