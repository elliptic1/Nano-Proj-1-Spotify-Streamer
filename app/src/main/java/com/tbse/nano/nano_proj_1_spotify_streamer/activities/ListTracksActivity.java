package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.adapters.TrackResultsAdapter;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.TrackResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.track_list)
public class ListTracksActivity extends Activity {

    private final static String TAG = MainActivity.TAG;

    private PlayTrackFragment playTrackFragment;

    @Bean
    TrackResultsAdapter adapter;

    @ViewById(R.id.listOfTracks)
    ListView listView;

    private List<Track> searchResults;

    @ItemClick(R.id.listOfTracks)
    public void itemTrackClicked(TrackResult trackResult) {
        Log.d(TAG, "trackResult clicked: " + trackResult.toString());

        playTrack(trackResult.getTrackIndex());
    }

    @Receiver(actions = "action_play_track", local = true)
    void playTrack(@Receiver.Extra("trackNumber") int trackNumber) {
        Log.d(TAG, "got play track intent: " + trackNumber);
        if (playTrackFragment != null) {
            try {
                playTrackFragment.dismiss();
            } catch (Exception e) {
                // ignore
            }
        }

        playTrackFragment = new PlayTrackFragment_();
        Bundle b = new Bundle();
        TrackResult trackResult = new TrackResult(searchResults.get(trackNumber), trackNumber);
        b.putParcelable("track", trackResult);
        b.putInt("trackNum", trackNumber);
        playTrackFragment.setArguments(b);
        playTrackFragment.show(getFragmentManager(), "track");
    }

    @AfterViews
    void beginSearch() {
        SpotifyApi api = new SpotifyApi();

        String artist = getIntent().getStringExtra("artist");
        searchSpotify(api, artist);
    }

    @Background
    void searchSpotify(SpotifyApi api, String artist) {
        final SpotifyService spotify = api.getService();
        spotify.searchTracks("artist:" + artist, new Callback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                Pager<Track> pager = tracksPager.tracks;
                if (pager.items.size() == 0) {
                    Log.d(TAG, "clearing list from searchSpotify");
                    clearTrackResultsList();
                    return;
                }

                populateTrackResultsList(pager.items);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "failure: " + error.getBody());
            }
        });

    }

    @UiThread
    void clearTrackResultsList() {
        if (adapter != null) {
            Log.d(TAG, "clearing" );
//            adapter.clear();
        }
    }

    @Background
    void populateTrackResultsList(final List<Track> trackList) {

        // sort by popularity
        Collections.sort(trackList, new Comparator<Track>() {
            @Override
            public int compare(Track lhs, Track rhs) {
                return rhs.popularity - lhs.popularity;
            }
        });

        searchResults = trackList;

        updateListView(trackList);

    }

    @AfterViews
    void setAdapter() {
        listView.setAdapter(adapter);
    }

    @UiThread
    void updateListView(final List<Track> trackList) {
        Log.d(TAG, "clearing list from updateListView");

        clearTrackResultsList();

        // Add the non-null Albums
        int c = 0;
        for (Track track : trackList) {
            if (track == null) continue;
            if (c >= 10) break;
            Log.d(TAG, "adding " + track + " at pos " + c);
            adapter.add(new TrackResult(track, c));
            c++;
        }

    }

}
