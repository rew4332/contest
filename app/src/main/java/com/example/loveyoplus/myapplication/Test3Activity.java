package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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

public class Test3Activity extends AppCompatActivity implements View.OnTouchListener {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Handler mHandler;
    String sTemp="";
    RelativeLayout r1;
    ImageView iv[];
    int[][] tag;
    int[] result;
    String answer[];
    TextView tvTimer,tvTitle;
    final int GAMETIME=1000*1;//遊戲時間


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



        //抓取資源tag[x][0]=獲取圖片id
        tag = new int[9][2];
        //t3_1~t3_9
        for(int i = 0;i<9;i++){
            tag[i][0] = getResources().getIdentifier("t3_"+(i+1),"drawable",getPackageName());
        }
        //作答後結果
        result = new int[2];

        //answer tag[x][1]=可作答數量 每touch一次 tag[x][1]--
        answer = new String[9];
        answer[0]="259,229;18,398;309,546;385,443";tag[0][1]=4;
        answer[1]="21,442;218,600";tag[1][1]=2;
        answer[2]="82,292;166,122;358,97;359,229";tag[2][1]=4;
        answer[3]="81,288;368,225;365,543";tag[3][1]=3;
        answer[4]="111,165;240,429;281,577";tag[4][1]=3;
        answer[5]="285,409;400,253;35,459";tag[5][1]=3;
        answer[6]="307,647;162,158";tag[6][1]=2;
        answer[7]="31,235;92,430;297,460";tag[7][1]=3;
        answer[8]="404,253;343,357;36,366";tag[8][1]=3;

        mHandler = new Handler();
        mHandler.post(countdowntimer);

        tvTitle.setText("選取不同處(選左圖)");
        iv[0].setImageResource(tag[0][0]);
        iv[0].setTag(tag[0][0]);
        iv[0].setOnTouchListener(this);



    }
    private Runnable countdowntimer = new Runnable() {
        public void run() {
            new CountDownTimer(GAMETIME, 1000) {

                @Override

                public void onTick(long millisUntilFinished) {
                    //倒數秒數中要做的事

                    tvTimer.setText("倒數時間:"+new SimpleDateFormat("m").format(millisUntilFinished)+":"+ new SimpleDateFormat("s").format(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    tvTimer.setText("倒數時間:結束");
                    //iv[0].setVisibility(View.INVISIBLE);
                    Log.e("answerXD:",sTemp);
                    Intent intent = new Intent();
                    intent.setClass(Test3Activity.this, Test4Activity.class);
                    startActivity(intent);
                    finish();

                }
            }.start();

        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {

            result[0]++;

            int[] viewCoords = new int[2];
            int touchX = (int) event.getX();
            int touchY = (int) event.getY();
            int imageX ; // viewCoords[0] is the X coordinate
            int imageY ; // viewCoords[1] is the y coordinate

            //Log.e("resource");
            iv[0].getLocationOnScreen(viewCoords);
            imageX = touchX - viewCoords[0];
            imageY = touchY - viewCoords[1];
            //Log.v("Touch x >>>",touchX+"");
            //Log.v("Touch y >>>",touchY+"\n");
            Log.v("Image x >>>",imageX+"");
            Log.v("Image y >>>",imageY+"\n");
            sTemp+=imageX+","+imageY+";";

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            ImageView ivX = new ImageView(this);

            //圈選大小
            params.width = 200;
            params.height = 200;
            ivX = new ImageView(this.getApplicationContext());
            ivX.setImageResource(R.drawable.circle);
            //圈圈位置


            for(int i = 0 ;i<9;i++) {
                //Log.d("\tv.getTag", v.getTag() + "");
                //Log.d("\ttag", tag[i][0] + "");
                if (v.getTag().equals(tag[i][0])) {
                    tag[i][1]--;
                    //判斷是否為答案IsAnswer((int)第幾題,x,y)
                    int p[] = IsAnswer(i, imageX, imageY);
                    if (p != null) {
                        //Log.e("p", p[0] + " " + p[1]);
                        params.setMargins(p[0]-100 , p[1]-100 , 0, 0);
                        ivX.setLayoutParams(params);
                        r1.addView(ivX);
                        result[1]++;
                        result[0]--;

                    }
                    //if作答次數歸零 換下一題refresh UI
                    if (tag[i][1] == 0) {

                        ((ViewGroup)iv[0].getParent()).removeAllViews();
                        if(i<8) {
                            iv[0].setImageResource(tag[i + 1][0]);
                            iv[0].setTag(tag[i + 1][0]);
                            r1.addView(iv[0]);
                        }
                        else{
                            Intent intent = new Intent();
                            intent.setClass(Test3Activity.this, Test4Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    }
                }
            }
        ((TextView)findViewById(R.id.tv2)).setText("O:"+result[1]+"\nX:"+result[0]);
        Log.e("answerXD:",sTemp);
        return false;
    }
    //
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
