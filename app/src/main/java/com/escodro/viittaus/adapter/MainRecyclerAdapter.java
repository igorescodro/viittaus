package com.escodro.viittaus.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.escodro.viittaus.R;
import com.escodro.viittaus.databinding.ViewListItemBinding;
import com.escodro.viittaus.model.CustomView;
import com.escodro.viittaus.viewmodel.ViewViewModel;

import java.util.List;

public class MainRecyclerAdapter extends
        RecyclerView.Adapter<MainRecyclerAdapter.BindingHolder> {

    private final Context mContext;
    private final List<CustomView> mViewList;

    public MainRecyclerAdapter(Context context, List<CustomView> viewList) {
        mContext = context;
        mViewList = viewList;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.view_list_item,
                parent,
                false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final ViewListItemBinding binding = holder.binding;
        binding.setViewModel(new ViewViewModel(mContext, mViewList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mViewList.size();
    }

    class BindingHolder extends RecyclerView.ViewHolder {

        private ViewListItemBinding binding;

        BindingHolder(ViewListItemBinding binding) {
            super(binding.rlMenuBackground);
            this.binding = binding;
        }
    }
}
