package com.escodro.viittaus.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.escodro.viittaus.R;
import com.escodro.viittaus.view.ArcSeekBar;
import com.escodro.viittaus.view.PlayerView;
import com.escodro.viittaus.view.transformation.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Adapter responsible to handle the {@link PlayerView} behavior.
 * <p/>
 * Created by Igor Escodro on 25/5/2016.
 */
public class PlayerAdapter implements MediaPlayer.OnPreparedListener, MediaPlayer
        .OnCompletionListener, Runnable, View.OnClickListener {

    /**
     * Constant to represent the total time of the spinning animation.
     */
    private final static int ROTATE_ANIMATION_DURATION = 8000;

    /**
     * {@link MediaPlayer} reference.
     */
    private MediaPlayer mMediaPlayer;

    /**
     * {@link PlayerHolder} reference.
     */
    private final PlayerHolder mHolder;

    /**
     * {@link Handler} to control the {@link ArcSeekBar} update.
     */
    private final Handler mHandler;

    /**
     * Application {@link Context}
     */
    private final Context mContext;

    /**
     * Rotate {@link Animation}.
     */
    private Animation mRotateAnimation;

    /**
     * Field to store the current degree of rotation of the album cover.
     */
    private float mCurrentDegree;

    /**
     * Create a new instance of {@link PlayerAdapter}.
     *
     * @param context application context
     * @param view    view containing the components
     */
    public PlayerAdapter(Context context, View view) {
        mHolder = new PlayerHolder(view);
        mHandler = new Handler();
        mContext = context;
        createRotateAnimation();
        initMediaPlayer();
    }

    /**
     * Initialize the {@link MediaPlayer}.
     */
    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mHandler.postDelayed(this, 100);
        mHolder.playPauseButton.setOnClickListener(this);
        updateButtonState();
    }

    /**
     * Creates the rotate {@link Animation} used in the album cover.
     */
    private void createRotateAnimation() {
        mRotateAnimation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF, 0.5F) {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mCurrentDegree = 360 * interpolatedTime;
                super.applyTransformation(interpolatedTime, t);
            }
        };
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setDuration(ROTATE_ANIMATION_DURATION);
        mRotateAnimation.setInterpolator(mContext, android.R.anim.linear_interpolator);
    }

    /**
     * Loads the album cover in the {@link ImageView} and transforms it in round form.
     *
     * @param resId the album cover resource id
     */
    private void prepareAlbumCover(int resId) {
        Picasso.with(mContext).load(resId).transform(new
                RoundedTransformation()).into(mHolder.albumCover);
    }

    /**
     * Method to handle the {@link ImageButton} press.
     */
    private void onButtonPressed() {
        if (!mMediaPlayer.isPlaying()) {
            playAlbumAnimation();
            mMediaPlayer.start();
        } else {
            pauseAlbumAnimation();
            mMediaPlayer.pause();
        }
        updateButtonState();
    }

    /**
     * Updates the Play/Pause {@link ImageButton} based on the {@link MediaPlayer} status
     */
    private void updateButtonState() {
        if (mMediaPlayer.isPlaying()) {
            mHolder.playPauseButton.setImageResource(R.drawable.ic_pause);
        } else {
            mHolder.playPauseButton.setImageResource(R.drawable.ic_play);
        }
    }

    /**
     * Plays the rotate {@link Animation}.
     */
    private void playAlbumAnimation() {
        mHolder.albumCover.startAnimation(mRotateAnimation);
    }

    /**
     * Pauses the rotate {@link Animation}.
     */
    private void pauseAlbumAnimation() {
        mHolder.albumCover.clearAnimation();
        mHolder.albumCover.setRotation(mHolder.albumCover.getRotation() + mCurrentDegree);
    }

    /**
     * Stops the rotate {@link Animation}.
     */
    private void stopAlbumAnimation() {
        mHolder.albumCover.clearAnimation();
    }

    /**
     * Converts milliseconds to the player format (HH:mm).
     *
     * @param millis time in milliseconds
     *
     * @return time in "HH:mm" format
     */
    private String getTimeString(long millis) {
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        return String.format(mContext.getString(R.string.player_time), minutes, seconds);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mHolder.arcSeekBar.setMax(mMediaPlayer.getDuration());
//        mMediaPlayer.start();
//        playAlbumAnimation();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopAlbumAnimation();
        mMediaPlayer.seekTo(0);
        mHolder.arcSeekBar.setProgress(0);
        updateButtonState();
    }

    @Override
    public void run() {
        final int currentPosition = mMediaPlayer.getCurrentPosition();
        final int remainingTime = mMediaPlayer.getDuration() - currentPosition;
        mHolder.arcSeekBar.setProgress(currentPosition);
        mHolder.textProgress.setText(getTimeString(currentPosition));
        final String remaining = String.format(mContext.getString(R.string.player_remaining_time),
                getTimeString(remainingTime));
        mHolder.textRemaining.setText(remaining);

        if (currentPosition < mMediaPlayer.getDuration()) {
            mHandler.postDelayed(this, 100);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_button:
                onButtonPressed();
        }
    }

    /**
     * When the view is loaded, sets the album cover to the correct size.
     */
    public void onViewLoaded() {
        mHolder.arcSeekBar.setAlbumSize(
                mHolder.albumCover.getWidth(),
                mHolder.albumCover.getHeight());
    }

    /**
     * Method that must be called on resume the activity.
     */
    public void onResume() {
        mHolder.arcSeekBar.resume();
    }

    /**
     * Method that must be called on pause the activity.
     */
    public void onPause() {
        mHolder.arcSeekBar.pause();
    }

    /**
     * Set the album cover.
     *
     * @param resId album cover resource id
     */
    public void setAlbumCover(int resId) {
        prepareAlbumCover(resId);
    }

    /**
     * Set the music title.
     *
     * @param musicTitle music title
     */
    public void setMusicTitle(String musicTitle) {
        mHolder.musicTitle.setText(musicTitle);
    }

    /**
     * Set the artist name.
     *
     * @param artistName artist name
     */
    public void setArtistName(String artistName) {
        mHolder.artistName.setText(artistName);
    }

    /**
     * Set the audio uri.
     *
     * @param uri audio uri
     */
    public void setAudioUri(Uri uri) {
        try {
            mMediaPlayer.setDataSource(mContext, uri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Holder containing all the {@link PlayerView} components.
     */
    private static class PlayerHolder {

        /**
         * {@link ImageView} with the album cover.
         */
        ImageView albumCover;

        /**
         * {@link ArcSeekBar} with the progress of the audio.
         */
        ArcSeekBar arcSeekBar;

        /**
         * {@link TextView} with the progress in minutes/second format.
         */
        TextView textProgress;

        /**
         * {@link TextView} with the remaining time in minutes/second format.
         */
        TextView textRemaining;

        /**
         * {@link TextView} with the artist name.
         */
        TextView artistName;

        /**
         * {@link TextView} with the music title.
         */
        TextView musicTitle;

        /**
         * {@link ImageButton} with the play/pause button.
         */
        ImageButton playPauseButton;

        /**
         * Create a new instance of {@link PlayerHolder}.
         *
         * @param view view with the components
         */
        PlayerHolder(View view) {
            albumCover = (ImageView) view.findViewById(R.id.album_cover);
            arcSeekBar = (ArcSeekBar) view.findViewById(R.id.arc_seekbar);
            textProgress = (TextView) view.findViewById(R.id.text_progress);
            textRemaining = (TextView) view.findViewById(R.id.text_remaining);
            playPauseButton = (ImageButton) view.findViewById(R.id.player_button);
            artistName = (TextView) view.findViewById(R.id.artist_name);
            musicTitle = (TextView) view.findViewById(R.id.music_title);
        }
    }
}
