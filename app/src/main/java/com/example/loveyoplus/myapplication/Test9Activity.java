package com.example.loveyoplus.myapplication;
import android.bluetooth.BluetoothDevice;
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
import java.util.Random;
import java.util.Set;

import com.neurosky.thinkgear.*;
import android.bluetooth.BluetoothAdapter;
import android.os.Message;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import android.bluetooth.BluetoothSocket;

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
    ImageView ivbrain;
    TextView tvbluetooth;
    Boolean allDeviceConnected[] = new Boolean[3];
    Handler listenhandler;
    BluetoothSocket bt[] =new BluetoothSocket[3];
    InputStream is[] = new InputStream[3];
    Boolean isWorked= true;

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
        tv[0]= (TextView) findViewById(R.id.tv1);
        tv[1]= (TextView) findViewById(R.id.tv3);timer=tv[1];
        ivbrain = (ImageView)findViewById(R.id.ivblutooth);
        tvbluetooth= (TextView)findViewById(R.id.tvbluetooth);

        mp = new MediaPlayer[3];
        mp[0] = MediaPlayer.create(this, R.raw.drumsound);
        mp[1] = MediaPlayer.create(this, R.raw.pianosound);
        mp[2] = MediaPlayer.create(this, R.raw.trumpetsound);


        tv[0].setText("選取顯示圖案及音樂");

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
        mHandler = new Handler();
        listenhandler = new Handler();
        blutoothSetting();
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageView tempiv =  new ImageView(Test9Activity.this);
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

                    timer.setText(""+new SimpleDateFormat("mm").format(millisUntilFinished)+":"+ new SimpleDateFormat("ss").format(millisUntilFinished));

                }

                @Override
                public void onFinish() {
                    timer.setText("結束");
                    for(int i=0;i<20;i++) {
                        rl[i].setVisibility(View.INVISIBLE);
                    }


                    new Thread(new Runnable() {
                        @Override
                        public void run() {


                            fileStorage fs = new fileStorage();

                            //String endDateandTime = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").format(new Date());
                            //String content = "{\"timestamp\":"+startDateandTime+",\"ques_id\":"+1+",\"ques_time:\""+GAMETIME+",\"do_right\":"+result[1]+",\"do_wrong\":"+result[0]+"}\r\n";
                            dataList.setInitial(ID.split("_")[0],startDateandTime,"9",(GAMETIME/1000)+"",result[1]+"",result[0]+"",soundResult[1]+"",soundResult[0]+"");
                            String content = dataList.printAll();
                            Log.e("printAll",content);
                            fs.writeFile(ID,content);

                            //fs.readFile2Map();
                            tgDevice.close();
                            isWorked = false;

                        }

                    }).start();

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
            ivX.setImageResource(R.drawable.circle3);
            ivX.setScaleType(ImageView.ScaleType.FIT_XY);
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
