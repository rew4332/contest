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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Random;
import android.os.Handler;

import static android.R.attr.strokeColor;
import static android.R.attr.strokeWidth;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button[] btn;
    TextView tv[];
    RelativeLayout rl1;
    LinearLayout llStart;
    int[] viewSize;//左半邊測驗區大小
    String[] answer;//題目
    int[] result;//紀錄目前對錯數量
    final int GAMETIME=1000*10;//遊戲時間
    String randomNum[]={"1","2","3","4","5","6","7","8","9"};
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

        tv =new TextView[3];
        for(int i=0;i<3;i++){
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

        //rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        //llStart = (LinearLayout) findViewById(R.id.llStart);
        //Log.e("llStart",llStart+"");
        //llStart.setLayoutParams(rlp);
        //llStart.setMinimumWidth(viewSize[0]);
        //llStart.setMinimumHeight(viewSize[1]);
        //llStart.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_focused));
        //((ViewGroup)findViewById(R.id.rlParent)).addView(llStart);

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
                    intent.setClass(MainActivity.this, Test2Activity.class);
                    startActivity(intent);
                    MainActivity.this.finish();

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
            s+=answer[i]+"\t\t\t\t";

        }
        tv[1].setText(s);
    }
    //設定按鈕位置及按鈕數字
    public void setBtn(){

        int btnSize=viewSize[0]/5;
        int hdiv3=viewSize[1]/3;
        int wdiv3=viewSize[0]/3;


        //Set the button position
        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(Color.parseColor("#0066FF"));
        gdDefault.setCornerRadius(btnSize/2);

        RelativeLayout.LayoutParams params;
        for(int i = 0 ;i<9;i++){
            btn[i].setVisibility(View.VISIBLE);

            //btn[i].setBackgroundDrawable(gdDefault);
            btn[i].setBackground(gdDefault);

            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.width=btnSize;
            params.height=btnSize;


            int wRan=/* 0;*/ (int)(Math.random()*(wdiv3-btnSize));
            Log.d("wRan",wRan+"");
            int hRan=/* 0;*/ (int)(Math.random()*(hdiv3-btnSize));
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


        int width =(displayMetrics.widthPixels);// rl1.getMeasuredWidth();
        int height = 2*displayMetrics.heightPixels/3;//rl1.getMeasuredHeight();
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
                        tv[1].setText(tv[1].getText().toString().replace(answer[j]+"\t\t\t\t",""));
                    }

                }
                if ((result[0]+result[1])%3==0){
                    setBtn();
                    setQuestion();
                }
                Log.e("result","O:"+result[1]+"\nX:"+result[0]);
            }
        }
    }
}

