package com.example.loveyoplus.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by loveyoplus on 2017/2/16.
 */

public class Test3Activity extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Handler mHandler;
    //String sTemp="";
    RelativeLayout r1;
    ImageView iv[];
    String[] tag;
    int[] result;
    String answer[];
    TextView tvTimer,tvTitle;
    final int GAMETIME=1000*60;//遊戲時間
    final int QUESTIONNUM=10;
    int destroyRunnable=0;
    int restQuestionNum;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t3);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態


        r1 = (RelativeLayout)findViewById(R.id.r1);
        tvTimer = (TextView)findViewById(R.id.tv1);
        tvTitle = (TextView)findViewById(R.id.tv3);
        iv =new ImageView[2];
        iv[0] = (ImageView) findViewById(R.id.iv1) ;
        iv[1]  = (ImageView)findViewById(R.id.iv2) ;



        //抓取資源tag[x]=獲取圖片id
        tag = new String[QUESTIONNUM];
        //t3_1~t3_9
        for(int i = 0;i<QUESTIONNUM;i++){
            tag[i] = "t3_"+(i+1);
        }
        //作答後結果
        result = new int[2];


        answer = new String[QUESTIONNUM];
        answer[0]="3451,0,776,356;2693,1840,200,200;1996,1836,676,672;3981,2028,364,840;0,1772,400,600";
        answer[1]="422,1058,181,262;2552,877,181,277;3350,901,173,439;0,0,1197,601;1870,1699,397,765";
        answer[2]="651,1814,167,267;2115,1667,225,199;1171,1741,143,97;2977,1843,625,763;0,2655,361,457";
        answer[3]="1035,1881,431,609;2415,1993,129,243;415,1201,1525,437;2103,973,621,757;4189,0,865,297";
        answer[4]="1077,1137,269,311;733,1231,133,121;3094,1843,297,303;3420,1585,307,291;0,2457,1285,837";
        answer[5]="431,317,599,307;2406,1749,305,531;2838,1585,123,769;4289,1427,115,711;4633,1107,239,265";
        answer[6]="2313,287,341,189;0,0,1549,765;0,2591,833,321;2903,2149,161,265;2775,2905,713,565";
        answer[7]="1221,1797,755,353;125,2897,525,223;4197,2893,177,417;3225,665,1593,393;3773,2231,163,257";
        answer[8]="1933,431,249,717;3052,1853,205,319;3020,2705,675,311;3776,323,249,267;4303,387,141,769";
        answer[9]="1641,1279,179,255;1783,583,1301,425;2981,1111,111,637;3318,2243,215,477;3583,1473,533,1653";



        mHandler = new Handler();
        mHandler.post(countdowntimer);

        tvTitle.setText("選取不同處(選左圖)");
        Log.d("drawableTag",tag[0]);

        restQuestionNum=5;//剩下的答案數量
        //binListener 每題有五個答案
        for(int setId= 0;setId<5;setId++) {
            //bind the first question
            bindListener(0,setId);
        }

    }
    private Runnable countdowntimer = new Runnable() {
        public void run() {
            new CountDownTimer(GAMETIME, 1000) {

                @Override

                public void onTick(long millisUntilFinished) {
                    //倒數秒數中要做的事

                    tvTimer.setText("倒數時間:"+new SimpleDateFormat("m").format(millisUntilFinished)+":"+ new SimpleDateFormat("s").format(millisUntilFinished));
                    Log.e("還在倒數",mHandler.obtainMessage()+"qaq");

                }

                @Override
                public void onFinish() {
                    tvTimer.setText("倒數時間:結束");
                    //iv[0].setVisibility(View.INVISIBLE);
                    //Log.e("answerXD:",sTemp);
                    if(destroyRunnable==0) {
                        Intent intent = new Intent();
                        intent.setClass(Test3Activity.this, Test4Activity.class);
                        startActivity(intent);
                        Test3Activity.this.finish();
                    }
                }
            }.start();

        }
    };

    public void bindListener(final int index,final int id){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        iv[0].setImageResource(getResources().getIdentifier(tag[index],"drawable",getPackageName()));
        iv[1].setImageResource(getResources().getIdentifier(tag[index]+"a","drawable",getPackageName()));

        int width =(displayMetrics.widthPixels);// rl1.getMeasuredWidth();
        int height = 3*displayMetrics.heightPixels/8;//rl1.getMeasuredHeight();
        int location[] = new int[2];
        //Log.d("location",getRelativeLeft(iv[0])+"");
        iv[0].measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        final int shiftX=(int)(width-(height*4918/3264.0f))/2;




        String a[]=answer[index].split(";");
        final float ratio = height/3264.0f;
        final String a2[] = a[id].split(",");
        ImageView iv =  new ImageView(this);

        iv.setImageResource(R.drawable.dot);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setX(shiftX+Float.parseFloat(a2[0])*ratio);
        iv.setY(Float.parseFloat(a2[1])*ratio);

        iv.setMinimumWidth((int)(Integer.parseInt(a2[2])*ratio));
        iv.setMinimumHeight((int)(Integer.parseInt(a2[3])*ratio));

        iv.setAlpha(0.0f);
        iv.setId(100+id);

        r1.addView(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //r1.removeView(v);
                int w=(int)(Integer.parseInt(a2[2])*ratio);
                int h=(int)(Integer.parseInt(a2[3])*ratio);
                ((ImageView) v).setImageResource(R.drawable.circle2);
                v.setAlpha(1.0f);
                v.getLayoutParams().width=w;
                v.getLayoutParams().height=h;
                Log.d("work","work");
                v.setOnClickListener(null);

                //剩餘答案-1
                restQuestionNum--;
                //記錄對的數量
                result[1]++;
                Log.d("restQuestionNum",restQuestionNum+"");

                if(restQuestionNum==0){


                    //若是最後一題
                    if(index==QUESTIONNUM-1){
                        //跳下個測驗
                        Intent intent = new Intent();
                        intent.setClass(Test3Activity.this, Test4Activity.class);
                        startActivity(intent);
                        Test3Activity.this.finish();
                    }
                    else{

                        r1.removeViewAt(1);r1.removeViewAt(1);r1.removeViewAt(1);r1.removeViewAt(1);r1.removeViewAt(1);
                        //跳下一題
                        for (int setId = 0; setId < 5; setId++) {

                            //bind the first question
                            restQuestionNum = 5;
                            bindListener(index + 1, setId);
                        }
                    }




                }



            }
        });



    }







    public int[] IsAnswer(int answerIndex,int x,int y){
        String a[]=answer[answerIndex].split(";");
        String position=x+","+y;
        for(int i=0;i<a.length;i++){
            int tx = Integer.parseInt(a[i].split(",")[0]);
            int ty = Integer.parseInt(a[i].split(",")[1]);
            if(x<tx+100&&x>tx-100&&y<ty+100&&y>ty-100){
                a[i].replace(position,"");
                int[] p = {tx,ty};
                return p;
            }
        }
        return null;

    }

}
