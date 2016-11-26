package com.escodro.viittaus.view;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;

import com.escodro.viittaus.R;
import com.escodro.viittaus.activity.PlayerActivity;
import com.escodro.viittaus.activity.RadarActivity;
import com.escodro.viittaus.activity.SpectreActivity;

/**
 * Enum to represent the custom views.
 * <p/>
 * Created by IgorEscodro on 19/11/16.
 */
public enum CustomView {

    /**
     * Enum to represent the {@link PlayerView}.
     */
    PLAYER_VIEW(
            R.drawable.ic_player_view,
            "PlayerView",
            "A view to simulate a player",
            PlayerActivity.class),

    /**
     * Enum to represent the {@link SpectreView}.
     */
    SPECTRE_VIEW(
            R.drawable.ic_spectre_view,
            "SpectreView",
            "Simulates a audio spectrum",
            SpectreActivity.class),

    /**
     * Enum to represent the {@link RadarView}.
     */
    RADAR_VIEW(
            R.drawable.ic_radar_view,
            "Radar",
            "Simulates a radar",
            RadarActivity.class);

    /**
     * Field to represent the resource id of the drawable.
     */
    private final int mIconId;

    /**
     * Field to represent the name of the custom view.
     */
    private final String mName;

    /**
     * Field to represent the description of the custom view.
     */
    private final String mDescription;

    /**
     * Field to represent the {@link AppCompatActivity} of the custom view.
     */
    private final Class<? extends AppCompatActivity> mActivity;

    /**
     * Default constructor.
     *
     * @param iconId   resource id
     * @param name     view name
     * @param desc     view description
     * @param activity activity with the view
     */
    CustomView(@DrawableRes int iconId, String name, String desc, Class<? extends AppCompatActivity>
            activity) {
        mIconId = iconId;
        mName = name;
        mDescription = desc;
        mActivity = activity;
    }

    /**
     * Get the resource id of the drawable.
     *
     * @return resource id
     */
    public int getIconId() {
        return mIconId;
    }

    /**
     * Get the view name.
     *
     * @return view name
     */
    public String getName() {
        return mName;
    }

    /**
     * Get the view description.
     *
     * @return view description
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Get the activity with the view.
     *
     * @return activity with the view
     */
    public Class<? extends AppCompatActivity> getActivity() {
        return mActivity;
    }
}
