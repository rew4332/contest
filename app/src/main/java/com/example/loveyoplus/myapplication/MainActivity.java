package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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





        etId = (EditText) findViewById(R.id.etId);
        btnLogin = (Button)findViewById(R.id.btnEnd);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etId.getText().toString().equals("")){
                    fileStorage fs = new fileStorage();
                    fs.createDirectory();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    fs.createFile(etId.getText().toString()+currentDateandTime);

                    Intent intent = new Intent();
                    intent.putExtra("ID",etId.getText().toString()+"_"+currentDateandTime);
                    intent.setClass(MainActivity.this, Test1Activity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        });




    }



}

