package com.example.loveyoplus.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Handler;

import com.example.loveyoplus.myapplication.fileStorage;
/**
 * Created by loveyoplus on 2017/3/11.
 */

public class FinishActivity extends AppCompatActivity {
    TextView tv;
    String ID="";
    Button btnupdate;
    Message message;
    ImageView ivwaiting;
    Boolean showAnimate=true;
    android.os.Handler listenhandler;
    String msg;
    String origin,modify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態

        ID=getIntent().getStringExtra("ID");
        //tv =(TextView) findViewById(R.id.tvStart);
        ivwaiting = (ImageView)findViewById(R.id.imageView6) ;
        message = new Message();
        message.what=1;
        fileStorage fs =new fileStorage();
        fs.createFile("update");
        origin=fs.readFile();
        origin.replace("\r\n","");
        if(origin.equals(""))modify=ID+"\r\n";
        else modify=origin+"\r\n"+ID+"\r\n";
        fs.setContinueWrite(false);
        fs.writeFile("update",modify);


        listenhandler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e("msg.what",msg.what+"");
                switch (msg.what){

                    case 1:

                        startCountdowntimer.run();
                        break;
                    case 2:
                        Toast.makeText(FinishActivity.this,"上傳失敗 請檢查網路狀態",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(FinishActivity.this,"上傳成功",Toast.LENGTH_LONG).show();
                        break;

                }
            }
        };




        btnupdate = (Button)findViewById(R.id.btnupdate);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable(){
                    public void run(){
                        while(showAnimate) {
                            Message m = new Message();
                            m.what = 1;
                            FinishActivity.this.listenhandler.sendMessage(m);
                            try {
                                Thread.sleep(2300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                btnupdate.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);



                        fileStorage fs = new fileStorage();
                        fs.createDirectory();
                        String filename=ID;
                        fs.createFile(filename);
                        int count=0;
                        for(int i=0;i<10;i++){
                            if(count>30)break;
                            count++;
                            try {
                                Map<String, String> data;
                                // Create the data structure to transfer.

                                    NetCon conn = new NetCon();
                                    data = fs.readFile2Map(i+1);
                                    Log.e("map",data+"");
                                    // Send Request
                                    msg = conn.SetJson(data).SetUrl("http://140.116.179.52/dbinsert.php").Execute();
                                    Log.e("msg ",msg);
                                    if(!msg.equals("OK"))i--;

                                try {
                                    Thread.sleep(700);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            } catch (IOException e) {
                                msg = e.getMessage();
                                Log.e("exception",msg);
                            }
                        }
                        showAnimate=false;
                        if(count>30){
                            btnupdate.setEnabled(true);
                            Message m = new Message();
                            m.what = 2;
                            FinishActivity.this.listenhandler.sendMessage(m);

                        }
                        else{
                            Message m = new Message();
                            m.what = 3;
                            fileStorage fstmp =new fileStorage();
                            fstmp.setContinueWrite(false);
                            fstmp.writeFile("update",origin+"\r\n");
                            FinishActivity.this.listenhandler.sendMessage(m);
                        }


                    }
                }).start();

            }
        });







        ((Button)findViewById(R.id.btnEnd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Leave();
            }
        });


    }
    private void Leave() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("是否離開?")
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 左方按鈕方法

                    }
                })
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 右方按鈕方法
                        FinishActivity.this.finish();
                        System.exit(0);

                    }
                });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
    }
    private Runnable startCountdowntimer = new Runnable() {
        public void run() {
            Log.e("countdowntimer","");
            new CountDownTimer(2300, 50) {

                @Override

                public void onTick(long millisUntilFinished) {
                    ivwaiting.setImageResource(getResources().getIdentifier("brainwave" + (23-(millisUntilFinished)%1150/50), "drawable", getPackageName()));
                }

                @Override
                public void onFinish() {
                    ivwaiting.setImageResource(getResources().getIdentifier("brainwave" + (1), "drawable", getPackageName()));


                }
            }.start();

        }
    };
}

