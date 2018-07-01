package com.example.dell.stepcountdemo;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.dell.stepcountdemo.service.StepService;
import com.example.dell.stepcountdemo.service.UpdateUiCallBack;

/**
 * 创建日期：2018/6/20
 * 作者:baiyang
 */
public class SMApp extends Application {
    public static SMApp instance=null;
    private boolean isBind;
    private StepService bindService;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
//        Intent intent = new Intent(instance, StepService.class);
//        isBind = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    //和绷定服务数据交换的桥梁，可以通过IBinder service获取服务的实例来调用服务的方法或者数据
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService.LcBinder lcBinder = (StepService.LcBinder) service;
            bindService = lcBinder.getService();
            bindService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(final int stepCount) {

                }
            });
            lcBinder.startStep();//开始计时
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
