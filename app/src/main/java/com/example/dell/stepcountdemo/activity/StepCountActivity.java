package com.example.dell.stepcountdemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.dell.stepcountdemo.R;
import com.example.dell.stepcountdemo.entity.MessageEntity;
import com.example.dell.stepcountdemo.service.StepService;
import com.example.dell.stepcountdemo.service.UpdateUiCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 创建日期：2018/6/20
 * 作者:baiyang
 */
public class StepCountActivity extends AppCompatActivity {
    private static final String TAG = "StepCountActivity";
    private TextView tv_count;
    private boolean isBind;
    private StepService bindService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        EventBus.getDefault().register(this);
        tv_count = (TextView) findViewById(R.id.tv_count);
        initVariables();
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
                    Log.e(TAG, "updateUi stepCount " + stepCount);
                    //当前接收到stepCount数据，就是最新的步数
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //这个获取的是系统的步数
                            tv_count.setText(stepCount + "");
                        }
                    });
                }
            });
            lcBinder.startStep();//开始计时
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void initVariables() {
        int versionCodes = Build.VERSION.SDK_INT;//取得SDK版本
        Intent intent = new Intent(this, StepService.class);
        if (versionCodes >= 19) {
            //大于19用绑定方式
            isBind = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //小于19开启服务
            startService(intent);
        }



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (isBind) {
            this.unbindService(serviceConnection);
        }
        Log.e(TAG, "Activity 销毁 ");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(MessageEntity msg) {
        if (msg.what == 100) {
            tv_count.setText((Integer) msg.obj + "");
        }
    }
}
