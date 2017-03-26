package com.example.loveyoplus.myapplication;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import java.util.Random;import com.neurosky.thinkgear.*;
import android.bluetooth.BluetoothAdapter;
import android.os.Message;

/**
 * Created by loveyoplus on 2017/2/21.
 */

public class Test9Activity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rl[],rl1;
    ImageView iv[],answerIv;
    int tag[],answerNum,soundAnswer=-1;
    TextView tv[],timer;
    int[] result,soundResult;
    private Handler mHandler;
    MediaPlayer mp[];
    int GAMETIME=1000*5;//遊戲時間
    String ID="";
    String startDateandTime;
    TGDevice tgDevice;
    BluetoothAdapter btAdapter;
    listProcess dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t9);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        startDateandTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        ID=getIntent().getStringExtra("ID");
        dataList = new listProcess();

        result = new int[2];
        soundResult= new int[2];
        tv = new TextView[3];
        tv[0]= (TextView) findViewById(R.id.textView2);
        tv[1]= (TextView) findViewById(R.id.textView6);timer=tv[1];

        mp = new MediaPlayer[3];
        mp[0] = MediaPlayer.create(this, R.raw.drumsound);
        mp[1] = MediaPlayer.create(this, R.raw.pianosound);
        mp[2] = MediaPlayer.create(this, R.raw.trumpetsound);


        tv[0].setText("選下列所顯示之圖案並\n" +  "音樂響起選取樂器");

        answerIv = (ImageView)findViewById(R.id.imageView);
        rl1 = (RelativeLayout)findViewById(R.id.rl);
        rl = new RelativeLayout[23];
        iv = new ImageView[23];


        //抓取資源tag[x][0]=獲取圖片id
        tag = new int[5];

        for(int i = 0;i<5;i++){
            tag[i] = getResources().getIdentifier("t7_"+(i+1),"drawable",getPackageName());
        }
        GAMETIME= loadSetting(9);
        blutoothSetting();
        mHandler = new Handler();
        mHandler.post(startCountdowntimer);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageView tempiv =  new ImageView(Test9Activity.this);
        tempiv.setScaleType(ImageView.ScaleType.FIT_XY);
        tempiv.setBackgroundColor(Color.WHITE);
        tempiv.setLayoutParams(rlp);
        rl1.addView(tempiv);
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
                /*case TGDevice.MSG_ATTENTION:
                    Log.v("HelloEEG", "Attention: " + msg.arg1);
                    dataAttention.setText("Attention: " + String.valueOf(msg.arg1));
                    break;
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

            rl1.addView(new ImageView(Test9Activity.this));
            //rl1.addView(tempiv);
            new CountDownTimer(5000, 100) {

                @Override

                public void onTick(long millisUntilFinished) {
                    ImageView tempiv = new ImageView(Test9Activity.this);
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
                    for(int i=0;i<23;i++){
                        iv[i] =(ImageView) findViewById(getResources().getIdentifier("iv" + (i + 1), "id", getPackageName()));
                        rl[i] =(RelativeLayout) findViewById(getResources().getIdentifier("rl" + (i + 1), "id", getPackageName()));
                        rl[i].setOnClickListener(Test9Activity.this);
                    }


                    setQuestion();
                    answerNum=setImageView();
                    while(answerNum<4){
                        setQuestion();
                        answerNum=setImageView();
                    }
                    mHandler.post(countdowntimer);
                    mHandler.post(soundtimer);


                }
            }.start();

        }
    };
    private Runnable soundtimer = new Runnable() {
        public void run() {
            new CountDownTimer(GAMETIME, 4000) {

                @Override

                public void onTick(long millisUntilFinished) {
                    //倒數秒數中要做的事
                    removeSoundAllMark();

                    if(millisUntilFinished>4000&&millisUntilFinished<=GAMETIME-4000){
                        if(soundAnswer!=-1)soundResult[0]++;
                        setSoundQuestion();
                        Log.d("setSoundQ answer",soundAnswer+"");
                        mp[soundAnswer].start();
                    }



                }

                @Override
                public void onFinish() {



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
                    fileStorage fs = new fileStorage();
                    dataList.setInitial(ID.split("_")[0],startDateandTime,"9",(GAMETIME/1000)+"",result[1]+"",result[0]+"",soundResult[1]+"",soundResult[0]+"");
                    String content = dataList.printAll();
                    Log.e("printAll",content);
                    fs.writeFile(ID,content);
                    tgDevice.close();

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",ID);
                    bundle.putString("ActivityName",Test9Activity.this.getClass().getSimpleName().toString());
                    intent.putExtras(bundle);
                    intent.setClass(Test9Activity.this, RedirectActivity.class);
                    startActivity(intent);
                    Test9Activity.this.finish();

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
            iv[i].setImageResource(getResources().getIdentifier("t7_"+(selected),"drawable",getPackageName()));
            rl[i].setTag(selected);

        }
        return answerNum;
    }

    public void setQuestion(){
        Random random = new Random();
        int selected= random.nextInt(tag.length)+1;
        Object o = getResources().getIdentifier("t7_"+(selected),"drawable",getPackageName());
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
        Log.e("result","Image:\n\t O:"+result[1]+"X:"+result[0]+"\nSound:\n\tO:"+soundResult[1]+"X:"+soundResult[0]);


    }
}
