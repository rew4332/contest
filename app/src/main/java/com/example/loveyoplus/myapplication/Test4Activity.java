package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by loveyoplus on 2017/2/17.
 */

public class Test4Activity extends AppCompatActivity implements View.OnClickListener {
    TableLayout tl1;
    Button[] btn;
    String[] answer;
    TextView tv1,tv2,timer;
    private Handler mHandler;
    int[] result;
    int randomNumLen=56;//1~56的數字
    final int GAMETIME=1000*60;//遊戲時間

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t4);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態

        tv2 = (TextView)findViewById(R.id.tv2);
        tv1 = (TextView)findViewById(R.id.tv1);
        timer= (TextView)findViewById(R.id.tv3);
        tl1 = (TableLayout) findViewById(R.id.tl1);
        btn = new Button[100];
        result = new int[2];


        //排版區
        //row params
        //TableRow.LayoutParams
        TableLayout.LayoutParams trp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);
        trp.weight=1;
        //btn params in row
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.weight=1;
        for(int i =0;i<7;i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(trp);
            for(int j=0;j<8;j++){
                btn[i*8+j] = new Button(this);
                btn[i*8+j].setOnClickListener(this);
                btn[i*8+j].setLayoutParams(lp);
                btn[i*8+j].setText((i*8+j+1)+"");
                btn[i*8+j].setTextSize(50);
                tr.addView(btn[i*8+j]);
            }

            tl1.addView(tr);

        }
        setBtn();

        //程式區

        setQuestion();
        mHandler = new Handler();
        mHandler.post(countdowntimer);



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

                    Intent intent = new Intent();
                    intent.setClass(Test4Activity.this, Test5Activity.class);
                    startActivity(intent);

                    Test4Activity.this.finish();

                }
            }.start();

        }
    };
    public void disableBtn(){
        for(int i = 0 ;i<randomNumLen;i++){
            btn[i].setVisibility(View.INVISIBLE);
        }
    }
    public void setBtn(){


        String randomNum[]={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56"};
        for(int i = 0 ;i<randomNumLen;i++){
            btn[i].setVisibility(View.VISIBLE);
            Random random = new Random();
            int selected= random.nextInt(randomNumLen-i);
            btn[i].setText(randomNum[selected]+"");
            randomNum[selected]=randomNum[randomNumLen-1-i];

        }


    }

    public void setQuestion(){
        String randomNum[]={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56"};
        answer = new String[15];
        tv1.setText("限時時間內找出數字:");
        String s="";
        int count=1;
        for(int i = 0 ;i<15;i++){
            Random random = new Random();
            int selected= random.nextInt(randomNumLen-i);
            answer[i]=randomNum[selected];
            randomNum[selected]=randomNum[randomNumLen-1-i];
            if(Integer.parseInt(answer[i])<10)s+="  ";
            s+=answer[i];
            if(count%5==0)s+="\n";
            else s+="\t\t\t\t";
            count++;

        }
        tv2.setText(s);
    }

    @Override
    public void onClick(View v) {
        for(int i=0;i<randomNumLen;i++) {
            if ( v==btn[i]){
                btn[i].setVisibility(View.INVISIBLE);
                result[0]++;
                for(int j=0;j<15;j++) {
                    if (answer[j].equals(btn[i].getText().toString())) {
                        result[1]++;
                        result[0]--;
                        answer[j]="";
                    }

                }
                if ((result[0]+result[1])%15==0){
                    setBtn();
                    setQuestion();
                }
                String s="";
                int count=1;
                for(int j=0;j<15;j++){
                    if(!answer[j].equals("")){
                        if(Integer.parseInt(answer[j])<10)s+="  ";
                        s+=answer[j];
                        if(count%5==0)s+="\n";
                        else s+="\t\t\t\t";
                        count++;
                    }
                }
                tv2.setText(s);
                Log.v("result","O:"+result[1]+"\nX:"+result[0]);
            }
        }
    }
}
