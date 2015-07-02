package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.adapters.TrackResultsAdapter;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.MyTrack;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.TrackResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
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
    @Bean
    TrackResultsAdapter adapter;

    @ViewById(R.id.listOfTracks)
    ListView listView;

    @ItemClick(R.id.listOfTracks)
    public void itemTrackClicked(TrackResult trackResult) {
        Log.d(TAG, "trackResult clicked: " + trackResult.toString());

        PlayTrackFragment playTrackFragment = new PlayTrackFragment_();
        Bundle b = new Bundle();
        b.putParcelable("track", trackResult);
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

                populateTrackResultsList(MyTrack.listOfTrackToListOfMyTrack(pager.items));
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
    void populateTrackResultsList(final List<MyTrack> trackList) {

        // sort by popularity
        Collections.sort(trackList, new Comparator<MyTrack>() {
            @Override
            public int compare(MyTrack lhs, MyTrack rhs) {
                return rhs.track.popularity - lhs.track.popularity;
            }
        });

        updateListView(trackList);

    }

    @AfterViews
    void setAdapter() {
        listView.setAdapter(adapter);
    }

    @UiThread
    void updateListView(final List<MyTrack> trackList) {
        Log.d(TAG, "clearing list from updateListView");

        clearTrackResultsList();

        // Add the non-null Albums
        int c = 0;
        for (MyTrack track : trackList) {
            if (track == null) continue;
            c++;
            if (c > 10) break;
            Log.d(TAG, "adding " + track);
            adapter.add(new TrackResult(track));
        }

    }


}
