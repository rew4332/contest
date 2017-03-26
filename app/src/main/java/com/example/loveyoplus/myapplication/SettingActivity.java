package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by loveyoplus on 2017/3/16.
 */


public class SettingActivity extends AppCompatActivity {
    EditText et[];
    Button btnFinish;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "";
                Boolean isOk=false;
                for(int i=0;i<10;i++){
                    content+=et[i].getText().toString()+"\r\n";
                    if(et[i].getText().toString().equals("")) {
                        Toast.makeText(SettingActivity.this, "欄位不可空白", Toast.LENGTH_LONG).show();

                        break;
                    }
                    if(i==9)
                    isOk=true;

                }


                if(isOk){
                    fileStorage fs = new fileStorage();
                    fs.createFile("setting");
                    fs.setContinueWrite(false);

                    fs.writeFile("setting",content);
                    SettingActivity.this.finish();

                }
            }
        });


    }
    void initView() {
        et = new EditText[10];
        fileStorage fs = new fileStorage();
        fs.createFile("setting");
        fs.setContinueWrite(false);
        String s = fs.readFile();

        for (int i = 0; i < 10; i++) {
            et[i] = (EditText) findViewById(getResources().getIdentifier("et" + (i + 1), "id", getPackageName()));
            et[i].setText(s.split("\r\n")[i + 1]);
            et[i].setInputType(InputType.TYPE_CLASS_NUMBER); //調用數字鍵盤
            et[i].setSelection(0, et[i].length());
            Log.e("getEditText", et[i].getText().toString());
        }
        btnFinish = (Button) findViewById(R.id.btnFinish);
        et[9].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i("DONE","Enter pressed");
                    String content = "";
                    Boolean isOk=false;
                    for(int i=0;i<10;i++){
                        content+=et[i].getText().toString()+"\r\n";
                        if(et[i].getText().toString().equals("")) {
                            Toast.makeText(SettingActivity.this, "欄位不可空白", Toast.LENGTH_LONG).show();

                            break;
                        }
                        if(i==9)
                            isOk=true;

                    }


                    if(isOk){
                        fileStorage fs = new fileStorage();
                        fs.createFile("setting");
                        fs.setContinueWrite(false);

                        fs.writeFile("setting",content);
                        SettingActivity.this.finish();

                    }

                }
                return false;
            }
        });


    }
}
