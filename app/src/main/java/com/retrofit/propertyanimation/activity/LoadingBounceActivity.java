package com.retrofit.propertyanimation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.retrofit.propertyanimation.animal.FruitView;

/**
 * 弹跳
 */
public class LoadingBounceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_loading_bounce);
        setContentView(new FruitView(this));
    }
}
