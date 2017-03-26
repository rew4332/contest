package com.example.loveyoplus.myapplication;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by loveyoplus on 2017/3/10.
 */

public class fileStorage {
    String fileName = "";
    String directoryName="/sdcard/brainWaveData";
    File newDirectory=null;
    String content = "";
    Boolean continueWrite=true;
    File myFile;
    public void  createDirectory(String directoryName){
        this.directoryName= "/sdcard/"+directoryName;
        this.newDirectory= new File(directoryName);
        newDirectory.mkdir();
    }
    public void  createDirectory(){
        this.directoryName= directoryName;
        this.newDirectory= new File(directoryName);
        newDirectory.mkdir();
    }
    public void createFile(String fileName){
        this.fileName= fileName;
        if(newDirectory==null)
            this.newDirectory= new File(directoryName);
        myFile = new File(newDirectory,fileName+".txt");

    }
    public void setContinueWrite(Boolean bool){
        this.continueWrite=bool;

    }
    public void writeFile(String ID,String content){

        this.content=content;
        try {
            Log.e("file","write");
            createFile(ID);
            FileOutputStream fOut = new FileOutputStream(myFile,continueWrite);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(content);
            myOutWriter.flush();
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            Log.e("file","exception");
            e.printStackTrace();
        }
    }
    public String readFile(){
        StringBuilder text = new StringBuilder();
        String myData="";

        try {
            FileInputStream fis = new FileInputStream(myFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData +"\r\n"+ strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
    }
    public HashMap<String, String> readFile2Map(int i){
        String str=readFile().split("\r\n")[i];
        // use properties to restore the map
        Properties props = new Properties();
        try {
            props.load(new StringReader(str.substring(1, str.length() - 1).replace(", ", "\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map2 = new HashMap<String, String>();
        for (Map.Entry<Object, Object> e : props.entrySet()) {
            map2.put((String)e.getKey(), (String)e.getValue());
        }
        return (HashMap<String, String>) map2;
        /*
        Log.e("delta",((HashMap<String, String>) map2).get("delta"));
        Log.e("student_id",((HashMap<String, String>) map2).get("student_id"));
        Log.e("ques_id",((HashMap<String, String>) map2).get("ques_id"));*/


    }

    //File path = Environment.getExternalStorageDirectory();
    public boolean checkfile(String str){
        createDirectory();

        File file = new File(newDirectory.toString()+"/"+str);
        Log.e("checkfile:",newDirectory+"/"+str);
        if(file.exists())return true;
        return false;
    }



    FileOutputStream writer = null;




}
