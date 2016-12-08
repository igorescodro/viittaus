package com.escodro.viittaus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.escodro.viittaus.R;
import com.escodro.viittaus.fragment.ViewListFragment;

public class ViewListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addViewListFragment();
    }

    private void addViewListFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, new ViewListFragment())
                .commit();
    }
}
