package trf.smt.com.netlibrary.base;

import android.content.Context;

import trf.smt.com.netlibrary.greendao.DaoMaster;
import trf.smt.com.netlibrary.greendao.DaoSession;
import trf.smt.com.netlibrary.greendao.MySQLiteOpenHelper;

/**
 * Created by gbh on 2018/6/25  11:17.
 *
 * @describe db 初始化
 */

public class DbAppliaction {
    private static DaoSession sDaoSession;

    public static void initDb(Context context) {
        if (null == context)
            throw new IllegalArgumentException("context is not null for application");

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context.getApplicationContext(), "smartinfo.db",
                null);
        DaoMaster daoMaster  = new DaoMaster(helper.getWritableDatabase());

//        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), "smartinfo.db");
//        SQLiteDatabase sqLiteDatabase = devOpenHelper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        sDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        if (null == sDaoSession) throw new IllegalArgumentException("sDaoSession is null ");
        return sDaoSession;
    }

}
