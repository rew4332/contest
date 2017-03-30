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
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import com.neurosky.thinkgear.*;
import android.bluetooth.BluetoothAdapter;

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
    RelativeLayout r1,rl1;
    ImageView iv[];
    String[] tag;
    int[] result;
    String answer[];
    TextView tvTimer,tvTitle;
    int GAMETIME=1000*5;//遊戲時間
     int QUESTIONNUM=10;
    int destroyRunnable=0;
    int restQuestionNum;

    TGDevice tgDevice;
    BluetoothAdapter btAdapter;
    String ID="";
    String startDateandTime;
    String question[]={"0","1","2","3","4","5","6","7","8","9"};
    listProcess dataList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t3);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dataList = new listProcess();
        startDateandTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        ID=getIntent().getStringExtra("ID");

        r1 = (RelativeLayout)findViewById(R.id.r1);
        rl1 = (RelativeLayout)findViewById(R.id.rl1);
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
        answer[0]="0,1809,233,409;1993,1933,673,525;3553,0,625,293;2677,1853,189,177;3817,1985,505,809";
        answer[1]="0,213,1165,653;357,1033,273,313;2533,853,221,321;3325,877,229,489;1845,1657,465,837";
        answer[2]="2093,1641,273,257;0,2609,393,533;625,1777,213,325;1153,1721,181,133;2953,1813,681,809";
        answer[3]="4153,0,989,329;393,1177,1569,493;2081,945,669,825;1013,1857,473,661;2235,1878,553,553";
        answer[4]="0,2429,1325,969;713,1209,177,157;1057,1113,321,361;3073,1817,325,349;3405,1565,341,325";
        answer[5]="2813,1549,177,817;4605,1077,297,317;413,293,657,353;2385,1721,353,577;4261,1401,177,765";
        answer[6]="0,117,1593,813;2293,265,381,229;2885,2125,201,305;2745,2869,765,417;0,2565,857,369";
        answer[7]="1197,1773,817,393;3205,645,1633,437;105,2873,577,273;3749,2205,213,301;4173,2865,229,405";
        answer[8]="3033,1829,245,365;2997,2681,729,357;4281,361,189,813;1913,409,301,753;3753,305,293,301";
        answer[9]="1765,561,1349,461;2957,1085,173,685;1621,1257,217,305;3561,1453,581,1689;3297,2221,249,517";


        GAMETIME=loadSetting(3);
        blutoothSetting();
        mHandler = new Handler();
        mHandler.post(startCountdowntimer);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageView tempiv =  new ImageView(Test3Activity.this);
        tempiv.setScaleType(ImageView.ScaleType.FIT_XY);
        tempiv.setBackgroundColor(Color.WHITE);
        tempiv.setLayoutParams(rlp);
        rl1.addView(tempiv);


        tvTitle.setText("選取不同處(選圖)");
        Log.d("drawableTag",tag[0]);




    }
    void blutoothSetting(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter != null) {

            tgDevice = new TGDevice(btAdapter, handler);

            Log.v("HelloEEG", "CREATED TGDevice");
        }
        tgDevice.connect(true);

        tgDevice.start();
    }
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {

            Log.v("HelloEEG", "Handler started");
            switch (msg.what) {
                case TGDevice.MSG_STATE_CHANGE:
                    switch (msg.arg1) {
                        case TGDevice.STATE_IDLE:
                            Log.v("HelloEEG", "IDLE STATE");

                            break;
                        case TGDevice.STATE_CONNECTING:
                            Log.v("HelloEEG", "CONNECTING...");

                            break;
                        case TGDevice.STATE_CONNECTED:
                            Log.v("HelloEEG", "CONNECTED");
                            tgDevice.start();
                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            Log.v("HelloEEG", "DISCONNECTED");

                            break;
                        case TGDevice.STATE_NOT_FOUND:
                            Log.v("HelloEEG", "STATE NOT FOUND");
                            break;
                        case TGDevice.STATE_NOT_PAIRED:
                            Log.v("HelloEEG", "STATE NOT PAIRED");
                            break;
                        default:
                            break;
                    }
                    break;
                case TGDevice.MSG_POOR_SIGNAL:

                    //data.setText("Signal: " + String.valueOf(msg.arg1));
                    break;
                case TGDevice.MSG_ATTENTION:
                    Log.v("HelloEEG", "Attention: " + msg.arg1);
                    dataList.addAttention(String.valueOf(msg.arg1));
                    break;
                /*
                case TGDevice.MSG_MEDITATION:
                    Log.v("HelloEEG", "Meditation: " + msg.arg1);
                    dataMeditation.setText("Meditation: " + String.valueOf(msg.arg1));
                case TGDevice.MSG_RAW_DATA:
                    //int rawValue = msg.arg1;


                    break;*/

                case TGDevice.MSG_EEG_POWER:
                    TGEegPower ep = (TGEegPower)msg.obj;
                    Log.d("HelloEEG", "Delta: " + ep.delta);
                    dataList.addArray(ep.delta+"",ep.theta+"",ep.lowAlpha+"",ep.highAlpha+"",ep.lowBeta+"",ep.highBeta+"",ep.lowGamma+"",ep.midGamma+"");
                    Log.v("HelloEEG", "PoorSignal: " + msg.arg1);

                    //data.setText("Signal: " + ep.delta);
                    break;
                default:
                    break;
            }
        }
    };
    int loadSetting(int i){
        fileStorage fs = new fileStorage();
        fs.createFile("setting");
        fs.setContinueWrite(false);
        String s= fs.readFile();
        if(s==null)return 60*1000;
        return Integer.parseInt(s.split("\r\n")[i])*1000;
    }

    private Runnable startCountdowntimer = new Runnable() {
        public void run() {
            final RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

            rl1.addView(new ImageView(Test3Activity.this));
            //rl1.addView(tempiv);
            new CountDownTimer(5000, 100) {

                @Override

                public void onTick(long millisUntilFinished) {
                    ImageView tempiv = new ImageView(Test3Activity.this);
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
                    restQuestionNum=5;//剩下的答案數量
                    Random random = new Random();
                    int selected= random.nextInt(QUESTIONNUM);

                    for(int setId= 0;setId<5;setId++) {
                        //bind the first question
                        bindListener(Integer.parseInt(question[selected]),setId);
                    }
                    String temp = question[selected];
                    question[selected]=question[QUESTIONNUM-1];
                    question[QUESTIONNUM-1]=temp;
                    //binListener 每題有五個答案
                    QUESTIONNUM--;
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

                    tvTimer.setText("倒數時間:"+new SimpleDateFormat("m").format(millisUntilFinished)+":"+ new SimpleDateFormat("s").format(millisUntilFinished));
                    Log.e("還在倒數",mHandler.obtainMessage()+"qaq");

                }

                @Override
                public void onFinish() {
                    tvTimer.setText("倒數時間:結束");
                    //iv[0].setVisibility(View.INVISIBLE);
                    //Log.e("answerXD:",sTemp);
                    if(destroyRunnable==0) {
                        fileStorage fs = new fileStorage();

                        //String endDateandTime = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").format(new Date());
                        dataList.setInitial(ID.split("_")[0],startDateandTime,"3",(GAMETIME/1000)+"",result[1]+"",result[0]+"","0","0");
                        String content = dataList.printAll();
                        Log.e("printAll",content);
                        fs.writeFile(ID,content);


                        tgDevice.close();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("ID",ID);
                        bundle.putString("ActivityName",Test3Activity.this.getClass().getSimpleName().toString());
                        intent.putExtras(bundle);
                        intent.setClass(Test3Activity.this, RedirectActivity.class);
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
                    if(0==QUESTIONNUM){
                        //跳下個測驗
                        fileStorage fs = new fileStorage();
                        dataList.setInitial(ID.split("_")[0],startDateandTime,"3",(GAMETIME/1000)+"",result[1]+"",result[0]+"","0","0");
                        String content = dataList.printAll();
                        Log.e("printAll",content);
                        fs.writeFile(ID,content);
                        tgDevice.close();
                        destroyRunnable=1;
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("ID",ID);
                        bundle.putString("ActivityName",Test3Activity.this.getClass().getSimpleName().toString());
                        intent.putExtras(bundle);
                        intent.setClass(Test3Activity.this, RedirectActivity.class);
                        startActivity(intent);
                        Test3Activity.this.finish();
                    }
                    else{

                        r1.removeViewAt(1);r1.removeViewAt(1);r1.removeViewAt(1);r1.removeViewAt(1);r1.removeViewAt(1);
                        //跳下一題
                        String temp;
                        Random random = new Random();
                        int selected= random.nextInt(QUESTIONNUM);
                        restQuestionNum=5;
                        //binListener 每題有五個答案
                        for(int setId= 0;setId<5;setId++) {
                            //bind the first question
                            bindListener(Integer.parseInt(question[selected]),setId);
                        }
                        temp=question[selected];

                        question[selected]=question[QUESTIONNUM-1];
                        question[QUESTIONNUM-1]=temp;

                        QUESTIONNUM--;

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
