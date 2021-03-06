package com.example.loveyoplus.myapplication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import com.neurosky.thinkgear.*;
import android.bluetooth.BluetoothAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
/**
 * Created by loveyoplus on 2017/2/17.
 */

public class Test4Activity extends AppCompatActivity implements View.OnClickListener {
    TableLayout tl1;
    Button[] btn;
    String[] answer;
    TextView tv1,tv2,timer;
    RelativeLayout rl1;
    private Handler mHandler;
    int[] result;
    int randomNumLen=56;//1~56的數字
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
    Boolean isWorked=true;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t4);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();
        GAMETIME=loadSetting(4);
        mHandler = new Handler();
        listenhandler = new Handler();
        blutoothSetting();

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageView tempiv =  new ImageView(Test4Activity.this);
        tempiv.setScaleType(ImageView.ScaleType.FIT_XY);
        tempiv.setBackgroundColor(Color.WHITE);
        tempiv.setLayoutParams(rlp);
        rl1.addView(tempiv);



    }
    void initView(){
        dataList = new listProcess();
        startDateandTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        ID=getIntent().getStringExtra("ID");

        tv2 = (TextView)findViewById(R.id.tv2);
        tv1 = (TextView)findViewById(R.id.tv1);
        timer= (TextView)findViewById(R.id.tv3);
        tl1 = (TableLayout) findViewById(R.id.tl1);
        rl1 = (RelativeLayout)findViewById(R.id.rl1);
        ivbrain = (ImageView)findViewById(R.id.ivblutooth);
        tvbluetooth= (TextView)findViewById(R.id.tvbluetooth);
        btn = new Button[100];
        result = new int[2];


        //排版區
        tv1.setText("限時時間內找出數字:");
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

            rl1.addView(new ImageView(Test4Activity.this));
            //rl1.addView(tempiv);
            new CountDownTimer(5000, 100) {

                @Override

                public void onTick(long millisUntilFinished) {
                    ImageView tempiv = new ImageView(Test4Activity.this);
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
                    //row params
                    //TableRow.LayoutParams
                    TableLayout.LayoutParams trp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);
                    trp.weight=1;
                    //btn params in row
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    lp.weight=1;
                    lp.setMargins(2,2,2,2);
                    GradientDrawable gdDefault = new GradientDrawable();
                    gdDefault.setColor(Color.parseColor("#2980b9"));
                    gdDefault.setCornerRadius(10);

                    for(int i =0;i<7;i++) {
                        TableRow tr = new TableRow(Test4Activity.this);
                        tr.setLayoutParams(trp);
                        for(int j=0;j<8;j++){
                            btn[i*8+j] = new Button(Test4Activity.this);
                            btn[i*8+j].setOnClickListener(Test4Activity.this);
                            btn[i*8+j].setLayoutParams(lp);
                            btn[i*8+j].setText((i*8+j+1)+"");
                            btn[i*8+j].setTextSize(50);
                            btn[i*8+j].setBackground(gdDefault);

                            tr.addView(btn[i*8+j]);
                        }

                        tl1.addView(tr);

                    }
                    setBtn();
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

                    timer.setText(""+new SimpleDateFormat("mm").format(millisUntilFinished)+":"+ new SimpleDateFormat("ss").format(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    timer.setText("結束");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {


                            fileStorage fs = new fileStorage();

                            //String endDateandTime = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").format(new Date());
                            //String content = "{\"timestamp\":"+startDateandTime+",\"ques_id\":"+1+",\"ques_time:\""+GAMETIME+",\"do_right\":"+result[1]+",\"do_wrong\":"+result[0]+"}\r\n";
                            dataList.setInitial(ID.split("_")[0],startDateandTime,"4",(GAMETIME/1000)+"",result[1]+"",result[0]+"","0","0");
                            String content = dataList.printAll();
                            Log.e("printAll",content);
                            fs.writeFile(ID,content);

                            //fs.readFile2Map();
                            tgDevice.close();


                            isWorked=false;

                        }

                    }).start();

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",ID);
                    bundle.putString("ActivityName",Test4Activity.this.getClass().getSimpleName().toString());
                    intent.putExtras(bundle);
                    intent.setClass(Test4Activity.this, RedirectActivity.class);
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
