package com.example.loveyoplus.myapplication;

import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by loveyoplus on 2017/3/25.
 */

public class listProcess {
    ArrayList<String> delta;
    ArrayList<String> theta;
    ArrayList<String>lalp;
    ArrayList<String>halp;
    ArrayList<String>lbeta;
    ArrayList<String>hbeta;
    ArrayList<String>lgam;
    ArrayList<String>mgam;
    ArrayList<String>attention;
    ArrayList<String>gsr;
    ArrayList<String>emg;
    ArrayList<String>pulse;

    String  student_id,timestamp,ques_id,ques_time,do_right,do_wrong,sound_wrong,sound_right;


    public listProcess(){
        delta = new ArrayList<>();
        theta = new ArrayList<>();
        lalp = new ArrayList<>();
        halp = new ArrayList<>();
        lbeta = new ArrayList<>();
        hbeta = new ArrayList<>();
        lgam = new ArrayList<>();
        mgam = new ArrayList<>();
        attention = new ArrayList<>();
        gsr = new ArrayList<>();
        emg = new ArrayList<>();
        pulse = new ArrayList<>();
    }
    public synchronized void addAttention(String attention){
        this.attention.add(attention);
    }
    public synchronized void addArray(String delta,String theta,String lalp,String halp,String lbeta,String hbeta,String lgam,String mgam){
        this.delta.add(delta);
        this.theta.add(theta);
        this.lalp.add(lalp);
        this.halp.add(halp);
        this.lbeta.add(lbeta);
        this.hbeta.add(hbeta);
        this.lgam.add(lgam);
        this.mgam.add(mgam);
    }
    public synchronized void addGSR(String gsr){

        gsr=gsr.replaceAll("\r","");
        this.gsr.add(gsr);


    }
    public synchronized void addEMG(String emg){
        emg=emg.replaceAll("\r","");
        this.emg.add(emg);
    }
    public synchronized void addPULSE(String pulse){
        pulse=pulse.replaceAll("\r","");
        this.pulse.add(pulse);
    }
    public synchronized void setInitial(String student_id,String timestamp,String ques_id,String ques_time,String do_right,String do_wrong,String sound_right,String sound_wrong){
        this.student_id  = student_id;
        this.timestamp = timestamp;
        this.ques_id = ques_id;
        this.ques_time = ques_time;
        this.do_right = do_right;
        this.do_wrong = do_wrong;
        this.sound_wrong = sound_wrong;
        this.sound_right = sound_right;
    }
    public synchronized void setInitial(String student_id){
        this.student_id = student_id;
    }
    public synchronized String printAll(){
        Map<String, String> data = new HashMap<>();
        data.put("student_id",student_id);
        //data.put("timestamp",timestamp);
        data.put("ques_id",ques_id);
        data.put("ques_time",ques_time);
        data.put("do_right",do_right);
        data.put("do_wrong",do_wrong);
        data.put("sound_right",sound_right);
        data.put("sound_wrong",sound_wrong);

        data.put("delta",this.delta.toString());

        data.put("theta",this.theta.toString());

        data.put("lalp",this.lalp.toString());

        data.put("halp",this.halp.toString());

        data.put("lbeta",this.lbeta.toString());

        data.put("hbeta",this.hbeta.toString());

        data.put("lgam",this.lgam.toString());

        data.put("mgam",this.mgam.toString());

        data.put("attention",this.attention.toString());

        data.put("gsr", this.gsr.toString());

        data.put("emg", this.emg.toString());

        data.put("pulse", this.pulse.toString());


        Gson gson = new Gson();


        return gson.toJson(data)+"\r\n";
    }
    synchronized void  removeAllData(){
        this.delta.clear();
        this.theta.clear();
        this.lalp.clear();
        this.halp.clear();
        this.lbeta.clear();
        this.hbeta.clear();
        this.lgam.clear();
        this.mgam.clear();
        this.attention.clear();
        this.gsr.clear();
        this.emg.clear();
        this.pulse.clear();
    }

    synchronized HashMap<String,String> returnMapWithAllData(){
        Map<String, String> data = new HashMap<>();
        data.put("student_id",student_id);

        data.put("delta",this.delta.toString());

        data.put("theta",this.theta.toString());

        data.put("lalp",this.lalp.toString());

        data.put("halp",this.halp.toString());

        data.put("lbeta",this.lbeta.toString());

        data.put("hbeta",this.hbeta.toString());

        data.put("lgam",this.lgam.toString());

        data.put("mgam",this.mgam.toString());

        data.put("attention",this.attention.toString());

        data.put("gsr", this.gsr.toString());

        data.put("emg", this.emg.toString());

        data.put("pulse", this.pulse.toString());

        return (HashMap<String,String>)data;
    }

}

