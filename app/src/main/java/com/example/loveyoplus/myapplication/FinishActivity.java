package com.example.loveyoplus.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.loveyoplus.myapplication.fileStorage;
/**
 * Created by loveyoplus on 2017/3/11.
 */

public class FinishActivity extends AppCompatActivity {
    TextView tv;
    String ID="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態

        ID=getIntent().getStringExtra("ID");
        //tv =(TextView) findViewById(R.id.tvStart);

        fileStorage fs = new fileStorage();
        fs.createDirectory();
        fs.createFile(ID);
        fs.readFile();
        String s[] =fs.readFile().toString().split("\r\n");




        ((Button)findViewById(R.id.btnEnd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinishActivity.this.finish();
            }
        });


    }
}
