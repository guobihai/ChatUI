package trf.smt.com.netlibrary.base;

import android.app.Application;

/**
 * Created by gbh on 2018/6/25  11:17.
 *
 * @describe
 */

public class SmtApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DbAppliaction.initDb(this);
    }
}
