package com.example.admin.contactinfoapp.Model;
import android.provider.BaseColumns;
/**
 * Created by Admin on 8/5/2017.
 */

public final class ReaderHelper {
    private ReaderHelper(){}
    public static class EntryHelper implements BaseColumns{
        public static final String TABLE_NAME="entry";
        public static final String COLUMN_NAME_USER="name";
        public static final String COLUMN_LAST_NAME_USER="lastname";
        public static final String COLUMN_STREET_USER="street";
        public static final String COLUMN_CITY_USER="city";
        public static final String COLUMN_STATE_USER="state";
        public static final String COLUMN_PATH_USER="path";
    }
}
