package com.escodro.viittaus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.escodro.viittaus.R;
import com.escodro.viittaus.view.RadarView;

/**
 * {@link AppCompatActivity} with the {@link RadarView}.
 * <p/>
 * Created by IgorEscodro on 20/11/16.
 */

public class RadarActivity extends AppCompatActivity {

    /**
     * {@link RadarView} reference.
     */
    private RadarView mRadarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
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
