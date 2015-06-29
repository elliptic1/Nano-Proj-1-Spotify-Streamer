package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.TrackResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.play_track)
public class PlayTrackFragment extends DialogFragment {

    @ViewById(R.id.play_album_image) ImageView albumImage;
    @ViewById(R.id.play_album_title) TextView albumTitle;
    @ViewById(R.id.play_track_title) TextView trackTitle;
    @ViewById(R.id.play_artist_name) TextView artistName;

    private static String TAG = MainActivity.TAG;

    public PlayTrackFragment() {
        Log.d(TAG, "PTF constr");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);

        Log.d(TAG, "onCreateDialog");

        return d;
    }

    @AfterViews
    void fillOutDialogFragment() {

        Log.d(TAG, "AfterViews");

        TrackResult tr = getArguments().getParcelable("track");
        if (tr == null) {
            Log.d(TAG, "tr is null");
            return;
        }

        Log.d(TAG, "artist: " + tr.getTrack().artists.get(0).name);
        artistName.setText(tr.getTrack().artists.get(0).name);
        trackTitle.setText(tr.getTrack().name);
        albumTitle.setText(tr.getAlbum().name);

    }

}
