package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by loveyoplus on 2017/2/20.
 */

public class Test8Activity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rl[],rl1;
    ImageView iv[],answerIv;
    int tag[],answerNum;
    TextView tv[],timer;
    int[] result;
    private Handler mHandler;
    final int GAMETIME=1000*5;//遊戲時間
    String ID="";
    String startDateandTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t7);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        startDateandTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        ID=getIntent().getStringExtra("ID");

        result = new int[2];
        tv = new TextView[3];
        tv[0]= (TextView) findViewById(R.id.textView2);
        tv[1]= (TextView) findViewById(R.id.textView6);timer=tv[1];


        tv[0].setText("選下列所顯示之數字");

        answerIv = (ImageView)findViewById(R.id.imageView);
        rl1 = (RelativeLayout)findViewById(R.id.rl);
        rl = new RelativeLayout[25];
        iv = new ImageView[25];


        //抓取資源tag[x][0]=獲取圖片id
        tag = new int[9];

        for(int i = 0;i<9;i++){
            tag[i] = getResources().getIdentifier("t8_"+(i+1),"drawable",getPackageName());
        }
        mHandler = new Handler();
        mHandler.post(startCountdowntimer);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageView tempiv =  new ImageView(Test8Activity.this);
        tempiv.setScaleType(ImageView.ScaleType.FIT_XY);
        tempiv.setBackgroundColor(Color.WHITE);
        tempiv.setLayoutParams(rlp);
        rl1.addView(tempiv);
    }
    private Runnable startCountdowntimer = new Runnable() {
        public void run() {
            final RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

            rl1.addView(new ImageView(Test8Activity.this));
            //rl1.addView(tempiv);
            new CountDownTimer(5000, 100) {

                @Override

                public void onTick(long millisUntilFinished) {
                    ImageView tempiv = new ImageView(Test8Activity.this);
                    tempiv.setLayoutParams(rlp);

                    //倒數秒數中要做的事

                    rl1.removeViewAt(rl1.getChildCount()-1);
                    tempiv.setImageResource(getResources().getIdentifier("t8_" + (int)(((millisUntilFinished)/1000)+1), "drawable", getPackageName()));
                    tempiv.setAlpha((float) ((int)millisUntilFinished%1000/1000.0f));
                    tempiv.setScaleType(ImageView.ScaleType.FIT_XY);

                    rl1.addView(tempiv);



                    Log.e("alpha",millisUntilFinished%1000/1000.0f+"");
                    Log.e("source",millisUntilFinished+"");



                    //if(millisUntilFinished%1000<300)
                    //rl1.removeView(tempiv);



                }

                @Override
                public void onFinish() {
                    rl1.removeViewAt(rl1.getChildCount()-1);
                    rl1.removeViewAt(rl1.getChildCount()-1);
                    for(int i=0;i<25;i++){
                        iv[i] =(ImageView) findViewById(getResources().getIdentifier("iv" + (i + 1), "id", getPackageName()));
                        rl[i] =(RelativeLayout) findViewById(getResources().getIdentifier("rl" + (i + 1), "id", getPackageName()));
                        rl[i].setOnClickListener(Test8Activity.this);
                    }
                    setQuestion();
                    answerNum=setImageView();
                    while(answerNum<4){
                        setQuestion();
                        answerNum=setImageView();
                    }

                    mHandler.post(countdowntimer);

                }
            }.start();

        }
    };
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

                    fileStorage fs = new fileStorage();
                    String endDateandTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String content = "test8:\r\n"+startDateandTime+";"+endDateandTime+";true:"+result[1]+";false:"+result[0]+"\r\n";
                    fs.writeFile(ID,content);
                    Intent intent = new Intent();
                    intent.putExtra("ID",ID);
                    intent.setClass(Test8Activity.this, Test9Activity.class);
                    startActivity(intent);
                    Test8Activity.this.finish();
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
            iv[i].setImageResource(getResources().getIdentifier("t8_"+(selected),"drawable",getPackageName()));
            rl[i].setTag(selected);

        }
        return answerNum;
    }

    public void setQuestion(){
        Random random = new Random();
        int selected= random.nextInt(tag.length)+1;
        Object o = getResources().getIdentifier("t8_"+(selected),"drawable",getPackageName());
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
                while(answerNum<4){
                    setQuestion();
                    answerNum=setImageView();
                }
            }
        }
        Log.e("result","O:"+result[1]+"X:"+result[0]);
    }
}