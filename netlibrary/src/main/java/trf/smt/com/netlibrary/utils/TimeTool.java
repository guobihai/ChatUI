package trf.smt.com.netlibrary.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by gbh on 2018/7/2  16:58.
 *
 * @describe
 */

public class TimeTool {
    //将日期格式的字符串转换为长整型//2018-10-20 14:42:47
    public static long timeTolongTime(String date, String format) {
        if(TextUtils.isEmpty(date))return System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
