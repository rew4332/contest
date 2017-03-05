package com.example.loveyoplus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

    GridLayout stationBar; // 放置題目站名之區塊(右側)
    RelativeLayout touchMap; // 放置捷運圖之區塊(左側)
    TextView tvTimer; // 計時器區塊

    TextView tvTitle; // 題目描述區塊
    int[] result;
    private Handler mHandler; // 計時物件之執行序
    final int GAMETIME=1000*60;

    // 計時物件
    private Runnable countdowntimer = new Runnable() {
        public void run() {
            new CountDownTimer(GAMETIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTimer.setText("倒數時間:"+new SimpleDateFormat("m").format(millisUntilFinished)+":"+ new SimpleDateFormat("s").format(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    tvTimer.setText("倒數時間:結束");
                    if(remainNum!=1) {
                        Intent intent = new Intent();
                        intent.setClass(Test5Activity.this, Test6Activity.class);
                        startActivity(intent);
                        Test5Activity.this.finish();
                    }
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
        stationBar = (GridLayout) findViewById(R.id.stationBar);
        touchMap = (RelativeLayout) findViewById(R.id.r1);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("找出如下所示之捷運站");


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

        station_name.add("蘆洲");
        station_poses.add(new ArrayList<Float>() {{
            add(99.0f);
            add(52.0f);
        }});

        station_name.add("三民高中");
        station_poses.add(new ArrayList<Float>() {{
            add(130.0f);
            add(85.0f);
        }});

        station_name.add("徐匯中學");
        station_poses.add(new ArrayList<Float>() {{
            add(159.0f);
            add(113.0f);
        }});

        station_name.add("三和國中");
        station_poses.add(new ArrayList<Float>() {{
            add(188.0f);
            add(142.0f);
        }});

        station_name.add("三重國小");
        station_poses.add(new ArrayList<Float>() {{
            add(216.0f);
            add(172.0f);
        }});

        station_name.add("新莊");
        station_poses.add(new ArrayList<Float>() {{
            add(41.0f);
            add(411.0f);
        }});

        station_name.add("頭前庄");
        station_poses.add(new ArrayList<Float>() {{
            add(78.0f);
            add(374.0f);
        }});

        station_name.add("先嗇宮");
        station_poses.add(new ArrayList<Float>() {{
            add(115.0f);
            add(337.0f);
        }});

        station_name.add("三重");
        station_poses.add(new ArrayList<Float>() {{
            add(151.0f);
            add(302.0f);
        }});

        station_name.add("菜寮");
        station_poses.add(new ArrayList<Float>() {{
            add(188.0f);
            add(265.0f);
        }});

        station_name.add("台北橋");
        station_poses.add(new ArrayList<Float>() {{
            add(222.0f);
            add(230.0f);
        }});

        station_name.add("大橋頭");
        station_poses.add(new ArrayList<Float>() {{
            add(299.0f);
            add(203.0f);
        }});

        station_name.add("民權西路");
        station_poses.add(new ArrayList<Float>() {{
            add(362.0f);
            add(203.0f);
        }});

        station_name.add("中山國小");
        station_poses.add(new ArrayList<Float>() {{
            add(446.0f);
            add(204.0f);
        }});

        station_name.add("行天宮");
        station_poses.add(new ArrayList<Float>() {{
            add(474.0f);
            add(239.0f);
        }});

        station_name.add("松江南京");
        station_poses.add(new ArrayList<Float>() {{
            add(475.0f);
            add(284.0f);
        }});

        station_name.add("忠孝新生");
        station_poses.add(new ArrayList<Float>() {{
            add(474.0f);
            add(350.0f);
        }});

        station_name.add("東門");
        station_poses.add(new ArrayList<Float>() {{
            add(466.0f);
            add(444.0f);
        }});

        station_name.add("古亭");
        station_poses.add(new ArrayList<Float>() {{
            add(422.0f);
            add(491.0f);
        }});

        station_name.add("頂溪");
        station_poses.add(new ArrayList<Float>() {{
            add(376.0f);
            add(554.0f);
        }});

        station_name.add("芝山");
        station_poses.add(new ArrayList<Float>() {{
            add(362.0f);
            add(33.0f);
        }});

        station_name.add("士林");
        station_poses.add(new ArrayList<Float>() {{
            add(362.0f);
            add(72.0f);
        }});

        station_name.add("劍潭");
        station_poses.add(new ArrayList<Float>() {{
            add(361.0f);
            add(109.0f);
        }});

        station_name.add("圓山");
        station_poses.add(new ArrayList<Float>() {{
            add(362.0f);
            add(148.0f);
        }});

        station_name.add("雙連");
        station_poses.add(new ArrayList<Float>() {{
            add(362.0f);
            add(240.0f);
        }});

        station_name.add("中山");
        station_poses.add(new ArrayList<Float>() {{
            add(362.0f);
            add(285.0f);
        }});

        station_name.add("台北車站");
        station_poses.add(new ArrayList<Float>() {{
            add(362.0f);
            add(350.0f);
        }});

        station_name.add("臺大醫院");
        station_poses.add(new ArrayList<Float>() {{
            add(361.0f);
            add(384.0f);
        }});

        station_name.add("中正紀念堂");
        station_poses.add(new ArrayList<Float>() {{
            add(385.0f);
            add(441.0f);
        }});

        station_name.add("大安森林公園");
        station_poses.add(new ArrayList<Float>() {{
            add(551.0f);
            add(437.0f);
        }});

        station_name.add("大安");
        station_poses.add(new ArrayList<Float>() {{
            add(606.0f);
            add(436.0f);
        }});

        station_name.add("府中");
        station_poses.add(new ArrayList<Float>() {{
            add(56.0f);
            add(561.0f);
        }});

        station_name.add("板橋");
        station_poses.add(new ArrayList<Float>() {{
            add(90.0f);
            add(527.0f);
        }});

        station_name.add("新埔");
        station_poses.add(new ArrayList<Float>() {{
            add(124.0f);
            add(495.0f);
        }});

        station_name.add("江子翠");
        station_poses.add(new ArrayList<Float>() {{
            add(164.0f);
            add(454.0f);
        }});

        station_name.add("龍山寺");
        station_poses.add(new ArrayList<Float>() {{
            add(226.0f);
            add(436.0f);
        }});

        station_name.add("西門");
        station_poses.add(new ArrayList<Float>() {{
            add(266.0f);
            add(384.0f);
        }});

        station_name.add("善導寺");
        station_poses.add(new ArrayList<Float>() {{
            add(410.0f);
            add(349.0f);
        }});

        station_name.add("忠孝復興");
        station_poses.add(new ArrayList<Float>() {{
            add(606.0f);
            add(350.0f);
        }});

        station_name.add("忠孝敦化");
        station_poses.add(new ArrayList<Float>() {{
            add(673.0f);
            add(350.0f);
        }});

        station_name.add("台電大樓");
        station_poses.add(new ArrayList<Float>() {{
            add(447.0f);
            add(534.0f);
        }});

        station_name.add("小南門");
        station_poses.add(new ArrayList<Float>() {{
            add(314.0f);
            add(415.0f);
        }});

        station_name.add("北門");
        station_poses.add(new ArrayList<Float>() {{
            add(271.0f);
            add(321.0f);
        }});

        station_name.add("南京復興");
        station_poses.add(new ArrayList<Float>() {{
            add(607.0f);
            add(285.0f);
        }});

        station_name.add("台北小巨蛋");
        station_poses.add(new ArrayList<Float>() {{
            add(703.0f);
            add(286.0f);
        }});

        station_name.add("西湖");
        station_poses.add(new ArrayList<Float>() {{
            add(691.0f);
            add(13.0f);
        }});

        station_name.add("劍南路");
        station_poses.add(new ArrayList<Float>() {{
            add(634.0f);
            add(42.0f);
        }});

        station_name.add("大直");
        station_poses.add(new ArrayList<Float>() {{
            add(606.0f);
            add(87.0f);
        }});

        station_name.add("松山機場");
        station_poses.add(new ArrayList<Float>() {{
            add(624.0f);
            add(134.0f);
        }});

        station_name.add("中山國中");
        station_poses.add(new ArrayList<Float>() {{
            add(605.0f);
            add(213.0f);
        }});

        station_name.add("科技大樓");
        station_poses.add(new ArrayList<Float>() {{
            add(606.0f);
            add(474.0f);
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

    protected void BindEvent(final int id, final RelativeLayout touchMap, final GridLayout displayBar) {
        String stationName = this.stationNameList.get(id);
        float stationPostionX = this.stationPositionList.get(id).get(0);
        float stationPostionY = this.stationPositionList.get(id).get(1);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        TextView stationNameText = new TextView(this);
        stationNameText.setText(stationName);
        stationNameText.setTextSize(getResources().getDimension(R.dimen.q_font_size)-20);
        stationNameText.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        stationNameText.setMinimumWidth(displayMetrics.widthPixels/3);
        stationNameText.setId(100 + id);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        stationNameText.setLayoutParams(params);

        displayBar.addView(stationNameText);




        int width =(displayMetrics.widthPixels);// rl1.getMeasuredWidth();
        int height = 3*displayMetrics.heightPixels/5;//rl1.getMeasuredHeight();
        int location[] = new int[2];
        //Log.d("location",getRelativeLeft(iv[0])+"");


        final int shiftY=(int)(height-(width*569/713.0f))/2;
        final float ratio = width/713.0f;

        //RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageView stationListener = new ImageView(this);
        stationListener.setMinimumWidth(40);
        stationListener.setMinimumHeight(40);
        stationListener.setImageResource(R.drawable.dot);
        stationListener.setAlpha(0.2f);
        stationListener.setId(200 + id);
        stationListener.setX(stationPostionX*ratio - 20);
        stationListener.setY(shiftY+stationPostionY*ratio - 20);
        stationListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             displayBar.removeView(findViewById(100 + id));


                touchMap.removeView(findViewById(200 + id));
                result[1]++;
                remainNum --;
                if(remainNum==1){
                    Log.d("end","");
                    Intent intent = new Intent();
                    intent.setClass(Test5Activity.this, Test6Activity.class);
                    startActivity(intent);
                    Test5Activity.this.finish();
                }

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
