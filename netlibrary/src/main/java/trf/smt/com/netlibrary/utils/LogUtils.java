package trf.smt.com.netlibrary.utils;

import trf.smt.com.netlibrary.BuildConfig;

/**
 * Created by gbh on 2018/4/2  15:58.
 *
 * @describe
 */

public class LogUtils {
    public static boolean isLog = BuildConfig.DEBUG;

    public static void sysout(Object obj) {
        if (isLog)
            System.out.println(obj.toString());
    }

    public static void sysout(String tag, Object obj) {
        if (isLog)
            System.out.println(tag + ":" + obj.toString());
    }
}
