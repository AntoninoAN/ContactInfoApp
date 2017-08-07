package com.example.admin.contactinfoapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.contactinfoapp.Model.DBHelper;
import com.example.admin.contactinfoapp.Model.ReaderHelper;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;

public class DetailUserActivity extends AppCompatActivity {
    private static final String TAG = DetailUserActivity.class.getSimpleName()+"_TAG";
    public String getName="";
    public ImageView iv_detail_image;
    public TextView tv_street_detail,tv_last_name_detail,tv_name_detail;
    private SQLiteDatabase database;
    private DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        Intent intent= getIntent();
        helper=new DBHelper(DetailUserActivity.this);
        database=helper.getWritableDatabase();
        if(intent!=null){
            tv_name_detail=(TextView)findViewById(R.id.tv_name_detail);
            tv_last_name_detail=(TextView)findViewById(R.id.tv_last_name_detail);
            tv_street_detail=(TextView)findViewById(R.id.tv_street_detail);

            iv_detail_image=(ImageView)findViewById(R.id.iv_detail_image);
            readRecord(intent.getStringExtra(MainActivity.MAIN_EXTRA_DATA));
        }
    }

    private void readRecord(String nameSearch){
        String name,lastname,street,city,State;

        String[] projection={ReaderHelper.EntryHelper._ID,
                ReaderHelper.EntryHelper.COLUMN_NAME_USER,
                ReaderHelper.EntryHelper.COLUMN_LAST_NAME_USER,
                ReaderHelper.EntryHelper.COLUMN_STREET_USER,
                ReaderHelper.EntryHelper.COLUMN_CITY_USER,
                ReaderHelper.EntryHelper.COLUMN_STATE_USER
            };
        String selection= ReaderHelper.EntryHelper.COLUMN_NAME_USER + " LIKE?";
        String[] selectionArg={
                nameSearch
        };
        String sortOrder= ReaderHelper.EntryHelper._ID+" ASC";
        Cursor cursor= database.query(
                ReaderHelper.EntryHelper.TABLE_NAME,
                projection,
                selection,
                selectionArg,
                null,
                null,
                sortOrder,
                null);
        while (cursor.moveToNext()) {
            long entryId = cursor.getLong(cursor.getColumnIndexOrThrow(ReaderHelper.EntryHelper._ID));
            name = cursor.getString(cursor.getColumnIndexOrThrow(ReaderHelper.EntryHelper.COLUMN_NAME_USER));
            lastname = cursor.getString(cursor.getColumnIndexOrThrow(ReaderHelper.EntryHelper.COLUMN_LAST_NAME_USER));
            street= cursor.getString(cursor.getColumnIndexOrThrow(ReaderHelper.EntryHelper.COLUMN_STREET_USER));
            city= cursor.getString(cursor.getColumnIndexOrThrow(ReaderHelper.EntryHelper.COLUMN_CITY_USER));
            State= cursor.getString(cursor.getColumnIndexOrThrow(ReaderHelper.EntryHelper.COLUMN_STATE_USER));

            Log.d(TAG, "readRecord: id: " + entryId+" title: "+name+" subtitle: "+lastname);
            printValues(name,lastname,street,city,State);
        }
    }
    public void printValues(String name, String lastname, String street, String city, String state){
        tv_name_detail.setText(new StringBuilder("NAME: "+name.toUpperCase()));

        tv_last_name_detail.setText("LAST NAME: "+lastname.toUpperCase());
        tv_street_detail.setText("STREET: "+street.toUpperCase()+"\nCITY: "+city.toUpperCase()+
                "\nSTATE: "+state.toUpperCase());
        try{
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File dir= cw.getDir("imageDir", Context.MODE_PRIVATE);
            File path= new File(dir,name.toUpperCase()+".jpg");
            //File f= new File(Environment.getExternalStorageDirectory(),name+".jpg");
            Log.d(TAG, "printValues: "+path.getAbsolutePath());
            Bitmap bmp= BitmapFactory.decodeStream(new FileInputStream(path));
            iv_detail_image.setImageBitmap(bmp);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
