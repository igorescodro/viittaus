package com.escodro.viittaus.model;

import android.support.v7.app.AppCompatActivity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor(suppressConstructorProperties = true)
public class CustomView {

    @Getter
    @Setter
    private int iconId;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private Class<? extends AppCompatActivity> activityClass;
}
