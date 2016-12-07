package com.escodro.viittaus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.escodro.viittaus.activity.ChronusActivity;
import com.escodro.viittaus.activity.PlayerActivity;
import com.escodro.viittaus.activity.RadarActivity;
import com.escodro.viittaus.activity.SpectreActivity;
import com.escodro.viittaus.adapter.MainRecyclerAdapter;
import com.escodro.viittaus.model.CustomView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewListFragment extends Fragment {

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    private List<CustomView> mViewList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewList = new ArrayList<>();
        populateViewList();
        setupRecyclerView();
    }

    private void populateViewList() {
        mViewList.add(new CustomView(
                R.drawable.ic_player_view,
                "PlayerView",
                "A view to simulate a player",
                PlayerActivity.class));

        mViewList.add(new CustomView(R.drawable.ic_spectre_view,
                "SpectreView",
                "Simulates a audio spectrum",
                SpectreActivity.class));

        mViewList.add(new CustomView(R.drawable.ic_radar_view,
                "Radar",
                "Simulates a radar",
                RadarActivity.class));

        mViewList.add(new CustomView(R.drawable.ic_chronus_view,
                "ChronusView",
                "A view to simulate a clock",
                ChronusActivity.class));
    }

    private void setupRecyclerView() {
        final MainRecyclerAdapter adapter = new MainRecyclerAdapter(getContext(), mViewList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        final DividerItemDecoration itemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                layoutManager.getOrientation());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(itemDecoration);
    }
}
