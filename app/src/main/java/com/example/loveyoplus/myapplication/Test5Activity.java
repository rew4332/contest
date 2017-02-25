package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.os.Handler;

/**
 * Created by loveyoplus on 2017/2/24.
 */

public class Test5Activity extends AppCompatActivity implements View.OnTouchListener {
    private ArrayList<String> stationNameList; // 放置題目站名之區塊(右側)
    private ArrayList<ArrayList<Float>> stationPositionList; // 放置捷運圖之區塊(左側)
    public int remainNum; // 未被許取之站點數量

    LinearLayout stationBar; // 放置題目站名之區塊(右側)
    RelativeLayout touchMap; // 放置捷運圖之區塊(左側)
    TextView tvTimer; // 計時器區塊

    TextView tvTitle; // 題目描述區塊
    int[] result;
    private Handler mHandler; // 計時物件之執行序

    // 計時物件
    private Runnable countdowntimer = new Runnable() {
        public void run() {
            new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTimer.setText("倒數時間:"+new SimpleDateFormat("m").format(millisUntilFinished)+":"+ new SimpleDateFormat("s").format(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    tvTimer.setText("倒數時間:結束");Intent intent = new Intent();
                    intent.setClass(Test5Activity.this, Test6Activity.class);
                    startActivity(intent);
                    finish();

                }
            }.start();
        }
    };


    private final int QUESTION_NUM = 15; // 題目要求之站數
    String s="";
    ImageView stationListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t5);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        stationListener    = new ImageView(this);

        result = new int[2];
        // Initialization of Env Paramater
        this.GenStationList();
        stationBar = (LinearLayout) findViewById(R.id.stationBar);
        touchMap = (RelativeLayout) findViewById(R.id.r1);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvTitle = (TextView) findViewById(R.id.tvTitle);


        touchMap.setOnTouchListener(this);
        // Initialization of Layout
        List<Integer> stationIds = this.PickUpStationIds(QUESTION_NUM);
        remainNum = QUESTION_NUM;
        for (int id: stationIds) {
            this.BindEvent(id, touchMap, stationBar);
        }

        // Timing
        mHandler = new Handler();
        mHandler.post(countdowntimer);

    }

    protected void GenStationList() {
        ArrayList<String> station_name = new ArrayList<>();
        ArrayList<ArrayList<Float>> station_poses = new ArrayList<>();

        // 紅線
        station_name.add("圓山");
        station_poses.add(new ArrayList<Float>() {{
            add(170.0f);
            add(128.0f);

        }});

        station_name.add("民權西路");
        station_poses.add(new ArrayList<Float>() {{
            add(170.0f);
            add(192.0f);
        }});

        station_name.add("雙連");
        station_poses.add(new ArrayList<Float>() {{
            add(170.0f);
            add(249.0f);
        }});

        station_name.add("中山");
        station_poses.add(new ArrayList<Float>() {{
            add(170.0f);
            add(311.0f);
        }});

        station_name.add("台北車站");
        station_poses.add(new ArrayList<Float>() {{
            add(170.0f);
            add(384.0f);
        }});

        station_name.add("台大醫院");
        station_poses.add(new ArrayList<Float>() {{
            add(170.0f);
            add(426.0f);
        }});

        station_name.add("中正紀念堂");
        station_poses.add(new ArrayList<Float>() {{
            add(202.0f);
            add(471.0f);
        }});

        station_name.add("東門");
        station_poses.add(new ArrayList<Float>() {{
            add(281.0f);
            add(471.0f);
        }});

        station_name.add("大安森林公園");
        station_poses.add(new ArrayList<Float>() {{
            add(429.0f);
            add(471.0f);
        }});

        station_name.add("大安");
        station_poses.add(new ArrayList<Float>() {{
            add(513.0f);
            add(473.0f);
        }});

        station_name.add("信義安和");
        station_poses.add(new ArrayList<Float>() {{
            add(653.0f);
            add(471.0f);
        }});

        station_name.add("台北101/世貿");
        station_poses.add(new ArrayList<Float>() {{
            add(787.0f);
            add(471.0f);
        }});

        // 橘線
        station_name.add("大橋頭");
        station_poses.add(new ArrayList<Float>() {{
            add(84.0f);
            add(187.0f);
        }});

        station_name.add("中山國小");
        station_poses.add(new ArrayList<Float>() {{
            add(304.0f);
            add(188.0f);
        }});

        station_name.add("行天宮");
        station_poses.add(new ArrayList<Float>() {{
            add(364.0f);
            add(249.0f);
        }});

        station_name.add("頂溪");
        station_poses.add(new ArrayList<Float>() {{
            add(190.0f);
            add(561.0f);
        }});

        station_name.add("永安市場");
        station_poses.add(new ArrayList<Float>() {{
            add(151.0f);
            add(596.0f);
        }});


        // 咖啡線
        station_name.add("松山機場");
        station_poses.add(new ArrayList<Float>() {{
            add(512.0f);
            add(135.0f);
        }});

        station_name.add("中山國中");
        station_poses.add(new ArrayList<Float>() {{
            add(512.0f);
            add(222.0f);
        }});

        station_name.add("科技大樓");
        station_poses.add(new ArrayList<Float>() {{
            add(513.0f);
            add(516.0f);
        }});

        station_name.add("六張犁");
        station_poses.add(new ArrayList<Float>() {{
            add(597.0f);
            add(562.0f);
        }});

        station_name.add("麟光");
        station_poses.add(new ArrayList<Float>() {{
            add(638.0f);
            add(604.0f);
        }});

        // 綠線
        station_name.add("南京三民");
        station_poses.add(new ArrayList<Float>() {{
            add(806.0f);
            add(306.0f);
        }});

        station_name.add("台北小巨蛋");
        station_poses.add(new ArrayList<Float>() {{
            add(676.0f);
            add(306.0f);
        }});

        station_name.add("南京復興");
        station_poses.add(new ArrayList<Float>() {{
            add(512.0f);
            add(310.0f);
        }});

        station_name.add("松江南京");
        station_poses.add(new ArrayList<Float>() {{
            add(364.0f);
            add(308.0f);
        }});

        station_name.add("北門");
        station_poses.add(new ArrayList<Float>() {{
            add(78.0f);
            add(334.0f);
        }});

        station_name.add("西門");
        station_poses.add(new ArrayList<Float>() {{
            add(77.0f);
            add(426.0f);
        }});

        station_name.add("小南門");
        station_poses.add(new ArrayList<Float>() {{
            add(114.0f);
            add(464.0f);
        }});

        station_name.add("古亭");
        station_poses.add(new ArrayList<Float>() {{
            add(240.0f);
            add(507.0f);
        }});

        station_name.add("台電大樓");
        station_poses.add(new ArrayList<Float>() {{
            add(279.0f);
            add(545.0f);
        }});

        station_name.add("公館");
        station_poses.add(new ArrayList<Float>() {{
            add(309.0f);
            add(579.0f);
        }});

        station_name.add("萬隆");
        station_poses.add(new ArrayList<Float>() {{
            add(347.0f);
            add(613.0f);
        }});






        // 藍線
        station_name.add("市政府");
        station_poses.add(new ArrayList<Float>() {{
            add(779.0f);
            add(383.0f);
        }});

        station_name.add("國父紀念館");
        station_poses.add(new ArrayList<Float>() {{
            add(691.0f);
            add(382.0f);
        }});

        station_name.add("忠孝敦化");
        station_poses.add(new ArrayList<Float>() {{
            add(598.0f);
            add(381.0f);
        }});

        station_name.add("忠孝復興");
        station_poses.add(new ArrayList<Float>() {{
            add(512.0f);
            add(383.0f);
        }});

        station_name.add("忠孝新生");
        station_poses.add(new ArrayList<Float>() {{
            add(363.0f);
            add(381.0f);
        }});

        station_name.add("善導寺");
        station_poses.add(new ArrayList<Float>() {{
            add(273.0f);
            add(381.0f);
        }});


        this.stationNameList = station_name;
        this.stationPositionList = station_poses;
    }

    protected List PickUpStationIds(int number) {
        int stationNum = this.stationNameList.size();

        ArrayList<Integer> idList = new ArrayList<Integer>();
        int i;
        for (i = 0; i < stationNum; ++ i) {
            idList.add(new Integer(i));
        }

        Collections.shuffle(idList);

        return idList.subList(0, number - 1);
    }

    protected void BindEvent(final int id, final RelativeLayout touchMap, final LinearLayout displayBar) {
        String stationName = this.stationNameList.get(id);
        float stationPostionX = this.stationPositionList.get(id).get(0);
        float stationPostionY = this.stationPositionList.get(id).get(1);

        TextView stationNameText = new TextView(this);
        stationNameText.setText(stationName);
        stationNameText.setId(100 + id);
        displayBar.addView(stationNameText);

        //RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageView stationListener = new ImageView(this);
        stationListener.setMinimumWidth(40);
        stationListener.setMinimumHeight(40);
        stationListener.setImageResource(R.drawable.dot);
        stationListener.setAlpha(0.2f);
        stationListener.setId(200 + id);
        stationListener.setX(stationPostionX - 20);
        stationListener.setY(stationPostionY - 20);
        stationListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayBar.removeView(findViewById(100 + id));
                touchMap.removeView(findViewById(200 + id));

                remainNum --;
            }
        });
        touchMap.addView(stationListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
/*
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        int imageX = touchX ;
        int imageY = touchY ;
        stationListener.setMinimumWidth(40);
        stationListener.setMinimumHeight(40);
        stationListener.setImageResource(R.drawable.dot);
        stationListener.setAlpha(0.0f);

        stationListener.setX(touchX+20);
        stationListener.setY(touchY+20);




        Log.v("Image x >>>",imageX+"");
        Log.v("Image y >>>",imageY+"\n");
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            ((RelativeLayout)v).removeView(stationListener);


            ((RelativeLayout)v).addView(stationListener);
        }
        if(event.getAction()==MotionEvent.ACTION_UP) {
            s += (imageX+40) + "," + (imageY+40) + ";";
            Log.e("x,y;", s + "");
        }
*/
        return true;

    }
}
