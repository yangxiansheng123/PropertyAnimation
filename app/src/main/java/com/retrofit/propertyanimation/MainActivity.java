package com.retrofit.propertyanimation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.retrofit.propertyanimation.activity.AnimalSetActivity;
import com.retrofit.propertyanimation.activity.LoadingBounceActivity;
import com.retrofit.propertyanimation.activity.RotateMerginExpandActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    protected Button button;
    protected Button btnBounceAnimal;
    protected Button btnAnimalSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            startActivity(new Intent(this, RotateMerginExpandActivity.class));
        } else if (view.getId() == R.id.btn_bounce_animal) {

            startActivity(new Intent(this, LoadingBounceActivity.class));
        } else if (view.getId() == R.id.btn_animal_set) {

            startActivity(new Intent(this, AnimalSetActivity.class));
        }
    }

    private void initView() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(MainActivity.this);
        btnBounceAnimal = (Button) findViewById(R.id.btn_bounce_animal);
        btnBounceAnimal.setOnClickListener(MainActivity.this);
        btnAnimalSet = (Button) findViewById(R.id.btn_animal_set);
        btnAnimalSet.setOnClickListener(MainActivity.this);
    }
}
