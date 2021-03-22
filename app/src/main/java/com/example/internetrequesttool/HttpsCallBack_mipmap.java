package com.example.internetrequesttool;

import android.graphics.Bitmap;

public interface HttpsCallBack_mipmap {
    //获取成功调用
    void onFinish(Bitmap response);
    //获取失败调用
    void onError(Exception e);
}
