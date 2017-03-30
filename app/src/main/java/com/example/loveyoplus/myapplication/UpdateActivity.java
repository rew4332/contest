package com.example.loveyoplus.myapplication;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.util.Map;

/**
 * Created by loveyoplus on 2017/3/29.
 */

public class UpdateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle_page);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        NetCon conn = new NetCon();

        String msg;
        fileStorage fs = new fileStorage();
        fs.createDirectory();
        String filename="";
        fs.createFile(filename);

        try {

            // Create the data structure to transfer.
            Map<String, String> data = fs.readFile2Map(1);
            //Log.e("map",data+"");

            // Send Request
            msg = conn.SetJson(data).SetUrl("http://140.116.179.52/dbinsert.php").Execute();
            Log.e("msg ",msg);
        } catch (IOException e) {
            msg = e.getMessage();
            Log.e("exception",msg);
        }



    }
}
