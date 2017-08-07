package com.example.admin.contactinfoapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.contactinfoapp.Data.Name;
import com.example.admin.contactinfoapp.Data.ParseJsontoData;
import com.example.admin.contactinfoapp.Data.Result;
import com.example.admin.contactinfoapp.Model.DBHelper;
import com.example.admin.contactinfoapp.Model.ReaderHelper;
import com.example.admin.contactinfoapp.Model.ReaderHelper.EntryHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName()+"_TAG";
    public static final String MAIN_EXTRA_DATA = "com.example.admin.contactinfoapp_MAIN_EXTRA_DATA";
    private static final String MAIN_EXTRA_DATA_NAME = "com.example.admin.contactinfoapp_MAIN_EXTRA_DATA_NAME";
    private static final String MAIN_EXTRA_DATA_ADDRES = "com.example.admin.contactinfoapp_MAIN_EXTRA_DATA_ADDRES";
    private static final String MAIN_EXTRA_DATA_EMAIL = "com.example.admin.contactinfoapp_MAIN_EXTRA_DATA_EMAIL";
    private static final String MAIN_EXTRA_DATA_PICTURE= "com.example.admin.contactinfoapp_MAIN_EXTRA_DATA_PICTURE";
    public OkHttpClient conectionClient;
    public EditText etsearchname;
    public Button btnsearch,btnsave,btnramdom;
    public ImageView imguser;
    public TextView tvemailuser,tvaddressuser,tvnameuser;
    private SQLiteDatabase database;
    private DBHelper helper;
    public List<Result> resultList;
    public ParseJsontoData helperParser;
    public Bitmap bmp;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvnameuser.setText(msg.getData().getString(MAIN_EXTRA_DATA_NAME).toUpperCase());
            tvaddressuser.setText(msg.getData().getString(MAIN_EXTRA_DATA_ADDRES).toUpperCase());
            tvemailuser.setText(msg.getData().getString(MAIN_EXTRA_DATA_EMAIL));
            byte[]byteArray= msg.getData().getByteArray(MAIN_EXTRA_DATA_PICTURE);
            bmp=BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
            imguser.setImageBitmap(bmp);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conectionClient= new OkHttpClient();
        etsearchname=(EditText)findViewById(R.id.et_search_name);
        imguser=(ImageView)findViewById(R.id.img_user);
        tvemailuser=(TextView)findViewById(R.id.tv_email_user);
        tvaddressuser=(TextView)findViewById(R.id.tv_address_user);
        tvnameuser=(TextView)findViewById(R.id.tv_name_user);
        btnsave=(Button)findViewById(R.id.btn_save);
        btnsave.setOnClickListener(this);
        btnsearch=(Button)findViewById(R.id.btn_search);
        btnsearch.setOnClickListener(this);
        btnramdom=(Button)findViewById(R.id.btn_create_random);
        btnramdom.setOnClickListener(this);
        resultList= new ArrayList<>();

        helper=new DBHelper(MainActivity.this);
        database=helper.getWritableDatabase();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                saveUser();
                break;
            case R.id.btn_search:
                loadUser();
                break;
            case R.id.btn_create_random:
                createNewRandomUser();
                break;
        }
    }

    private void createNewRandomUser() {
        helperParser= new ParseJsontoData();
        resultList.clear();
        String containerURL="";
        final Request request= new Request.Builder().url("http://randomuser.me/api").build();
        conectionClient.newCall(request).enqueue(new Callback() {//getting info back
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();//this supousely not work
            }
            @Override//proccess the info for deserialize JSON
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String respon= response.body().string();
                    try{
                        JSONObject jObject= new JSONObject(respon);
                        JSONArray jArray = jObject.getJSONArray("results");
                        for(int i=0;i<jArray.length();i++){
                            resultList.add(helperParser.setFromJSONtoPOJO(jArray.getJSONObject(i)));
                        }
                        Message message= handler.obtainMessage();
                        Bundle data= new Bundle();
                        for (Result itemResult:
                                resultList) {
                            //Log.d(TAG, "onResume: "+itemResult);
                            data.putString(MAIN_EXTRA_DATA_NAME,String.format(itemResult.getName().getFirst()," ",
                                    itemResult.getName().getLast()));
                            data.putString(MAIN_EXTRA_DATA_ADDRES,String.format(itemResult.getLocation().getStreet()+
                                    " "+itemResult.getLocation().getCity()+" "+itemResult.getLocation().getState()));
                            data.putString(MAIN_EXTRA_DATA_EMAIL,itemResult.getEmail());

                            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new
                                    URL(itemResult.getPicture().getLarge()).getContent());
                            ByteArrayOutputStream outputstream= new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputstream);
                            byte[] byteArray= outputstream.toByteArray();
                            data.putByteArray(MAIN_EXTRA_DATA_PICTURE,byteArray);
                        }
                        message.setData(data);
                        handler.sendMessage(message);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void loadUser() {
        if(checkTextEmpty(etsearchname.getText().toString())){
            Toast.makeText(MainActivity.this,"ERROR EMPTY VALUES",Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent= new Intent(MainActivity.this,DetailUserActivity.class);
            intent.putExtra(MAIN_EXTRA_DATA,etsearchname.getText().toString().toUpperCase());
            startActivity(intent);
        }
    }
    public void saveUser() {
        saveRecord();
    }
    public void saveRecord(){
        if(checkTextEmpty(tvnameuser.getText().toString(),tvaddressuser.getText().toString(),tvemailuser.getText().toString())){
            Toast.makeText(MainActivity.this,"ERROR EMPTY VALUES",Toast.LENGTH_SHORT).show();
        }
        else{
            ContentValues values= new ContentValues();
            for(Result items: resultList){
                values.put(EntryHelper.COLUMN_NAME_USER, items.getName().getFirst());
                values.put(EntryHelper.COLUMN_LAST_NAME_USER,items.getName().getLast());
                values.put(EntryHelper.COLUMN_STREET_USER,items.getLocation().getStreet());
                values.put(EntryHelper.COLUMN_CITY_USER,items.getLocation().getCity());
                values.put(EntryHelper.COLUMN_STATE_USER,items.getLocation().getState());
                values.put(EntryHelper.COLUMN_PATH_USER,items.getPicture().getLarge());
            }
            long recordId= database.insert(EntryHelper.TABLE_NAME,null,values);
            if(recordId>0){
                Log.d(TAG, "saveRecord: " + recordId);
                Toast.makeText(MainActivity.this,"USER SAVED",Toast.LENGTH_SHORT).show();
            }
        }
        saveToInternalStorage(bmp);
    }
    public Boolean checkTextEmpty(String name, String add, String email){
        if(name.isEmpty()&add.isEmpty()&email.isEmpty())
            return true;
        else
            return false;
    }
    public Boolean checkTextEmpty(String name){
        return name.isEmpty();
    }
    public void saveToInternalStorage(Bitmap bitmapWork){
        Log.d(TAG, "saveToInternalStorage: "+bitmapWork.toString());
        /*File myExternalFile;
        FileOutputStream fos;
        String path= Environment.getExternalStorageDirectory().toString();
        Log.d(TAG, "saveToInternalStorage: "+path);
        if(isExternalReadOnly()||isExternalStorageAvailable()) {
            try {
                myExternalFile= new File(path,tvnameuser.getText().toString()+".jgp");
                fos = new FileOutputStream(myExternalFile);

                bitmapWork.compress(Bitmap.CompressFormat.JPEG,100,fos);
                fos.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }*/
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File dir= cw.getDir("imageDir", Context.MODE_PRIVATE);
        File path= new File(dir,tvnameuser.getText().toString()+".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(path);
            bitmapWork.compress(Bitmap.CompressFormat.JPEG,100,fos);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        Log.d(TAG, "saveToInternalStorage: 1"+path.toString());
    }
    private static boolean isExternalReadOnly(){
        String externalStorageState= Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState)){
            return true;
        }
        else {
            return false;
        }
    }
    private static boolean isExternalStorageAvailable(){
        String externalStorageState=Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(externalStorageState)){
            return true;
        }
        else
            return false;
    }
}
