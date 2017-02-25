package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by loveyoplus on 2017/2/16.
 */

public class Test2Activity extends AppCompatActivity implements View.OnClickListener {
    Button[] btn;
    TextView tv[];
    RelativeLayout rl1;
    int[] viewSize;//左半邊測驗區大小
    String[] answer;//題目
    int[] result;//紀錄目前對錯數量
    final int GAMETIME=1000*1;//遊戲時間
    String randomNum[]={"一","二","三","四","五","六","七","八","九"};
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t1);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        //initialize
        result = new int[2];
        rl1 = (RelativeLayout) findViewById(R.id.rl1);

        btn = new Button[10];
        for(int i=0;i<9;i++){
            btn[i]=(Button)findViewById(getResources().getIdentifier("button" + (i + 1), "id", getPackageName()));
            btn[i].setTextSize(50);
            btn[i].setOnClickListener(this);
        }

        tv =new TextView[4];
        for(int i=0;i<4;i++){
            tv[i] =  (TextView)findViewById(getResources().getIdentifier("tv" + (i + 1), "id", getPackageName()));
        }
        tv[0].setText("限時時間內找出數字:");

        //get layout size
        viewSize = new int[2];
        viewSize[0]=getViewSize()[0];
        viewSize[1]=getViewSize()[1];

        /*
                    set button position
                    set button num
                */
        setBtn();
        setQuestion();//first Question
        mHandler = new Handler();
        mHandler.post(countdowntimer);



    }
    //timer
    private Runnable countdowntimer = new Runnable() {
        public void run() {
            new CountDownTimer(GAMETIME, 1000) {

                @Override

                public void onTick(long millisUntilFinished) {
                    //倒數秒數中要做的事

                    tv[2].setText("倒數時間:"+new SimpleDateFormat("m").format(millisUntilFinished)+":"+ new SimpleDateFormat("s").format(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    tv[2].setText("倒數時間:結束");
                    disableBtn();
                    Intent intent = new Intent();
                    intent.setClass(Test2Activity.this, Test3Activity.class);
                    startActivity(intent);
                    finish();

                }
            }.start();

        }
    };
    //鎖住所有按鈕
    public void disableBtn(){
        for(int i = 0 ;i<9;i++){
            btn[i].setVisibility(View.INVISIBLE);
        }
    }
    //設定答案
    public void setQuestion(){

        answer = new String[3];

        String s="";
        for(int i = 0 ;i<3;i++){
            String temp;
            Random random = new Random();
            int selected= random.nextInt(9-i);
            temp=randomNum[selected];
            answer[i]=randomNum[selected];
            randomNum[selected]=randomNum[9-1-i];
            randomNum[9-1-i]=temp;
            s+=answer[i]+",";

        }
        tv[1].setText(s);
    }
    //設定按鈕位置及按鈕數字
    public void setBtn(){

        int hdiv5=viewSize[1]/5;
        int hdiv3=viewSize[1]/3;
        int wdiv3=viewSize[0]/3;

        //Set the button position
        RelativeLayout.LayoutParams params;
        for(int i = 0 ;i<9;i++){
            btn[i].setVisibility(View.VISIBLE);
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.width=hdiv5;
            params.height=hdiv5;

            int wRan=/* 0;*/ (int)(Math.random()*(wdiv3-hdiv5));
            Log.d("wRan",wRan+"");
            int hRan=/* 0;*/ (int)(Math.random()*(hdiv3-hdiv5));
            Log.d("hRan",wRan+"");
            if (i<3)
                params.setMargins(i%3*wdiv3+wRan,0+hRan,0,0);
            else if(i<6)
                params.setMargins(i%3*wdiv3+wRan,hdiv3+hRan,0,0);
            else
                params.setMargins(i%3*wdiv3+wRan,hdiv3*2+hRan,0,0);
            btn[i].setLayoutParams(params);
        }

        //set Button text  抽牌法

        for(int i = 0 ;i<9;i++){

            String temp;
            Random random = new Random();
            int selected= random.nextInt(9-i);
            temp=randomNum[selected];
            btn[i].setText(randomNum[selected]+"");
            randomNum[selected]=randomNum[9-1-i];
            randomNum[9-1-i]=temp;

        }


    }
    //得到relativelayout大小
    public int[] getViewSize(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        int width =(2*displayMetrics.widthPixels/3);// rl1.getMeasuredWidth();
        int height = displayMetrics.heightPixels;//rl1.getMeasuredHeight();
        int[] viewSize={width,height};
        Log.e("viewWidth",viewSize[0]+"");
        Log.e("viewHeight",viewSize[1]+"");
        return viewSize;

    }


    @Override
    public void onClick(View v) {
        for(int i=0;i<9;i++) {
            if ( v==btn[i]){
                btn[i].setVisibility(View.INVISIBLE);
                result[0]++;
                for(int j=0;j<3;j++) {
                    if (answer[j].equals(btn[i].getText().toString())) {
                        result[1]++;
                        result[0]--;
                        tv[1].setText(tv[1].getText().toString().replace(answer[j]+",",""));
                    }

                }
                if ((result[0]+result[1])%3==0){
                    setBtn();
                    setQuestion();
                }
                tv[3].setText("O:"+result[1]+"\nX:"+result[0]);
            }
        }
    }
}
