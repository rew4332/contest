package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by loveyoplus on 2017/2/17.
 */

public class Test6Activity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rl[],rl1;
    ImageView iv[][];
    TextView tv[],timer;

    ImageView answerIv;
    int[] result;
    private Handler mHandler;
    int randomNumLen=37;
    int GAMETIME=1000*5;//遊戲時間
    String ID="";
    String startDateandTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t6);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        startDateandTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        ID=getIntent().getStringExtra("ID");


        rl= new RelativeLayout[9];
        iv = new ImageView[9][3];
        tv = new TextView[3];
        result = new int[2];
        tv[0] =(TextView) findViewById(R.id.textView2);
        tv[1] =(TextView) findViewById(R.id.textView6);

        tv[0].setText("找出下面所顯示之注音符號");
        timer = tv[1];
        answerIv= (ImageView) findViewById(R.id.imageView);
        rl1 = (RelativeLayout)findViewById(R.id.rl);

        GAMETIME = loadSetting(6);
        mHandler = new Handler();
        mHandler.post(startCountdowntimer);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageView tempiv =  new ImageView(Test6Activity.this);
        tempiv.setScaleType(ImageView.ScaleType.FIT_XY);
        tempiv.setBackgroundColor(Color.WHITE);
        tempiv.setLayoutParams(rlp);
        rl1.addView(tempiv);






    }
    int loadSetting(int i){
        fileStorage fs = new fileStorage();
        fs.createFile("setting");
        fs.setContinueWrite(false);
        String s= fs.readFile("setting");
        if(s==null)return 60*1000;
        return Integer.parseInt(s.split("\r\n")[i])*1000;
    }
    private Runnable startCountdowntimer = new Runnable() {
        public void run() {
            final RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

            rl1.addView(new ImageView(Test6Activity.this));
            //rl1.addView(tempiv);
            new CountDownTimer(5000, 100) {

                @Override

                public void onTick(long millisUntilFinished) {
                    ImageView tempiv = new ImageView(Test6Activity.this);
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
                    for(int i=0;i<9;i++) {
                        rl[i] = (RelativeLayout) findViewById(getResources().getIdentifier("rl" + (i + 1), "id", getPackageName()));
                        rl[i].setOnClickListener(Test6Activity.this);
                        iv[i][0] = (ImageView) findViewById(getResources().getIdentifier("iv" + (i + 1), "id", getPackageName()));
                        iv[i][1] = new ImageView(Test6Activity.this);
                        iv[i][2] = new ImageView(Test6Activity.this);
                        rl[i].addView(iv[i][1]);
                        rl[i].addView(iv[i][2]);
                    }
                    setImage();
                    setQuestion();

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
                    for(int i=0;i<9;i++) {
                        rl[i].setVisibility(View.INVISIBLE);
                    }

                    fileStorage fs = new fileStorage();
                    String endDateandTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String content = "test6:\r\n"+startDateandTime+";"+endDateandTime+";true:"+result[1]+";false:"+result[0]+"\r\n";
                    fs.writeFile(ID,content);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",ID);
                    bundle.putString("ActivityName",Test6Activity.this.getClass().getSimpleName().toString());
                    intent.putExtras(bundle);
                    intent.setClass(Test6Activity.this, RedirectActivity.class);
                    startActivity(intent);
                    Test6Activity.this.finish();

                }
            }.start();

        }
    };

    public boolean isAnswer(RelativeLayout r){
        for(int i= 0 ;i<9;i++){
            if(r==rl[i]&&i==(int)answerIv.getTag()){
                Log.e("isAnswer","true");
                return true;

            }
        }
        Log.e("isAnswer","false");
        return false;
    }
    public void setQuestion(){
        Random random = new Random();
        int selected=random.nextInt(9*3);
        Object o =iv[(int)(selected/3)][selected%3].getTag();
        answerIv.setTag((int)selected/3);
        answerIv.setImageResource((int)o);
    }
    public void setImage(){
        String randomNum[]={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37"};

        for(int i = 0 ;i<9;i++){
            for(int j =0;j<3;j++){
                Random random = new Random();
                int selected= random.nextInt(randomNumLen-(i*3+j));
                Object o = getResources().getIdentifier("t6_"+randomNum[selected],"drawable",getPackageName());
                iv[i][j].setImageResource((Integer) o);
                iv[i][j].setTag(o);

                randomNum[selected]=randomNum[randomNumLen-1-(i*3+j)];
            }


        }
    }

    @Override
    public void onClick(View v) {

        if(isAnswer((RelativeLayout) v)) {
            result[1]++;
        }
        else
            result[0]++;
        Log.e("result","O:"+result[1]+"X:"+result[0]);
        setImage();
        setQuestion();


    }
}
