package com.escodro.viittaus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.escodro.viittaus.R;
import com.escodro.viittaus.view.ChronusView;

public class ChronusActivity extends AppCompatActivity {

    private ChronusView mChronusView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronus);

        mChronusView = (ChronusView) findViewById(R.id.chronus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChronusView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mChronusView.pause();
    }
}
