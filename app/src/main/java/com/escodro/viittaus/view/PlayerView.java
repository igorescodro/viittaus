package com.escodro.viittaus.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.escodro.viittaus.R;
import com.escodro.viittaus.adapter.PlayerAdapter;

/**
 * Custom view to represent a audio player.
 * <p/>
 * Created by Igor Escodro on 13/5/2016.
 */
public class PlayerView extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    /**
     * {@link PlayerAdapter} reference.
     */
    private PlayerAdapter mAdapter;

    /**
     * Create a new instance of {@link PlayerView}.
     *
     * @param context application context
     */
    public PlayerView(Context context) {
        this(context, null);
    }

    /**
     * Create a new instance of {@link PlayerView}.
     *
     * @param context application context
     * @param attrs   attribute set
     */
    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            setWillNotDraw(false);
            init(context);
        }
    }

    /**
     * Inflates the view and sets the basic configuration.
     *
     * @param context application context
     */
    private void init(Context context) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.view_player, this);
        mAdapter = new PlayerAdapter(getContext(), view);

        final ViewTreeObserver treeObserver = getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(this);
    }

    /**
     * Resumes the {@link PlayerAdapter}.
     */
    public void onResume() {
        mAdapter.onResume();
    }

    /**
     * Pauses the {@link PlayerAdapter}.
     */
    public void onPause() {
        mAdapter.onPause();
    }

    /**
     * Destroys the {@link PlayerAdapter}.
     */
    public void onDestroy() {
        mAdapter.onDestroy();
    }

    /**
     * Destroys the {@link PlayerAdapter}.
     */
    public void onDestroy() {
        mAdapter.onDestroy();
    }

    /**
     * Sets the audio {@link Uri} to be played.
     *
     * @param uri {@link Uri} to be played
     */
    public void setAudioUri(Uri uri) {
        mAdapter.setAudioUri(uri);
    }

    /**
     * Sets the album cover to be animated.
     *
     * @param resId album cover resource id
     */
    public void setAlbumCover(int resId) {
        mAdapter.setAlbumCover(resId);
    }

    /**
     * Sets the music title in the player.
     *
     * @param musicTitle music title
     */
    public void setMusicTitle(String musicTitle) {
        mAdapter.setMusicTitle(musicTitle);
    }

    /**
     * Sets the artist name in the player.
     *
     * @param artistName artist name
     */
    public void setArtistName(String artistName) {
        mAdapter.setArtistName(artistName);
    }

    @Override
    public void onGlobalLayout() {
        mAdapter.onViewLoaded();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
