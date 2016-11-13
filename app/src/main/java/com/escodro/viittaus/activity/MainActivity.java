package com.escodro.viittaus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.escodro.viittaus.R;
import com.escodro.viittaus.view.RadarView;

/**
 * Created by IgorEscodro on 12/11/16.
 */

public class MainActivity extends AppCompatActivity {

    private RadarView mRadarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mRadarView = (RadarView) findViewById(R.id.radar_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRadarView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRadarView.pause();
    }
}
