package com.example.loveyoplus.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Random;
import android.os.Handler;

import static android.R.attr.strokeColor;
import static android.R.attr.strokeWidth;


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
        btnLogin = (Button)findViewById(R.id.btnStart);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etId.getText().toString().equals("")){

                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Test1Activity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        });




    }

}

