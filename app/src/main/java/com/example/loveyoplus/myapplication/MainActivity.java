package com.example.loveyoplus.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity  {
    EditText etId;
    Button btnLogin;
    ImageView ivbrain;
    TextView tvbluetooth;
    Message m;
    BluetoothAdapter btAdapter;
    TGDevice tgDevice;
    ImageButton btnRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        loadSetting();
        initView();
        blutoothSetting();






    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message m){
            //依照不同的訊息來做不同的處理
            switch(m.what){
                case 1:
                    blutoothSetting();
                    //removeMessages(m.what);
                    break;

            }
        }
    };
    private Handler brainHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {

            Log.v("HelloEEG", "Handler started");
            switch (msg.what){

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
                        btnRefresh.setEnabled(true);

                        break;
                    case TGDevice.STATE_NOT_FOUND:
                        Log.v("HelloEEG", "STATE NOT FOUND");
                        ivbrain.setImageResource(R.drawable.brainwave_bluetooth_dis);
                        tvbluetooth.setText("裝置未開啟");
                        btnRefresh.setEnabled(true);
                        break;
                    case TGDevice.STATE_NOT_PAIRED:
                        Log.v("HelloEEG", "STATE NOT PAIRED");
                        btnRefresh.setEnabled(true);
                        break;
                    default:
                        break;
                }
                    break;
                case TGDevice.MSG_POOR_SIGNAL:
                    tvbluetooth.setText("已連線\tPoor Signal:"+(msg.arg1));
                    ivbrain.setImageResource(R.drawable.brainwave_bluetooth);

                    //data.setText("Signal: " + String.valueOf(msg.arg1));
                    break;
                case TGDevice.MSG_ATTENTION:
                    Log.v("HelloEEG", "Attention: " + msg.arg1);

                    break;

                case TGDevice.MSG_EEG_POWER:
                    TGEegPower ep = (TGEegPower)msg.obj;
                    Log.d("HelloEEG", "Delta: " + ep.delta);

                    Log.v("HelloEEG", "PoorSignal: " + msg.arg1);

                    //data.setText("Signal: " + ep.delta);
                    break;
                default:
                    break;
            }
        }
    };
    void blutoothSetting(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btnRefresh.setEnabled(false);
        if(btAdapter != null &&btAdapter.isEnabled()) {
            ivbrain.setImageResource(R.drawable.brainwave_bluetooth_on);
            tvbluetooth.setText("裝置搜尋中");

            tgDevice = new TGDevice(btAdapter, brainHandler);
            tgDevice.connect(true);
            tgDevice.start();

            Log.v("HelloEEG", "CREATED TGDevice");
        }
        else{

            ivbrain.setImageResource(R.drawable.brainwave_bluetooth_off);
            Log.v("HelloEEG", "bluetooth off");
            tvbluetooth.setText("未開啟藍芽");
            btnRefresh.setEnabled(true);
        }

    }

    void initView(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Log.e("screenWidth",displayMetrics.widthPixels+"");
        Log.e("screenHeight",displayMetrics.heightPixels+"");
        int statusBarHeight2 = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight2 = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("WangJ", "status" + statusBarHeight2);

        //800 1232

        ivbrain = (ImageView)findViewById(R.id.ivblutooth);
        tvbluetooth = (TextView)findViewById(R.id.tvbluetooth);
        btnRefresh = (ImageButton)findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blutoothSetting();
            }
        });

        etId = (EditText) findViewById(R.id.etId);
        btnLogin = (Button)findViewById(R.id.btnEnd);
        etId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i("DONE","Enter pressed");
                    if(etId.getText().toString().equals("setting")){
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        etId.setText("");

                    }
                    else if(!etId.getText().toString().equals("")){
                        tgDevice.close();
                        fileStorage fs = new fileStorage();
                        fs.createDirectory();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String currentDateandTime = sdf.format(new Date());
                        fs.createFile(etId.getText().toString()+currentDateandTime);

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("ID",etId.getText().toString()+"_"+currentDateandTime);
                        bundle.putString("ActivityName",MainActivity.this.getClass().getSimpleName().toString());
                        intent.putExtras(bundle);
                        intent.setClass(MainActivity.this, RedirectActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etId.getText().toString().equals("setting")){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SettingActivity.class);
                    startActivity(intent);

                }
                else if(!etId.getText().toString().equals("")){
                    tgDevice.close();
                    Log.d("close","true");
                    fileStorage fs = new fileStorage();
                    fs.createDirectory();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    fs.createFile(etId.getText().toString()+currentDateandTime);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",etId.getText().toString()+"_"+currentDateandTime);
                    bundle.putString("ActivityName",MainActivity.this.getClass().getSimpleName().toString());
                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.this, RedirectActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        });

    }
    void loadSetting(){
        fileStorage fs = new fileStorage();
        if(!fs.checkfile("setting.txt")) {
            fs.createFile("setting");
            fs.setContinueWrite(false);
            String content = "60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n";
            fs.writeFile("setting", content);
            Log.e("file","loading default setting");
        }
        Intent enabler=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(enabler);

    }
    void upDateList(){
        fileStorage fs = new fileStorage();
        if(!fs.checkfile("updateList.txt")) {
            fs.createFile("setting");
            fs.setContinueWrite(false);
            String content = "60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n60\r\n";
            fs.writeFile("setting", content);
            Log.e("file","loading default setting");
        }
    }



}

