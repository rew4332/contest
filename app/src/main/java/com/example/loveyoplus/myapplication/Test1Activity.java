package com.example.loveyoplus.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadFactory;

import android.os.Handler;
import android.widget.Toast;

import com.neurosky.thinkgear.*;

import static android.R.attr.strokeColor;
import static android.R.attr.strokeWidth;
import static com.example.loveyoplus.myapplication.R.id.ivanimate;
import static com.example.loveyoplus.myapplication.R.id.list;


public class Test1Activity extends AppCompatActivity implements View.OnClickListener {
    Button[] btn;
    TextView tv[],tvbluetooth;
    RelativeLayout rl1;
    LinearLayout llStart;
    int[] viewSize;//左半邊測驗區大小
    String[] answer;//題目
    int[] result;//紀錄目前對錯數量
     int GAMETIME=1000*5;//遊戲時間
    String randomNum[]={"1","2","3","4","5","6","7","8","9"};
    private Handler mHandler;
    String ID="";
    String startDateandTime;
    TGDevice tgDevice;
    BluetoothAdapter btAdapter;
    Boolean brainWave = false;
    listProcess dataList;
    ImageView ivbrain;
    Boolean allDeviceConnected[] = new Boolean[3];
    Handler listenhandler;
    BluetoothSocket bt[] =new BluetoothSocket[3];
    InputStream is[] = new InputStream[3];
    Boolean isWorked=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t1);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態



        initView();
        GAMETIME=loadSetting(1);

        mHandler = new Handler();
        listenhandler = new Handler();
        blutoothSetting();

        //mHandler.post(countdowntimer);


        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageView tempiv =  new ImageView(Test1Activity.this);
        tempiv.setScaleType(ImageView.ScaleType.FIT_XY);
        tempiv.setBackgroundColor(Color.WHITE);
        tempiv.setLayoutParams(rlp);
        rl1.addView(tempiv);

    }
    void blutoothSetting(){

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        allDeviceConnected = new Boolean[3];
        allDeviceConnected[0]=false;
        allDeviceConnected[1]=false;
        allDeviceConnected[2]=false;

        if (btAdapter != null && btAdapter.isEnabled()) {
            ivbrain.setImageResource(R.drawable.brainwave_bluetooth_on);
            tvbluetooth.setText("裝置搜尋中");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        bluetoothtest("pulse");

                        bluetoothtest("gsr");

                        bluetoothtest("emg");
                        BluetoothDevice mmDevice=null;
                        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
                        if(pairedDevices.size() > 0)
                        {
                            for(BluetoothDevice device : pairedDevices)
                            {
                                if(device.getName().equals("MindWave Mobile"))
                                {
                                    mmDevice = device;
                                    break;
                                }
                            }
                        }
                        tgDevice = new TGDevice(btAdapter, brainHandler);
                        tgDevice.connect(mmDevice,true);

                        tgDevice.start();
                        mHandler.post(startCountdowntimer);




                    }catch (Exception consumed)
                    {
                        Log.e("interrupted",consumed+"");
                    }
                }
            }).start();

        }
        else{

            ivbrain.setImageResource(R.drawable.brainwave_bluetooth_off);
            Log.v("HelloEEG", "bluetooth off");
            tvbluetooth.setText("未開啟藍芽");

        }


    }

    public void bluetoothtest(String nameOfBluetooth){
        int blueToothID;
        BluetoothSocket mmSocket;

        BluetoothDevice mmDevice=null;


        InputStream mmInputStream =null;

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID

        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals(nameOfBluetooth))
                {

                    mmDevice = device;

                    break;
                }
            }
        }

        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);


            mmSocket.connect();


            mmInputStream = mmSocket.getInputStream();
            switch (nameOfBluetooth){
                case "gsr":
                    bt[0]=mmSocket;
                    is[0]=mmInputStream;
                    break;
                case "emg":
                    bt[1]=mmSocket;
                    is[1]=mmInputStream;
                    break;
                case "pulse":
                    bt[2]=mmSocket;
                    is[2]=mmInputStream;
                    break;
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        if(mmInputStream!=null) {
            switch (nameOfBluetooth){
                case "gsr":
                    allDeviceConnected[0]=true;
                    break;
                case "emg":
                    allDeviceConnected[1]=true;
                    break;
                case "pulse":
                    allDeviceConnected[2]=true;
                    break;
            }
            beginListenForData(mmInputStream, nameOfBluetooth);
        }


    }
    void beginListenForData(final InputStream input, final String nameOfBluetooth)
    {
        final byte[] readBuffer;


        final byte delimiter = 10; //This is the ASCII code for a newline character



        readBuffer = new byte[1024];
        new Thread(new Runnable() {
            @Override
            public void run() {

                int readBufferPosition = 0;

                while (!Thread.currentThread().isInterrupted()&&isWorked) {
                    try {
                        int bytesAvailable = input.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            input.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    int count = 0;
                                    listenhandler.post(new Runnable() {
                                        public void run() {
                                            if (nameOfBluetooth.equals("pulse")) {

                                                dataList.addPULSE(data + "");

                                            } else if (nameOfBluetooth.equals("gsr")) {

                                                dataList.addGSR(data + "");
                                            } else if (nameOfBluetooth.equals("emg")) {

                                                dataList.addEMG(data + "");
                                            }
                                            //myLabel.setText(data);
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {

                    }

                }
                try {
                    switch (nameOfBluetooth){
                        case "gsr":
                            if(bt[0]!=null){
                                is[0].close();
                                bt[0].close();
                            }
                            break;
                        case "emg":
                            if(bt[1]!=null) {
                                is[1].close();
                                bt[1].close();
                            }
                            break;
                        case "pulse":
                            if(bt[2]!=null){
                                is[2].close();
                                bt[2].close();
                            }
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }).start();



    }

    private Handler brainHandler = new Handler() {


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
                            tvbluetooth.setText("連線中");

                            break;
                        case TGDevice.STATE_CONNECTED:
                            Log.v("HelloEEG", "CONNECTED");
                            brainWave = true;
                            tvbluetooth.setText("已連線");
                            ivbrain.setImageResource(R.drawable.brainwave_bluetooth);
                            tgDevice.start();
                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            Log.v("HelloEEG", "DISCONNECTED");
                            ivbrain.setImageResource(R.drawable.brainwave_bluetooth_dis);
                            tvbluetooth.setText("已斷線");

                            break;
                        case TGDevice.STATE_NOT_FOUND:
                            Log.v("HelloEEG", "STATE NOT FOUND");
                            ivbrain.setImageResource(R.drawable.brainwave_bluetooth_dis);
                            tvbluetooth.setText("裝置未開啟");
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
                /*case TGDevice.MSG_MEDITATION:
                    Log.v("HelloEEG", "Meditation: " + msg.arg1);
                    dataMeditation.setText("Meditation: " + String.valueOf(msg.arg1));
                case TGDevice.MSG_RAW_DATA:
                    //int rawValue = msg.arg1;


                    break;*/

                case TGDevice.MSG_EEG_POWER:
                    TGEegPower ep = (TGEegPower)msg.obj;
                    Log.d("HelloEEG", "Delta: " + ep.delta);




                    //fileStorage fs = new fileStorage();
                    dataList.addArray(ep.delta+"",ep.theta+"",ep.lowAlpha+"",ep.highAlpha+"",ep.lowBeta+"",ep.highBeta+"",ep.lowGamma+"",ep.midGamma+"");

                    //fs.writeFile(ID,content);
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
    void initView(){
        dataList = new listProcess();
        startDateandTime = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").format(new Date());
        ID=getIntent().getStringExtra("ID");
        ivbrain = (ImageView)findViewById(R.id.ivblutooth);
        tvbluetooth= (TextView)findViewById(R.id.tvbluetooth);

        //initialize
        result = new int[2];
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        btn = new Button[10];
        tv =new TextView[3];
        for(int i=0;i<3;i++){
            tv[i] =  (TextView)findViewById(getResources().getIdentifier("tv" + (i + 1), "id", getPackageName()));
        }
        tv[0].setText("限時時間內找出數字:");

        //get layout size
        viewSize = new int[2];
        viewSize[0]=getViewSize()[0];
        viewSize[1]=getViewSize()[1];



    }
    private Runnable startCountdowntimer = new Runnable() {
        public void run() {

            final RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

            rl1.addView(new ImageView(Test1Activity.this));
            //rl1.addView(tempiv);
            new CountDownTimer(5000, 100) {

                @Override

                public void onTick(long millisUntilFinished) {
                    ImageView tempiv = new ImageView(Test1Activity.this);
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
                    for(int i=0;i<9;i++){
                        btn[i]=(Button)findViewById(getResources().getIdentifier("button" + (i + 1), "id", getPackageName()));
                        btn[i].setTextSize(70);
                        btn[i].setOnClickListener(Test1Activity.this);
                    }
                    setBtn();
                    setQuestion();//first Question
                    mHandler.post(countdowntimer);

                }
            }.start();

        }
    };
    //timer
    private Runnable countdowntimer = new Runnable() {
        public void run() {
            new CountDownTimer(GAMETIME, 1000) {

                @Override

                public void onTick(long millisUntilFinished) {
                    //倒數秒數中要做的事

                    tv[2].setText(""+new SimpleDateFormat("mm").format(millisUntilFinished)+":"+ new SimpleDateFormat("ss").format(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    tv[2].setText("結束");
                    disableBtn();



                    new Thread(new Runnable() {
                        @Override
                        public void run() {


                            fileStorage fs = new fileStorage();

                            //String endDateandTime = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").format(new Date());
                            //String content = "{\"timestamp\":"+startDateandTime+",\"ques_id\":"+1+",\"ques_time:\""+GAMETIME+",\"do_right\":"+result[1]+",\"do_wrong\":"+result[0]+"}\r\n";
                            dataList.setInitial(ID.split("_")[0],startDateandTime,"1",(GAMETIME/1000)+"",result[1]+"",result[0]+"","0","0");
                            String content = dataList.printAll();
                            Log.e("printAll",content);
                            fs.writeFile(ID,content);


                            //Log.e("map content",fs.readFile2Map(1).toString());

                            //fs.readFile2Map();




                            tgDevice.close();
                            isWorked=false;
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("ID",ID);
                            bundle.putString("ActivityName",Test1Activity.this.getClass().getSimpleName().toString());
                            intent.putExtras(bundle);
                            intent.setClass(Test1Activity.this, RedirectActivity.class);
                            startActivity(intent);
                            Test1Activity.this.finish();
                        }

                    }).start();

                }
            }.start();

        }
    };

    //鎖住所有按鈕
    public void disableBtn(){
        for(int i = 0 ;i<9;i++){
            btn[i].setVisibility(View.INVISIBLE);
        }
    }

    //設定答案
    public void setQuestion(){

        answer = new String[3];

        String s="";
        for(int i = 0 ;i<3;i++){
            String temp;
            Random random = new Random();
            int selected= random.nextInt(9-i);
            temp=randomNum[selected];
            answer[i]=randomNum[selected];
            randomNum[selected]=randomNum[9-1-i];
            randomNum[9-1-i]=temp;
            s+=answer[i]+"\t\t\t\t";

        }
        tv[1].setText(s);
    }
    //設定按鈕位置及按鈕數字
    public void setBtn(){

        int btnSize=viewSize[0]/5;
        int hdiv3=viewSize[1]/3;
        int wdiv3=viewSize[0]/3;


        //Set the button position
        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(Color.parseColor("#2980b9"));
        gdDefault.setCornerRadius(btnSize/2);

        RelativeLayout.LayoutParams params;
        for(int i = 0 ;i<9;i++){
            btn[i].setVisibility(View.VISIBLE);

            //btn[i].setBackgroundDrawable(gdDefault);
            btn[i].setBackground(gdDefault);
            btn[i].setTextColor(Color.WHITE);

            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.width=btnSize;
            params.height=btnSize;


            int wRan=/* 0;*/ (int)(Math.random()*(wdiv3-btnSize));
            Log.d("wRan",wRan+"");
            int hRan=/* 0;*/ (int)(Math.random()*(hdiv3-btnSize));
            Log.d("hRan",wRan+"");
            if (i<3)
                params.setMargins(i%3*wdiv3+wRan,0+hRan,0,0);
            else if(i<6)
                params.setMargins(i%3*wdiv3+wRan,hdiv3+hRan,0,0);
            else
                params.setMargins(i%3*wdiv3+wRan,hdiv3*2+hRan,0,0);
            btn[i].setLayoutParams(params);
        }

        //set Button text  抽牌法

        for(int i = 0 ;i<9;i++){

            String temp;
            Random random = new Random();
            int selected= random.nextInt(9-i);
            temp=randomNum[selected];
            btn[i].setText(randomNum[selected]+"");
            randomNum[selected]=randomNum[9-1-i];
            randomNum[9-1-i]=temp;

        }


    }
    //得到relativelayout大小
    public int[] getViewSize(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Log.e("screenWidth",displayMetrics.widthPixels+"");
        Log.e("screenHeight",displayMetrics.heightPixels+"");
        int width =(displayMetrics.widthPixels);// rl1.getMeasuredWidth();
        int height = 2*displayMetrics.heightPixels/3;//rl1.getMeasuredHeight();
        int[] viewSize={width,height};
        Log.e("viewWidth",viewSize[0]+"");
        Log.e("viewHeight",viewSize[1]+"");
        return viewSize;

    }


    @Override
    public void onClick(View v) {
        for(int i=0;i<9;i++) {
            if ( v==btn[i]){
                btn[i].setVisibility(View.INVISIBLE);
                result[0]++;
                for(int j=0;j<3;j++) {
                    if (answer[j].equals(btn[i].getText().toString())) {
                        result[1]++;
                        result[0]--;
                        tv[1].setText(tv[1].getText().toString().replace(answer[j]+"\t\t\t\t",""));
                    }

                }
                if ((result[0]+result[1])%3==0){
                    setBtn();
                    setQuestion();
                }
                Log.e("result","O:"+result[1]+"\nX:"+result[0]);
            }
        }
    }
}

