package com.retrofit.propertyanimation.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.retrofit.propertyanimation.R;

/**
 * 动画集合
 */
public class AnimalSetActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button btnAlpha;
    protected Button btnTAS;
    protected Button btnInterpolator;
    protected ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_animal_set);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_alpha) {
            ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(imageView, "alpha", 1.0f, 0.3f, 1.0f);
            alphaAnim.setDuration(1000);
            alphaAnim.setStartDelay(300);
            alphaAnim.start();
        } else if (view.getId() == R.id.btn_t_a_s) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "translationX", 0f, 500F);

            ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);

            ObjectAnimator animator3 = ObjectAnimator.ofFloat(imageView, "scaleX", 0f, 2f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(500);

            //animatorSet.play(animator3).with(animator2).after(animator1);//链式调用顺序
            //animatorSet.playTogether(animator1,animator2,animator3);//一起执行
            //顺序执行
            animatorSet.playSequentially(animator1, animator2, animator3);
            animatorSet.start();
        } else if (view.getId() == R.id.btn_Interpolator) {
            ValueAnimator animator = new ValueAnimator();
            animator.setDuration(3000);
            animator.setObjectValues(new PointF(0, 0));
            final PointF point = new PointF();
            //估值
            animator.setEvaluator(new TypeEvaluator() {
                @Override
                public Object evaluate(float fraction, Object startValue, Object endValue) {
                    point.x = 100f * (fraction * 5);
                    // y=vt=1/2*g*t*t(重力计算)
                    point.y = 0.5f * 98f * (fraction * 5) * (fraction * 5);
                    return point;
                }
            });

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    imageView.setX(point.x);
                    imageView.setY(point.y);
                }
            });

            animator.start();
        }
    }

    private void initView() {
        btnAlpha = (Button) findViewById(R.id.btn_alpha);
        btnAlpha.setOnClickListener(AnimalSetActivity.this);
        btnTAS = (Button) findViewById(R.id.btn_t_a_s);
        btnTAS.setOnClickListener(AnimalSetActivity.this);
        btnInterpolator = (Button) findViewById(R.id.btn_Interpolator);
        btnInterpolator.setOnClickListener(AnimalSetActivity.this);
        imageView = (ImageView) findViewById(R.id.imageView);
    }
}
