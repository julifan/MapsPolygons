package rs.pupin.custompolyline2;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import rs.pupin.model.DaoMaster;
import rs.pupin.model.DaoSession;


public class CustomPolyline2Application extends Application {
    public DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lease-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}