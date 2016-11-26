package com.escodro.viittaus.activity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.escodro.viittaus.R;
import com.escodro.viittaus.view.PlayerView;

/**
 * {@link AppCompatActivity} with the {@link PlayerView}.
 * <p/>
 * Created by Igor Escodro on 6/5/2016.
 */
public class PlayerActivity extends AppCompatActivity {

    /**
     * {@link PlayerView} reference.
     */
    private PlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mPlayerView = (PlayerView) findViewById(R.id.player);
        if (mPlayerView != null) {
            mPlayerView.setAlbumCover(R.drawable.other_planets);
            mPlayerView.setArtistName("Kisses");
            mPlayerView.setMusicTitle("Endeavour");
            mPlayerView.setAudioUri(resourceIdToUri(R.raw.endeavour));
        }
    }

    /**
     * Converts from resource id to {@link Uri}
     *
     * @param resId resource id
     *
     * @return uri of the resource
     */
    private Uri resourceIdToUri(int resId) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                getResources().getResourcePackageName(resId) + '/' +
                getResources().getResourceTypeName(resId) + '/' +
                getResources().getResourceEntryName(resId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }
}
