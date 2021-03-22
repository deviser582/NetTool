package com.example.internetrequesttool;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class HttpsUtil {
    //自定义线程池没搞懂T_T
    public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    public static void getNet(final String address,final HttpsCallBack listener) {
        //使用线程池开启线程
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.connect();
                    InputStream in = connection.getInputStream();

                    //数据处理
                    StringBuilder sb = new StringBuilder();
                    String oneline;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    try{
                        while ((oneline = reader.readLine()) != null){
                            sb.append(oneline).append('\n');
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            in.close();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //接口方法，在调用中重写
                    listener.onFinish(sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
    }

    public static void postNet(String mUrl, HashMap<String, String> params, final HttpsCallBack listener) {
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(mUrl);
                    HttpURLConnection connection = (HttpURLConnection)
                            url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    StringBuilder dataToWrite = new StringBuilder();
                    for (String key : params.keySet()) {
                        dataToWrite.append(key).append("=").append(params.get(key)).append("&");
                    }
                    connection.connect();
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(dataToWrite.substring(0,
                            dataToWrite.length() - 1).getBytes());
                    InputStream in = connection.getInputStream();

                    //数据处理
                    StringBuilder sb = new StringBuilder();
                    String oneline;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    try{
                        while ((oneline = reader.readLine()) != null){
                            sb.append(oneline).append('\n');
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            in.close();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    String responseData = sb.toString();
                    //接口方法，在调用中重写
                    listener.onFinish(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
