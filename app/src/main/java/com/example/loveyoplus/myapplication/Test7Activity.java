package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by loveyoplus on 2017/2/19.
 */

public class Test7Activity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rl[];
    ImageView iv[],answerIv;
    int tag[],answerNum;
    TextView tv[],timer;
    int[] result;
    private Handler mHandler;
    final int GAMETIME=1000*60;//遊戲時間
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t7);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態

        result = new int[2];
        tv = new TextView[3];
        tv[0]= (TextView) findViewById(R.id.textView2);
        tv[1]= (TextView) findViewById(R.id.textView6);timer=tv[1];


        tv[0].setText("選下列所顯示之圖案");

        answerIv = (ImageView)findViewById(R.id.imageView);
        rl = new RelativeLayout[25];
        iv = new ImageView[25];
        for(int i=0;i<25;i++){
            iv[i] =(ImageView) findViewById(getResources().getIdentifier("iv" + (i + 1), "id", getPackageName()));
            rl[i] =(RelativeLayout) findViewById(getResources().getIdentifier("rl" + (i + 1), "id", getPackageName()));
            rl[i].setOnClickListener(this);
        }

        //抓取資源tag[x][0]=獲取圖片id
        tag = new int[5];

        for(int i = 0;i<5;i++){
            tag[i] = getResources().getIdentifier("t7_"+(i+1),"drawable",getPackageName());
        }
        mHandler = new Handler();
        mHandler.post(countdowntimer);

        setQuestion();
        answerNum=setImageView();
        while(answerNum<4){
            setQuestion();
            answerNum=setImageView();
        }






    }

    private Runnable countdowntimer = new Runnable() {
        public void run() {
            new CountDownTimer(GAMETIME, 1000) {

                @Override

                public void onTick(long millisUntilFinished) {
                    //倒數秒數中要做的事

                    timer.setText("倒數時間:"+new SimpleDateFormat("m").format(millisUntilFinished)+":"+ new SimpleDateFormat("s").format(millisUntilFinished));

                }

                @Override
                public void onFinish() {
                    timer.setText("倒數時間:結束");
                    for(int i=0;i<25;i++) {
                        rl[i].setVisibility(View.INVISIBLE);
                    }
                    Intent intent = new Intent();
                    intent.setClass(Test7Activity.this, Test8Activity.class);
                    startActivity(intent);
                    Test7Activity.this.finish();

                }
            }.start();

        }
    };
    public int setImageView(){
        int answerNum=0;
        for(int i=0;i<25;i++){
            Random random = new Random();
            int selected= random.nextInt(tag.length)+1;
            if((int)answerIv.getTag()==selected)answerNum++;
            iv[i].setImageResource(getResources().getIdentifier("t7_"+(selected),"drawable",getPackageName()));
            rl[i].setTag(selected);

        }
        return answerNum;
    }

    public void setQuestion(){
        Random random = new Random();
        int selected= random.nextInt(tag.length)+1;
        Object o = getResources().getIdentifier("t7_"+(selected),"drawable",getPackageName());
        answerIv.setImageResource((int)o);
        answerIv.setTag(selected);

    }
    public boolean isAnswer(RelativeLayout r){
        for(int i= 0 ;i<25;i++){
            if(r==rl[i]&&(int)rl[i].getTag()==(int)answerIv.getTag()){

                Log.e("isAnswer","true");
                return true;
            }
        }
        Log.e("isAnswer","false");
        return false;
    }
    public void removeAllMark(){
        for(int i=0;i<25;i++){
            rl[i].setOnClickListener(this);
            int count=((ViewGroup)rl[i]).getChildCount();
            //Log.e(i+"count:",count+"");
            if(count>1)
            ((ViewGroup)rl[i]).removeViewAt(1);
        }
    }
    @Override
    public void onClick(View v) {
        if(isAnswer((RelativeLayout)v)){

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            params.width = ((RelativeLayout)v).getWidth();
            params.height = ((RelativeLayout)v).getHeight();
            ImageView ivX ;
            ivX = new ImageView(this);
            ivX.setImageResource(R.drawable.circle);
            ivX.setLayoutParams(params);
            ((RelativeLayout)v).addView(ivX);
            ((RelativeLayout)v).setOnClickListener(null);
            answerNum--;
            if(answerNum==0){
                result[1]++;
                removeAllMark();
                setQuestion();
                answerNum=setImageView();
                while(answerNum<4){
                    setQuestion();
                    answerNum=setImageView();
                }

            }
        }
        else{
            result[0]++;
            removeAllMark();
            setQuestion();
            answerNum=setImageView();
            while(answerNum<4){
                setQuestion();
                answerNum=setImageView();
            }
        }
        Log.e("result","O:"+result[1]+"X:"+result[0]);
    }
}
