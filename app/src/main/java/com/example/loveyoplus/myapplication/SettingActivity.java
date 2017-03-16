package com.example.loveyoplus.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    void initView(){
        et = new EditText[10];
        for(int i=0;i<10;i++){
            et[i]= (EditText)  findViewById(getResources().getIdentifier("et" + (i + 1), "id", getPackageName()));
        }
        btnFinish = (Button)findViewById(R.id.btnFinish);


    }
}
