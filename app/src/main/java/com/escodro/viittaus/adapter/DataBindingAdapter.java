package com.escodro.viittaus.adapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

@SuppressWarnings("unused")
public class DataBindingAdapter {

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
