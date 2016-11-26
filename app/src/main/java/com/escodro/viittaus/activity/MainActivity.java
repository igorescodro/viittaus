package com.escodro.viittaus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.escodro.viittaus.R;
import com.escodro.viittaus.adapter.MainRecyclerAdapter;
import com.escodro.viittaus.view.CustomView;

import java.util.Arrays;
import java.util.List;

import rx.Observer;

/**
 * Activity with the custom view list.
 * <p/>
 * Created by IgorEscodro on 12/11/16.
 */

public class MainActivity extends AppCompatActivity implements Observer<CustomView> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * Initialize the activity components.
     */
    private void init() {
        final List<CustomView> viewList = Arrays.asList(CustomView.values());
        final MainRecyclerAdapter adapter = new MainRecyclerAdapter(viewList);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        final DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(itemDecoration);

        adapter.getPositionClicks().subscribe(this);
    }

    @Override
    public void onCompleted() {
        //Do nothing.
    }

    @Override
    public void onError(Throwable e) {
        //Do nothing.
    }

    @Override
    public void onNext(CustomView customView) {
        final Intent intent = new Intent(this, customView.getActivity());
        startActivity(intent);
    }
}
