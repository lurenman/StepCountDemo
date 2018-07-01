package com.example.dell.stepcountdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.stepcountdemo.activity.StepCountActivity;
import com.example.dell.stepcountdemo.activity.StepCountNewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_step_count;
    private Context mContext;
    private Button bt_step_count_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        bt_step_count = (Button) findViewById(R.id.bt_step_count);
        bt_step_count_new = (Button) findViewById(R.id.bt_step_count_new);
        bt_step_count.setOnClickListener(this);
        bt_step_count_new.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_step_count:
                Intent intent = new Intent(mContext, StepCountActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_step_count_new:
                Intent intent1 = new Intent(mContext, StepCountNewActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
