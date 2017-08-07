package com.example.admin.contactinfoapp.Model;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.admin.contactinfoapp.Model.ReaderHelper.EntryHelper;
/**
 * Created by Admin on 8/5/2017.
 */

public class DBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="my_dbUser.db";
    public static final String SQL_CREATE_ENTRIES= "CREATE TABLE "+
            EntryHelper.TABLE_NAME +" ("+
            EntryHelper._ID+" INTEGER PRIMARY KEY,"+
            EntryHelper.COLUMN_NAME_USER+" TEXT,"+
            EntryHelper.COLUMN_LAST_NAME_USER+" TEXT, "+
            EntryHelper.COLUMN_STREET_USER+" TEXT, "+
            EntryHelper.COLUMN_CITY_USER+" TEXT, "+
            EntryHelper.COLUMN_STATE_USER+" TEXT, "+
            EntryHelper.COLUMN_PATH_USER+")";
    public static final String SQL_DELETE_ENTRIES= "DROP TABLE IF EXISTS "+
            EntryHelper.TABLE_NAME;
    public DBHelper(Context context){super(context,DATABASE_NAME,null,DATABASE_VERSION);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
