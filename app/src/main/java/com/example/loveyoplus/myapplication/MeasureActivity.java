package com.example.loveyoplus.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;

import static android.R.id.message;

/**
 * Created by loveyoplus on 2017/4/4.
 */

public class MeasureActivity extends AppCompatActivity {
    boolean isRecord=false;
    Button btnrecord,btnStop;
    Boolean showAnimate=false;
    android.os.Handler listenhandler;
    Message message;
    ImageView ivanimate;
    android.os.Handler mHandler;
    TGDevice tgDevice;
    BluetoothAdapter btAdapter;
    ImageView ivbrain;
    TextView tvbluetooth;
    listProcess dataList;
    ImageButton btnRefresh;
    Boolean brainWaveConnected=false;
    Boolean stopWorker,allDeviceConnected[];
    Thread workerThread;
    BluetoothSocket gsr,emg,pluse;
    String msg;
    String ID;
    Thread updateThread;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
        //stopWorker=true;
        blutoothSetting();



    }
    void blutoothSetting(){
        allDeviceConnected = new Boolean[3];
        allDeviceConnected[0]=false;
        allDeviceConnected[1]=false;
        allDeviceConnected[2]=false;

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btnRefresh.setEnabled(false);
        if (btAdapter != null && btAdapter.isEnabled()) {
            ivbrain.setImageResource(R.drawable.brainwave_bluetooth_on);
            tvbluetooth.setText("裝置搜尋中");

            workerThread = new Thread(new Runnable() {
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




                    }catch (Exception consumed)
                    {
                        Log.e("interrupted",consumed+"");
                    }
                }
            });
            workerThread.start();
        }
        else{

            ivbrain.setImageResource(R.drawable.brainwave_bluetooth_off);
            Log.v("HelloEEG", "bluetooth off");
            tvbluetooth.setText("未開啟藍芽");
            btnRefresh.setEnabled(true);
        }


    }
    public void bluetoothtest(String nameOfBluetooth){
        int blueToothID;
        BluetoothSocket mmSocket;

        BluetoothDevice mmDevice=null;

        OutputStream mmOutputStream = null;
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
    void updateData(){

        updateThread=new Thread(new Runnable() {
            @Override
            public void run() {
                dataList.removeAllData();
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!updateThread.isInterrupted()&&showAnimate==true) {

                    try {
                        Map<String, String> data;
                        // Create the data structure to transfer.

                        NetCon conn = new NetCon();
                        data = new HashMap<String, String>(dataList.returnMapWithAllData());
                        Log.e("map", data + "");
                        // Send Request

                        msg = conn.SetJson(data).SetUrl("http://140.116.179.52/dbinsert2.php").Execute();
                        Log.e("msg ", msg);
                        if (msg.equals("OK")) {
                            dataList.removeAllData();
                            Message m = new Message();
                            m.what = 2;
                            listenhandler.sendMessage(m);
                            Thread.sleep(20000);

                        }
                        else{
                            Thread.sleep(700);
                        }

                    } catch (IOException e) {
                        msg = e.getMessage();

                        Log.e("exception", msg);
                    }
                    catch(NullPointerException e){
                        Log.e("exception", msg);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("exception", msg);
                    }
                }
            }
        });
        updateThread.start();
    }
    void updateLastData(){

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Map<String, String> data;
                    // Create the data structure to transfer.

                    NetCon conn = new NetCon();
                    data = new HashMap<String, String>(dataList.returnMapWithAllData());
                    Log.e("map", data + "");
                    // Send Request

                    msg = conn.SetJson(data).SetUrl("http://140.116.179.52/dbinsert2.php").Execute();
                    Log.e("msg ", msg);
                    if (msg.equals("OK")) {
                        dataList.removeAllData();
                        Message m = new Message();
                        m.what = 2;
                        listenhandler.sendMessage(m);

                    }

                } catch (IOException e) {
                    msg = e.getMessage();

                    Log.e("exception", msg);
                }
                catch(NullPointerException e){
                    Log.e("exception", msg);

                }
            }

        }).start();

    }

    void beginListenForData(final InputStream input,final String nameOfBluetooth)
    {
        final byte[] readBuffer;


        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;

        readBuffer = new byte[1024];
            new Thread(new Runnable() {
                @Override
                public void run() {

                        int readBufferPosition = 0;
                        stopWorker = false;
                        while (!Thread.currentThread().isInterrupted() && !stopWorker) {
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
                                                        ((TextView) findViewById(R.id.textView6)).setText(" pulse data: " + data);
                                                        dataList.addPULSE(data + "");

                                                    } else if (nameOfBluetooth.equals("gsr")) {
                                                        ((TextView) findViewById(R.id.textView2)).setText("gsr data:" + data);
                                                        dataList.addGSR(data + "");
                                                    } else if (nameOfBluetooth.equals("emg")) {
                                                        ((TextView) findViewById(R.id.textView4)).setText("emg data:" + data);
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
                    }

            }).start();



    }


    public void initView(){
        ID=getIntent().getStringExtra("ID").split("_")[0];
        btnrecord = (Button) findViewById(R.id.btnrecord);
        btnStop =(Button) findViewById(R.id.btnStop);
        ivanimate = (ImageView) findViewById(R.id.ivanimate);
        ivbrain = (ImageView)findViewById(R.id.ivblutooth);
        tvbluetooth= (TextView)findViewById(R.id.tvbluetooth);
        dataList = new listProcess();
        //ID="fakeyoga";
        dataList.setInitial(ID);

        btnRefresh = (ImageButton)findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopWorker=true;
                blutoothSetting();
            }
        });


        btnrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!showAnimate) {

                    updateData();
                    showAnimate=true;
                    btnrecord.setText("PAUSE");
                    brainWaveConnected=true;
                    new Thread(new Runnable() {
                        public void run() {
                            Log.e("showAnimate",showAnimate+"");
                            while (showAnimate) {

                                Message m = new Message();
                                m.what = 1;
                                MeasureActivity.this.listenhandler.sendMessage(m);
                                try {
                                    Thread.sleep(1150);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();

                }
                else{
                    updateLastData();
                    updateThread.interrupt();
                    brainWaveConnected=false;
                    showAnimate=false;
                    btnrecord.setText("RECORD");
                }
            }
        });
        message = new Message();
        message.what=1;
        listenhandler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e("msg.what",msg.what+"");
                switch (msg.what){

                    case 1:

                        startAnimate.run();
                        break;
                    case 2:
                        Toast.makeText(MeasureActivity.this,"上傳成功",Toast.LENGTH_LONG).show();
                }

            }
        };
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Leave();


            }
        });

    }
    private void Leave() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("是否離開?")
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 左方按鈕方法

                    }
                })
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 右方按鈕方法
                        MeasureActivity.this.finish();
                        tgDevice.close();
                        System.exit(0);

                    }
                });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
    }
    private android.os.Handler brainHandler = new android.os.Handler() {


        @Override
        public void handleMessage(Message msg) {

            //Log.v("HelloEEG", "Handler started");
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
                            brainWaveConnected=true;
                            tvbluetooth.setText("已連線");
                            ivbrain.setImageResource(R.drawable.brainwave_bluetooth);


                            tgDevice.start();
                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            Log.v("HelloEEG", "DISCONNECTED");
                            ivbrain.setImageResource(R.drawable.brainwave_bluetooth_dis);
                            tvbluetooth.setText("已斷線");

                            brainWaveConnected=false;
                            btnRefresh.setEnabled(true);

                            break;
                        case TGDevice.STATE_NOT_FOUND:
                            Log.v("HelloEEG", "STATE NOT FOUND");
                            ivbrain.setImageResource(R.drawable.brainwave_bluetooth_dis);
                            tvbluetooth.setText("裝置未開啟");

                            brainWaveConnected=false;
                            btnRefresh.setEnabled(true);
                            break;
                        case TGDevice.STATE_NOT_PAIRED:
                            Log.v("HelloEEG", "STATE NOT PAIRED");
                            break;
                        default:
                            break;
                    }
                    break;
                case TGDevice.MSG_POOR_SIGNAL:
                        Log.e("signal",(100-Integer.parseInt(String.valueOf(msg.arg1))/2)+"");
                        tvbluetooth.setText("已連線\t訊號強度:"+(100-Integer.parseInt(String.valueOf(msg.arg1))/2)+"%");
                        Log.e("HelloEEG", "PoorSignal: " + msg.arg1);

                    break;
                case TGDevice.MSG_ATTENTION:
                    Log.v("HelloEEG", "Attention: " + msg.arg1);
                    if(brainWaveConnected) {
                        dataList.addAttention(String.valueOf(msg.arg1));
                    }
                    break;

                case TGDevice.MSG_EEG_POWER:
                    TGEegPower ep = (TGEegPower)msg.obj;
                    Log.d("HelloEEG", "Delta: " + ep.delta);
                    for(int i=0;i<3;i++){
                        if(!allDeviceConnected[i])break;
                        if(i==2)btnrecord.setEnabled(true);
                    }




                    //fileStorage fs = new fileStorage();
                    if(brainWaveConnected) {
                        dataList.addArray(ep.delta + "", ep.theta + "", ep.lowAlpha + "", ep.highAlpha + "", ep.lowBeta + "", ep.highBeta + "", ep.lowGamma + "", ep.midGamma + "");

                    }
                    //fs.writeFile(ID,content);


                    //data.setText("Signal: " + ep.delta);
                    break;
                default:
                    break;
            }
        }
    };

    private Runnable startAnimate = new Runnable() {
        public void run() {
            Log.e("countdowntimer","");
            new CountDownTimer(1150, 25) {

                @Override

                public void onTick(long millisUntilFinished) {
                    ivanimate.setImageResource(getResources().getIdentifier("brainwave" + (23-(millisUntilFinished)%575/25), "drawable", getPackageName()));
                }

                @Override
                public void onFinish() {
                    ivanimate.setImageResource(getResources().getIdentifier("brainwave" + (1), "drawable", getPackageName()));


                }
            }.start();

        }
    };
}
