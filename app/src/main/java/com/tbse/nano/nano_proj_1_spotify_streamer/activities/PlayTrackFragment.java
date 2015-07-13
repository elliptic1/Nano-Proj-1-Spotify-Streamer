package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.DialogFragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.TrackResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

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
    @ViewById(R.id.left_btn)
    ImageView prevBtn;
    @ViewById(R.id.middle_btn)
    ImageView playPauseBtn;
    @ViewById(R.id.right_btn)
    ImageView nextBtn;

    private enum PlayerState {PLAYING, PAUSED};
    private PlayerState mPlayerState = PlayerState.PAUSED;

    private static String TAG = MainActivity.TAG;

    public PlayTrackFragment() {
        Log.d(TAG, "PTF constr");
    }

    @Click(R.id.left_btn)
    void clickLeft() {
        // TODO load prev track
    }

    @Click(R.id.middle_btn)
    void clickMiddle() {
        // TODO play / pause

        MediaPlayer mediaPlayer = MainActivity.getMediaPlayer();

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        MainActivity.setMediaPlayer(mediaPlayer);

        if (mPlayerState == PlayerState.PAUSED) {
            mPlayerState = PlayerState.PLAYING;

            playPauseBtn.setBackgroundResource(android.R.drawable.ic_media_pause);

            TrackResult tr = getArguments().getParcelable("track");
            if (tr == null) {
                Log.e(TAG, "track result is null");
                return;
            }

            startAudio(mediaPlayer, tr.getTrack().preview_url);

        } else if (mPlayerState == PlayerState.PLAYING) {
            mPlayerState = PlayerState.PAUSED;

            playPauseBtn.setBackgroundResource(android.R.drawable.ic_media_play);

            mediaPlayer.stop();

        }

    }

    @Background
    void startAudio(MediaPlayer mediaPlayer, String track_prev_url) {
        if (mediaPlayer.isPlaying()) {
            return;
        }
        try {
            mediaPlayer.setDataSource(track_prev_url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();
        } catch (IllegalStateException ignored) {
            mediaPlayer.reset();
        } catch (IOException ignored) {
        }
    }

    @Click(R.id.right_btn)
    void clickRight() {
        // TODO load next track
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
