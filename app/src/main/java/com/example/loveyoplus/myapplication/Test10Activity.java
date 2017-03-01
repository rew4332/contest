package com.example.loveyoplus.myapplication;

import android.media.MediaPlayer;
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
 * Created by loveyoplus on 2017/2/22.
 */

public class Test10Activity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout rl[];
    ImageView iv[],answerIv;
    int tag[],answerNum,soundAnswer=-1;
    TextView tv[],timer;
    int[] result,soundResult;
    private Handler mHandler;
    MediaPlayer mp[];
    final int GAMETIME=1000*150;//遊戲時間

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t9);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態

        result = new int[2];
        soundResult= new int[2];
        tv = new TextView[3];
        tv[0]= (TextView) findViewById(R.id.textView2);
        tv[1]= (TextView) findViewById(R.id.textView6);timer=tv[1];
        tv[2]= (TextView) findViewById(R.id.textView5);
        mp = new MediaPlayer[3];
        mp[0] = MediaPlayer.create(this, R.raw.drumsound);
        mp[1] = MediaPlayer.create(this, R.raw.pianosound);
        mp[2] = MediaPlayer.create(this, R.raw.trumpetsound);


        tv[0].setText("選下列所顯示之圖案");

        answerIv = (ImageView)findViewById(R.id.imageView);
        rl = new RelativeLayout[23];
        iv = new ImageView[23];
        for(int i=0;i<23;i++){
            iv[i] =(ImageView) findViewById(getResources().getIdentifier("iv" + (i + 1), "id", getPackageName()));
            rl[i] =(RelativeLayout) findViewById(getResources().getIdentifier("rl" + (i + 1), "id", getPackageName()));
            rl[i].setOnClickListener(this);
        }

        //抓取資源tag[x][0]=獲取圖片id
        tag = new int[9];

        for(int i = 0;i<9;i++){
            tag[i] = getResources().getIdentifier("t8_"+(i+1),"drawable",getPackageName());
        }
        mHandler = new Handler();

        mHandler.post(countdowntimer);
        mHandler.post(soundtimer);


        setQuestion();
        answerNum=setImageView();
        while(answerNum<4){
            setQuestion();
            answerNum=setImageView();
        }


    }
    private Runnable soundtimer = new Runnable() {
        public void run() {
            new CountDownTimer(100000, 4000) {

                @Override

                public void onTick(long millisUntilFinished) {
                    //倒數秒數中要做的事
                    removeSoundAllMark();

                    if(millisUntilFinished<=99000){
                        if(soundAnswer!=-1)soundResult[0]++;
                        setSoundQuestion();
                        Log.d("setSoundQ answer",soundAnswer+"");
                        mp[soundAnswer].start();
                    }



                }

                @Override
                public void onFinish() {
                    Test10Activity.this.finish();


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
                    for(int i=0;i<20;i++) {
                        rl[i].setVisibility(View.INVISIBLE);
                    }

                }
            }.start();

        }
    };

    public void setSoundQuestion(){
        Random rand  = new Random();
        int selected = rand.nextInt(3);
        soundAnswer=selected;
    }
    public boolean isSoundAnswer(RelativeLayout r){
        if(soundAnswer==0&&r==rl[20])return true;
        else if(soundAnswer==1&&r==rl[21])return true;
        else if(soundAnswer==2&&r==rl[22])return true;
        return false;
    }

    public int setImageView(){
        int answerNum=0;
        for(int i=0;i<20;i++){
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
        for(int i= 0 ;i<20;i++){
            if(r==rl[i]&&(int)rl[i].getTag()==(int)answerIv.getTag()){

                Log.e("isAnswer","true");
                return true;
            }
        }
        Log.e("isAnswer","false");
        return false;
    }
    public void removeAllMark(){
        for(int i=0;i<20;i++){
            rl[i].setOnClickListener(this);
            int count=((ViewGroup)rl[i]).getChildCount();
            //Log.e(i+"count:",count+"");
            if(count>1) {
                ((ViewGroup) rl[i]).removeViewAt(1);
            }
        }
    }
    public void removeSoundAllMark(){
        for(int i=20;i<23;i++){
            int count=((ViewGroup)rl[i]).getChildCount();
            //Log.e(i+"count:",count+"");
            if(count>1) {
                ((ViewGroup) rl[i]).removeViewAt(1);
            }
        }
    }
    @Override
    public void onClick(View v) {
        if((RelativeLayout)v==rl[20]||(RelativeLayout)v==rl[21]||(RelativeLayout)v==rl[22]){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            params.width = ((RelativeLayout)v).getWidth();
            params.height = ((RelativeLayout)v).getHeight();
            ImageView ivX ;
            ivX = new ImageView(this);
            ivX.setImageResource(R.drawable.circle);
            ivX.setLayoutParams(params);

            Log.e("sound:","ok");
            if(soundAnswer==-1);
            else if(isSoundAnswer((RelativeLayout)v)){
                Log.d("sound result","true");
                soundAnswer=-1;
                soundResult[1]++;
                ((RelativeLayout)v).addView(ivX);

            }
            else {
                Log.d("sound result","false");
                soundResult[0]++;
                soundAnswer=-1;
                ((RelativeLayout)v).addView(ivX);
            }

        }


        else if(isAnswer((RelativeLayout)v)){

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
        tv[2].setText("Image:\n\t O:"+result[1]+"X:"+result[0]+"\nSound:\n\tO:"+soundResult[1]+"X:"+soundResult[0]);


    }
}
