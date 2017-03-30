package com.example.loveyoplus.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by loveyoplus on 2017/3/14.
 */

public class RedirectActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    String ID;
    String description[][];
    String ActivityName;
    TextView tvDes1,tvDes2;
    Button btn;
    PagerTabStrip pagerTabStrip;
    int[] mResources ;
    private int[] tab={
            R.drawable.focus_tab,
            R.drawable.unfocus_tab
    };
    private ImageView[] tips;
    private ImageView[] mImageViews;
    PagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle_page);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        ID=getIntent().getExtras().getString("ID");
        ActivityName=getIntent().getExtras().getString("ActivityName");
        Log.e("ActivityName",ActivityName);

        initView();
        //initDescription();
        //showDescription(ActivityName);
        initImageView(ActivityName);









    }
    void initImageView(String Name) {
        switch (Name) {
            case "MainActivity":
                for (int i = 0; i > -1; i++) {
                    if (getResources().getIdentifier("t1_" + (i + 1) + "d", "drawable", getPackageName()) == 0) {
                        mResources = new int[i];
                        for (int j = 0; j < i ; j++)
                            mResources[j] = getResources().getIdentifier("t1_" + (j + 1) + "d", "drawable", getPackageName());
                        break;
                    }
                }

                break;
            case "Test1Activity":

                for (int i = 0; i > -1; i++) {
                    if (getResources().getIdentifier("t2_" + (i + 1) + "d", "drawable", getPackageName()) == 0) {
                        mResources = new int[i ];
                        for (int j = 0; j < i ; j++)
                            mResources[j] = getResources().getIdentifier("t2_" + (j + 1) + "d", "drawable", getPackageName());
                        break;
                    }
                }

                break;
            case "Test2Activity":
                for (int i = 0; i > -1; i++) {
                    if (getResources().getIdentifier("t3_" + (i + 1) + "d", "drawable", getPackageName()) == 0) {
                        mResources = new int[i ];
                        for (int j = 0; j < i ; j++)
                            mResources[j] = getResources().getIdentifier("t3_" + (j + 1) + "d", "drawable", getPackageName());
                        break;
                    }
                }

                break;
            case "Test3Activity":
                for (int i = 0; i > -1; i++) {
                    if (getResources().getIdentifier("t4_" + (i + 1) + "d", "drawable", getPackageName()) == 0) {
                        mResources = new int[i ];
                        for (int j = 0; j < i ; j++)
                            mResources[j] = getResources().getIdentifier("t4_" + (j + 1) + "d", "drawable", getPackageName());
                        break;
                    }
                }

                break;
            case "Test4Activity":
                for (int i = 0; i > -1; i++) {
                    if (getResources().getIdentifier("t5_" + (i + 1) + "d", "drawable", getPackageName()) == 0) {
                        mResources = new int[i ];
                        for (int j = 0; j < i ; j++)
                            mResources[j] = getResources().getIdentifier("t5_" + (j + 1) + "d", "drawable", getPackageName());
                        break;
                    }
                }

                break;
            case "Test5Activity":
                for (int i = 0; i > -1; i++) {
                    if (getResources().getIdentifier("t6_" + (i + 1) + "d", "drawable", getPackageName()) == 0) {
                        mResources = new int[i ];
                        for (int j = 0; j < i ; j++)
                            mResources[j] = getResources().getIdentifier("t6_" + (j + 1) + "d", "drawable", getPackageName());
                        break;
                    }
                }

                break;
            case "Test6Activity":
                for (int i = 0; i > -1; i++) {
                    if (getResources().getIdentifier("t7_" + (i + 1) + "d", "drawable", getPackageName()) == 0) {
                        mResources = new int[i ];
                        for (int j = 0; j < i ; j++)
                            mResources[j] = getResources().getIdentifier("t7_" + (j + 1) + "d", "drawable", getPackageName());
                        break;
                    }
                }

                break;
            case "Test7Activity":
                for (int i = 0; i > -1; i++) {
                    if (getResources().getIdentifier("t8_" + (i + 1) + "d", "drawable", getPackageName()) == 0) {
                        mResources = new int[i ];
                        for (int j = 0; j < i ; j++)
                            mResources[j] = getResources().getIdentifier("t8_" + (j + 1) + "d", "drawable", getPackageName());
                        break;
                    }
                }

                break;
            case "Test8Activity":
                for (int i = 0; i > -1; i++) {
                    if (getResources().getIdentifier("t9_" + (i + 1) + "d", "drawable", getPackageName()) == 0) {
                        mResources = new int[i ];
                        for (int j = 0; j < i ; j++)
                            mResources[j] = getResources().getIdentifier("t9_" + (j + 1) + "d", "drawable", getPackageName());
                        break;
                    }
                }

                break;
            case "Test9Activity":
                for (int i = 0; i > -1; i++) {
                    if (getResources().getIdentifier("t10_" + (i + 1) + "d", "drawable", getPackageName()) == 0) {
                        mResources = new int[i ];
                        for (int j = 0; j < i ; j++)
                            mResources[j] = getResources().getIdentifier("t10_" + (j + 1) + "d", "drawable", getPackageName());
                        break;
                    }
                }

                break;
        }
        LinearLayout group = (LinearLayout) findViewById(R.id.viewGroup);

        tips = new ImageView[mResources.length];

        for (int i = 0; i < tips.length; i++) {

            ImageView imageView = new ImageView(this);

            LinearLayout.LayoutParams llp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            llp.width=15;
            llp.setMargins(20,0,20,0);


            imageView.setLayoutParams(llp);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


            tips[i] = imageView;

            if (i == 0) {

                tips[i].setBackgroundResource(R.drawable.focus_tab);

            } else {

                tips[i].setBackgroundResource(R.drawable.unfocus_tab);

            }

            group.addView(tips[i]);
        }

        mCustomPagerAdapter = new CustomPagerAdapter(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setOnPageChangeListener(RedirectActivity.this);
        //设置Adapter




    }


    void redirect(String Name){
        Intent intent = new Intent();
        intent.putExtra("ID",ID);


        switch (Name){
            case "MainActivity":
                intent.setClass(RedirectActivity.this, Test1Activity.class);
                startActivity(intent);
                break;
            case "Test1Activity":
                intent.setClass(RedirectActivity.this, Test2Activity.class);
                startActivity(intent);
                break;
            case "Test2Activity":
                intent.setClass(RedirectActivity.this, Test3Activity.class);
                startActivity(intent);
                break;
            case "Test3Activity":
                intent.setClass(RedirectActivity.this, Test4Activity.class);
                startActivity(intent);
                break;
            case "Test4Activity":
                intent.setClass(RedirectActivity.this, Test5Activity.class);
                startActivity(intent);
                break;
            case "Test5Activity":
                intent.setClass(RedirectActivity.this, Test6Activity.class);
                startActivity(intent);
                break;
            case "Test6Activity":
                intent.setClass(RedirectActivity.this, Test7Activity.class);
                startActivity(intent);
                break;
            case "Test7Activity":
                intent.setClass(RedirectActivity.this, Test8Activity.class);
                startActivity(intent);
                break;
            case "Test8Activity":
                intent.setClass(RedirectActivity.this, Test9Activity.class);
                startActivity(intent);
                break;
            case "Test9Activity":
                intent.setClass(RedirectActivity.this, Test10Activity.class);
                startActivity(intent);
                break;




        }
        this.finish();
    }
    void showDescription(String Name){


        switch (Name){
            case "MainActivity":
                tvDes1.setText(description[0][0]);
                tvDes2.setText(description[0][1]);
                break;
            case "Test1Activity":
                tvDes1.setText(description[1][0]);
                tvDes2.setText(description[1][1]);
                break;
            case "Test2Activity":
                tvDes1.setText(description[2][0]);
                tvDes2.setText(description[2][1]);
                break;
            case "Test3Activity":
                tvDes1.setText(description[3][0]);
                tvDes2.setText(description[3][1]);
                break;
            case "Test4Activity":
                tvDes1.setText(description[4][0]);
                tvDes2.setText(description[4][1]);
                break;
            case "Test5Activity":
                tvDes1.setText(description[5][0]);
                tvDes2.setText(description[5][1]);
                break;
            case "Test6Activity":
                tvDes1.setText(description[6][0]);
                tvDes2.setText(description[6][1]);
                break;
            case "Test7Activity":
                tvDes1.setText(description[7][0]);
                tvDes2.setText(description[7][1]);
                break;
            case "Test8Activity":
                tvDes1.setText(description[8][0]);
                tvDes2.setText(description[8][1]);
                break;
            case "Test9Activity":
                tvDes1.setText(description[9][0]);
                tvDes2.setText(description[9][1]);
                break;




        }


    }
    void initView(){
        //tvDes1 = (TextView)findViewById(R.id.tvDes1);
        //tvDes2 = (TextView)findViewById(R.id.tvDes2);
        btn = (Button)findViewById(R.id.btnNext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect(ActivityName);
            }
        });




        //pagerTabStrip.setTextColor(Color.parseColor("#3D3D3D"));


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        for(int i=0;i< tips.length;i++){
            tips[i].setBackgroundResource(R.drawable.unfocus_tab);
        }
        tips[position].setBackgroundResource(R.drawable.focus_tab);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(mResources[position]);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    /*
        @Override
        public CharSequence getPageTitle(int position) {


            return ""+mTextResources[position];
        }*/
    }
    void initDescription(){
        description = new String[10][2];
        description[0][0]="\n數字導向分測驗 - 針對集中性注意力";
        description[0][1]="\n此分測驗利用隨機排列的數字(1 至9)作為視覺刺激源，受試者必須在限定的時間內，盡可能地在平板畫面中選取系統所指定的特定數字";
        description[1][0]="\n文字導向分測驗 - 針對集中性注意力";
        description[1][1]="\n此分測驗利用隨機排列的數字(一 至九)作為視覺刺激源，受試者必須在限定的時間內，盡可能地在平板畫面中選取系統所指定的特定國字";
        description[2][0]="\n圖片對照分測驗 - 針對持續性注意力";
        description[2][1]="\n此分測驗利用兩張相似的圖片作為視覺刺激源，受試者必須在有限的時間內盡可能地在平板畫面中圈選兩張圖片不一致之處";
        description[3][0]="\n數字圈選分測驗 - 針對持續性注意力";
        description[3][1]="\n此分測驗利用隨機分布的兩位數數字作為視覺刺激源，受試者必須在限定的時間內盡可能地在平板畫面中圈選系統所指定的特定數字";
        description[4][0]="\n地圖搜尋分測驗 - 針對選擇性注意力分測驗";
        description[4][1]="\n此分測驗利用捷運圖作為背景，並以這些地圖上的地鐵路徑、地鐵站、地區名稱與各種符號作為視覺干擾源。受試者必須在限定的時間內盡可能地在平板畫面中圈選系統所指定的捷運站";
        description[5][0]="\n符號偵測分測驗 - 針對選擇性注意力分測驗";
        description[5][1]="\n此分測驗利用數層深淺不一的重疊注音符號作為視覺干擾源。受試者必須在限定的時間內盡可能地在平板畫面中圈選系統所指定的注音符號";
        description[6][0]="\n符號交替圈選分測驗 - 針對交替性注意力";
        description[6][1]="\n此分測驗利用多種簡單的幾何圖形作為視覺刺激源。受試者必須在限定的時間內盡可能地依照系統指示交替圈選特定的幾何圖形";
        description[7][0]="\n數字交替分測驗 - 針對交替性注意力";
        description[7][1]="\n此分測驗利用兩種簡單的個位數數字作為視覺刺激源。受試者必須在限定的時間內盡可能地依照系統指示交替圈選特定的數字";
        description[8][0]="\n圈選結合單音分測驗 - 針對分配性注意力";
        description[8][1]="\n此分測驗運用持續性注意力測驗中的「數字圈選分測驗」，結合同時呈現的聽覺刺激評量分配性注意力。受試者除須在限定的時間內執行「數字圈選分測驗」外，並且須同時注意聆聽是否出現特定的單音，當單音撥放時，受試者須立即在平板的選項上點擊聽到單音選項";
        description[9][0]="\n對照結合單音分測驗 - 針對分配性注意力";
        description[9][1]="\n此分測驗運用持續性注意力測驗中的「圖片對照分測驗」，結合同時呈現的聽覺刺激評量分配性注意力。受試者除須在限定的時間內執行「圖片對照分測驗」外，並且須同時注意聆聽是否出現特定的單音，當單音撥放時，受試者須立即在平板的選項上點擊聽到單音選項";
    }

}
