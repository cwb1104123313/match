package com.example.administrator.match.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.administrator.match.adapter.EnvirAdapter;
import com.example.administrator.match.domain.EnvironmentalBean;
import com.example.administrator.match.until.CacheUntil;
import com.example.administrator.match.until.NetUntil;
import com.example.administrator.match.until.SQL_Environmental;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EnvironmentalService extends Service {

    private String ip;
    private String port;
    private NetUntil netUntil;
    private EnvirAdapter envirAdapter;
    private SQL_Environmental sql_environmental;
    private SQLiteDatabase database;
    public EnvironmentalService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ip= CacheUntil.getString(this,"url","192.168.1.101");
        port=CacheUntil.getString(this,"port","8080");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private List<EnvironmentalBean> beans=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    netUntil.getData("{}","http://"+ip+":"+port+"/transportservice/type/jason/action/GetAllSense.do",handler);
                    Message message= handler.obtainMessage();
                    message.what=0;
                    handler.sendMessageDelayed(message,5000);
                    break;
                case 1:
                    envirAdapter.notifyDataSetChanged();
                    break;
                case NetUntil.NET_GETDATA:
                    //beans.add()
                    setJson(msg.obj.toString());
                    break;
            }
        }
    };



    EnvironmentalBean bean =new EnvironmentalBean();
    private void setJson(String result){
        try {
            String serverinfo= new JSONObject(result).getString("serverinfo");
            JSONObject info= new JSONObject(serverinfo);
            Message message=handler.obtainMessage();
            if(info.isNull("Status")){
                bean.setLightIntensity(info.getInt("LightIntensity"));
                bean.setHumidity(info.getInt("humidity"));
                bean.setTemperature(info.getInt("temperature"));
                bean.setCo2(info.getInt("co2"));
                bean.setPm(info.getInt("pm2.5"));
                netUntil.getData("{\"RoadId\" : 1}","http://"+ip+":"+port+"/transportservice/type/jason/action/GetRoadStatus.do",handler);
            }else{
                bean.setStatus(info.getInt("Status"));
                message.what=1;
                message.obj=bean;
                handler.sendMessage(message);

                beans.add(bean);
                calculationAvg();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void calculationAvg() {
        if(beans.size()==12){
            int pm=0;
            int co2=0;
            int LightIntensity=0;
            int humidity=0;
            int temperature=0;
            int Status=0;
            for(EnvironmentalBean bean:beans){
                pm+=bean.getPm();
                co2+=bean.getCo2();
                LightIntensity+=bean.getLightIntensity();
                humidity+=bean.getHumidity();
                temperature+=bean.getTemperature();
            }
            if(database.isOpen()){
                Long l=database.insert(SQL_Environmental.TABLENAME,null,getContentValues(new EnvironmentalBean(pm/12,co2/12,LightIntensity/12,humidity/12,temperature/12,Status/12)));
                Log.e("eee",l+"");
            }
            beans.clear();
        }
    }

    private ContentValues getContentValues(EnvironmentalBean bean){
        ContentValues values=new ContentValues();
        values.put("pm",bean.getPm());
        values.put("co2",bean.getCo2());
        values.put("LightIntensity",bean.getLightIntensity());
        values.put("humidity",bean.getHumidity());
        values.put("temperature",bean.getTemperature());
        values.put("Status",bean.getStatus());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date= simpleDateFormat.format(Calendar.getInstance().getTime());
        values.put("createDate",date);
        return values;
    };
}
