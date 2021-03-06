package com.escodro.viittaus.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.escodro.viittaus.R;
import com.escodro.viittaus.view.SpectreView;

import java.util.Random;

public class SpectreActivity extends AppCompatActivity {

    private SpectreView mSpectreView;
    private Handler mHandler;

    /**
     * Runnable to add random values to the {@link SpectreView} and loop after 300 milliseconds.
     */
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mSpectreView.add(getRandomNumber());
            mHandler.postDelayed(mRunnable, 300);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spectre);

        mHandler = new Handler();
        mHandler.post(mRunnable);

        mSpectreView = (SpectreView) findViewById(R.id.spectre);

    }

    private int getRandomNumber() {
        final Random random = new Random();
        return random.nextInt(100 - 30) + 30;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpectreView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSpectreView.pause();
    }
}
