package com.example.loveyoplus.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by loveyoplus on 2017/3/29.
 */

public class UpdateActivity extends AppCompatActivity {
    ListView list;
    public static String[] names;
    public static CheckBox[] cbList,cbList2;
    MyAdapter adapter = null;
    fileStorage fs;
    String msg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatemanagement);
        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); //隱藏狀態
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        list= (ListView) findViewById(R.id.list);
        adapter = new MyAdapter(this);


        loadUpdateFile();



        findViewById(R.id.btnupdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileStorage fs1=new fileStorage();

                fs1.setContinueWrite(false);
                fs1.writeFile("update","");
                for(int i=0;i<names.length;i++){
                    Log.e("names",names[i]);
                    if(!cbList[i+1].isChecked()){
                        fileStorage fs=new fileStorage();
                        fs.setContinueWrite(true);
                        fs.writeFile("update",names[i]+"\r\n");

                    }
                    else{
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        fileStorage fs = new fileStorage();
                        fs.createDirectory();
                        String filename=names[i];
                        fs.createFile(filename);
                        int count=0;
                        for(int j=0;j<10;j++){
                            if(count>30)break;
                            count++;
                            try {
                                Map<String, String> data;
                                // Create the data structure to transfer.

                                NetCon conn = new NetCon();
                                data = fs.readFile2Map(j+1);
                                Log.e("map",data+"");
                                // Send Request
                                msg = conn.SetJson(data).SetUrl("http://140.116.179.52/dbinsert.php").Execute();
                                Log.e("msg ",msg);
                                if(!msg.equals("OK"))j--;

                                try {
                                    Thread.sleep(700);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            } catch (IOException e) {
                                msg = e.getMessage();
                                Log.e("exception",msg);
                            }
                        }



                    }
                }
                loadUpdateFile();

            }
        });












    }


    public void loadUpdateFile(){
        fs = new fileStorage();
        fs.createFile("update");
        //fs.writeFile("update","410073025_12:15fd3s65 \r\n");
        String list2[]=fs.readFile().split("\r\n");
        names = new String[list2.length-1];
        for(int i=0;i<names.length;i++){
            names[i]=list2[i+1];
        }
        cbList = new CheckBox[names.length+1];
        cbList2 = new CheckBox[names.length+1];
        for(int i=0;i<cbList.length;i++){
            cbList[i] = new CheckBox(this);

        }
        list.setAdapter(adapter);


        list.setOnItemClickListener(ilistener);
    }
    private ListView.OnItemClickListener ilistener = new ListView.OnItemClickListener(){


        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            Log.e("len of names",names.length+"");
            Log.e("len of checkbox",cbList.length+"");
            Log.e("the index of check box",arg2+"");



            if(arg2==0)
            {
                cbList[arg2].setChecked(!cbList[arg2].isChecked());

                for(int i=1;i<names.length+1;i++){
                    cbList[i].setChecked(cbList[arg2].isChecked());
                    if(cbList2[i]!=null)
                        cbList2[i].setChecked(cbList[arg2].isChecked());
                }
                cbList2[arg2].setChecked(cbList[arg2].isChecked());

            }
            else{
                cbList2[arg2].setChecked(!cbList[arg2].isChecked());
                cbList[arg2].setChecked(!cbList[arg2].isChecked());


            }

            for(int i=0;i<names.length+1;i++){

                if(cbList[i].isChecked()){

                    Log.e("CheckBox "+(i),"true");
                }
                else{
                    Log.e("CheckBox "+(i),"false");
                }
            }
        }



    };
}
class MyAdapter extends BaseAdapter{

    LayoutInflater myInflater;
    public MyAdapter(Context c) {
        myInflater = LayoutInflater.from(c);

    }

    @Override
    public int getCount() {
        //Log.e("getCount",UpdateActivity.names.length+1+"");
        return UpdateActivity.names.length+1;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("getView position",position+"");

        convertView = myInflater.inflate(R.layout.mylist,parent,false);
        TextView tv =(TextView) convertView.findViewById(R.id.textView7);
        TextView tv2=(TextView) convertView.findViewById(R.id.textView8);
        UpdateActivity.cbList2[position] = (CheckBox) convertView.findViewById(R.id.checkBox);
        UpdateActivity.cbList2[position].setChecked(UpdateActivity.cbList[position].isChecked());
        UpdateActivity.cbList2[position].setClickable(false);
        //UpdateActivity.cbList[position].setChecked(false);
        if(position==0){
            tv.setText("學號");
            tv2.setText("測驗時間");
        }
        else {
            tv.setText(UpdateActivity.names[position-1].split("_")[0]);
            tv2.setText(UpdateActivity.names[position-1].split("_")[1]+UpdateActivity.names[position-1].split("_")[2]);

        }
        return convertView;
    }
}
