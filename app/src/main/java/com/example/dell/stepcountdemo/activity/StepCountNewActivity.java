package com.example.dell.stepcountdemo.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.dell.stepcountdemo.R;

/**
 * 创建日期：2018/6/21
 * 作者:baiyang
 */
public class StepCountNewActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "StepCountNewActivity";
    /**
     * 当前步数
     */
    private int nowBuSu = 0;
    /**
     * 传感器管理对象
     */
    private SensorManager sensorManager;
    /**
     * 计步传感器类型  Sensor.TYPE_STEP_COUNTER或者Sensor.TYPE_STEP_DETECTOR
     */
    private static int stepSensorType = -1;
    private TextView tv_count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        tv_count = (TextView) findViewById(R.id.tv_count);
        int versionCodes = Build.VERSION.SDK_INT;//取得SDK版本
        if (versionCodes >= 19) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startStepDetector();
                }
            }).start();
        }
    }

    /**
     * 选择计步数据采集的传感器
     * SDK大于等于19，开启计步传感器，小于开启加速度传感器
     */
    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        //获取传感器管理类
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        //SDK版本大于等于19开启计步传感器
        addCountStepListener();

    }

    /**
     * 启动计步传感器计步
     */
    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_COUNTER;
            sensorManager.registerListener(StepCountNewActivity.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.i("计步传感器类型", "Sensor.TYPE_STEP_COUNTER");
        } else if (detectorSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_DETECTOR;
            sensorManager.registerListener(StepCountNewActivity.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensorType == Sensor.TYPE_STEP_COUNTER) {
            //获取当前传感器返回的临时步数
            int tempStep = (int) event.values[0];
            nowBuSu = tempStep;

        }
        //这种类型的传感器触发一个事件每次采取的步骤是用户。只允许返回值是1.0,为每个步骤生成一个事件。
        // 像任何其他事件,时间戳表明当事件发生(这一步),这对应于脚撞到地面时,生成一个高加速度的变化。
        else if (stepSensorType == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                nowBuSu++;
            }
        }
        Log.e(TAG, "onSensorChanged nowBuSu: " + nowBuSu);
        tv_count.setText(nowBuSu + "");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null)
        sensorManager.unregisterListener(this);
    }
}
