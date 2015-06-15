package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.DialogFragment;
import android.util.Log;

import com.tbse.nano.nano_proj_1_spotify_streamer.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.play_track)
public class PlayTrackFragment extends DialogFragment {

    @AfterViews
    void fillOutDialogFragment() {
        Log.d("Nano1", "fillOutDF");
    }

}
