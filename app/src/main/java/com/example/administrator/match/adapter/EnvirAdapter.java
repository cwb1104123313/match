package com.example.administrator.match.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.match.R;
import com.example.administrator.match.domain.EnvironmentalBean;


import java.util.List;

public class EnvirAdapter extends BaseAdapter {

    private EnvironmentalBean bean;
    private Context context;
    public EnvirAdapter(Context context, EnvironmentalBean bean) {
        this.context=context;
        this.bean=bean;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(context, R.layout.envir_layout,null);
        }
            TextView type=convertView.findViewById(R.id.type);
            TextView data=convertView.findViewById(R.id.data);
            switch (position){
                case 0:
                    type.setText("空气温度");
                    data.setText(""+bean.getTemperature());
                    break;
                case 1:
                    type.setText("空气湿度");
                    data.setText(""+bean.getHumidity());
                    break;
                case 2:
                    type.setText("光照");
                    data.setText(""+bean.getLightIntensity());
                    break;
                case 3:
                    type.setText("CO2");
                    data.setText(""+bean.getCo2());
                    break;
                case 4:
                    type.setText("PM2.5");
                    data.setText(""+bean.getPm());
                    break;
                case 5:
                    type.setText("道路状态");
                    data.setText(""+bean.getStatus());
                    break;
            }

        return convertView;
    }
}
