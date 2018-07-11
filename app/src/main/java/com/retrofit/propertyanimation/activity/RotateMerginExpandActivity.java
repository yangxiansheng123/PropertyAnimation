package com.retrofit.propertyanimation.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.retrofit.propertyanimation.R;
import com.retrofit.propertyanimation.animal.SplashView;

import java.util.Timer;
import java.util.TimerTask;

public class RotateMerginExpandActivity extends AppCompatActivity {

    protected SplashView splash;
    private Timer time;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_mergin_expand);
        initView();
        startLoadData();

    }

    private void initView() {
        splash = (SplashView) findViewById(R.id.splash);
    }

    private void startLoadData() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //数据加载完毕，开启后面的两个动画
                splash.splashAndDisapper();
            }
        }, 3000);
    }
}
