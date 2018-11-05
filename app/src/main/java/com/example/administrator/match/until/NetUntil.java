package com.example.administrator.match.until;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetUntil {

    public static final int NET_GETDATA=0x11;

    public void getData(String json, String url, final Handler handler) {
        OkHttpClient httpClient=new OkHttpClient();
        MediaType mediaType=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody=RequestBody.create(mediaType,json);
        Request request= new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("eeeeeee","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStreamReader is=new InputStreamReader(response.body().byteStream(),"utf-8");
                BufferedReader reader=new BufferedReader(is);
                String str="";
                String result="";
                while((str=reader.readLine())!=null){
                    result+=str;
                }
                Log.e("ss",result);
                Message message=handler.obtainMessage();
                message.what=NET_GETDATA;
                message.obj=result;
                handler.sendMessage(message);
            }
        });
    }
}
