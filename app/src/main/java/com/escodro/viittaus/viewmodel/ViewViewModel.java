package com.escodro.viittaus.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.escodro.viittaus.model.CustomView;

public class ViewViewModel extends BaseObservable {

    private Context mContext;
    private CustomView mCustomView;

    public ViewViewModel(Context context,
                         CustomView customView) {
        mContext = context;
        mCustomView = customView;
    }

    public int getViewIcon() {
        return mCustomView.getIconId();
    }

    public String getViewName() {
        return mCustomView.getName();
    }

    public String getViewDescription() {
        return mCustomView.getDescription();
    }

    public View.OnClickListener onItemClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(mContext, mCustomView.getActivityClass());
                mContext.startActivity(intent);
            }
        };
    }
}
