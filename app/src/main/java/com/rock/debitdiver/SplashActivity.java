package com.rock.debitdiver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hanks.htextview.HTextView;

import me.itangqi.waveloadingview.WaveLoadingView;

public class SplashActivity extends AppCompatActivity {

    WaveLoadingView mWaveLoadingView;
    HTextView txtSplash;
    Button btReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mWaveLoadingView = findViewById(R.id.loadingIndicator);
        txtSplash = findViewById(R.id.titleSplash);
        btReady = findViewById(R.id.btReady);
        mWaveLoadingView.setProgressValue(80);
        mWaveLoadingView.setAmplitudeRatio(60);
        mWaveLoadingView.setAnimDuration(3000);
        txtSplash.animateText("Debt Diver");
        txtSplash.animate();
        btReady.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowButton();
            }
        }, 3000);

        btReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickReady();
            }
        });
//        mWaveLoadingView.pauseAnimation();
//        mWaveLoadingView.resumeAnimation();
//        mWaveLoadingView.cancelAnimation();
//        mWaveLoadingView.startAnimation();
    }

    public void ShowButton()
    {
        mWaveLoadingView.setCenterTitle("Tap below button");
        btReady.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInUp)
                .duration(350)
                .playOn(btReady);
    }

    public void OnClickReady()
    {
        Intent LoginIntent = new Intent(this, LoginActivity.class);
        LoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(LoginIntent);
        finish();
    }
}
