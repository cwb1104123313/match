package com.example.administrator.match.fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.administrator.match.R;
import com.example.administrator.match.adapter.EnvirAdapter;
import com.example.administrator.match.domain.EnvironmentalBean;
import com.example.administrator.match.until.CacheUntil;
import com.example.administrator.match.until.NetUntil;
import com.example.administrator.match.until.SQL_Environmental;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Fragment_environment extends Fragment{


    private TextView temp;
    private TextView humidity;
    private TextView light;
    private TextView co2;
    private TextView pm;
    private TextView status;
    private NetUntil netUntil;
    private GridView gridView;

    private final static int SEND_ENM=1;
    private final static int SEND_STATUS=2;

    private List<EnvironmentalBean> list=new ArrayList<>();
    private EnvirAdapter envirAdapter;

    private String ip;
    private String port;
    private SQL_Environmental sql_environmental;
    private SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_environment,null);

        gridView=view.findViewById(R.id.gridview);
        envirAdapter=new EnvirAdapter(getContext(),bean);
        gridView.setAdapter(envirAdapter);
        ip=CacheUntil.getString(getContext(),"url","192.168.1.101");
        port=CacheUntil.getString(getContext(),"port","8080");
        netUntil=new NetUntil();

        gridView.setOnItemClickListener(listener);

        sql_environmental=new SQL_Environmental(getActivity());
        database= sql_environmental.getWritableDatabase();
        return view;
    }

    private AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    };
    private List<EnvironmentalBean>beans=new ArrayList<>();
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
        if(list.size()==12){
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



    @Override
    public void onPause() {
        super.onPause();
        Log.e("eeee","pause");
        if(database!=null){
            database.close();
        }
    }

   @Override
   public void onResume() {
       super.onResume();
       Message message=handler.obtainMessage();
       message.what=0;
       handler.sendMessage(message);
   }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ee","onDrstroy()");
    }
}
