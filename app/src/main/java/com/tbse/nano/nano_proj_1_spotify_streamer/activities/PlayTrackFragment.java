package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.TrackResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import kaaes.spotify.webapi.android.models.Image;

@EFragment(R.layout.play_track)
public class PlayTrackFragment extends DialogFragment {

    @ViewById(R.id.play_album_image)
    ImageView albumImage;
    @ViewById(R.id.play_album_title)
    TextView albumTitle;
    @ViewById(R.id.play_track_title)
    TextView trackTitle;
    @ViewById(R.id.play_artist_name)
    TextView artistName;

    private static String TAG = MainActivity.TAG;

    public PlayTrackFragment() {
        Log.d(TAG, "PTF constr");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
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
        int duration = Integer.valueOf("" + (tr.getTrack().duration_ms / 1000));
        int minutes = duration / 60;
        int leftover = duration % 60;
        trackTitle.setText(tr.getTrack().name
                + " (" + minutes + "m " + leftover + "s)");
        albumTitle.setText(tr.getAlbum().name);

        if (tr.getNumberOfImages() > 0) {
            albumImage.setVisibility(View.VISIBLE);
            Image image = tr.getImage();
            if (image != null) {
                Picasso.with(getActivity())
                        .load(image.url)
                        .fit()
                        .centerCrop()
                        .into(albumImage);
            }

        } else {
            albumImage.setVisibility(View.INVISIBLE);
        }

    }

}
