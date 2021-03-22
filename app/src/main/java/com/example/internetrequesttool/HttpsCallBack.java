package com.example.internetrequesttool;

public interface HttpsCallBack {
    //本来想写个请求图片的，发现请求来的图片不知道怎么处理T_T
    //获取成功调用
    void onFinish(String response);
    //获取失败调用
    void onError(Exception e);
}
