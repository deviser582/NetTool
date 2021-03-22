package com.example.internetrequesttool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView data;
    Button send;
    MyHandler myHandler = new MyHandler(MainActivity.this);

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivty;

        public MyHandler(MainActivity activity) {
            mActivty = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivty.get();
            super.handleMessage(msg);
            if (activity != null) {
                activity.data.setText((String) msg.obj);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = findViewById(R.id.sendRequest);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendrequset("https://v1.alapi.cn/api/eventHitory",MainActivity.this);
            }
        });

        data = findViewById(R.id.data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }


    //回调处理方法
    private void sendrequset(String url, MainActivity activity){
        HttpsUtil.getNet(url, new HttpsCallBack() {
            @Override
            public void onFinish(String response) {
                Message msg = new Message();
                msg.obj = response;
                myHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Message message=new Message();
                message.obj = "网络错误";
                myHandler.sendMessage(message);
            }
        });
    }

}