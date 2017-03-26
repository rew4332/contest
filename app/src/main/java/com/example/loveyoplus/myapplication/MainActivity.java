package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity  {
    EditText etId;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        loadSetting();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Log.e("screenWidth",displayMetrics.widthPixels+"");
        Log.e("screenHeight",displayMetrics.heightPixels+"");
        int statusBarHeight2 = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight2 = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("WangJ", "status" + statusBarHeight2);

        //800 1232


        etId = (EditText) findViewById(R.id.etId);
        btnLogin = (Button)findViewById(R.id.btnEnd);
        etId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i("DONE","Enter pressed");
                    if(etId.getText().toString().equals("setting")){
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        etId.setText("");

                    }
                    else if(!etId.getText().toString().equals("")){
                        fileStorage fs = new fileStorage();
                        fs.createDirectory();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String currentDateandTime = sdf.format(new Date());
                        fs.createFile(etId.getText().toString()+currentDateandTime);

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("ID",etId.getText().toString()+"_"+currentDateandTime);
                        bundle.putString("ActivityName",MainActivity.this.getClass().getSimpleName().toString());
                        intent.putExtras(bundle);
                        intent.setClass(MainActivity.this, RedirectActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etId.getText().toString().equals("setting")){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SettingActivity.class);
                    startActivity(intent);

                }
                else if(!etId.getText().toString().equals("")){
                    fileStorage fs = new fileStorage();
                    fs.createDirectory();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    fs.createFile(etId.getText().toString()+currentDateandTime);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",etId.getText().toString()+"_"+currentDateandTime);
                    bundle.putString("ActivityName",MainActivity.this.getClass().getSimpleName().toString());
                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.this, RedirectActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        });




    }
    void loadSetting(){
        fileStorage fs = new fileStorage();
        if(!fs.checkfile("setting.txt")) {
            fs.createFile("setting");
            fs.setContinueWrite(false);
            String content = "60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n";
            fs.writeFile("setting", content);
            Log.e("file","loading default setting");
        }

    }



}

