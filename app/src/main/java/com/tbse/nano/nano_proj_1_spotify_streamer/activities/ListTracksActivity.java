package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.adapters.TrackResultsAdapter;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.TrackResult;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
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

@EActivity
public class ListTracksActivity extends Activity {

    private final static String TAG = MainActivity.TAG;
    private TrackResultsAdapter adapter;

    @ViewById(R.id.listOfTracks) ListView listView;

    @ItemClick(R.id.listOfTracks)
    public void itemTrackClicked(TrackResult trackResult) {
        Log.d(TAG, "trackResult clicked: " + trackResult.toString());

        PlayTrackFragment playTrackFragment = new PlayTrackFragment();
        playTrackFragment.show(getFragmentManager(), "track");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.track_list);

        SpotifyApi api = new SpotifyApi();
        final SpotifyService spotify = api.getService();

        String artist = getIntent().getStringExtra("artist");

        spotify.searchTracks("artist:" + artist, new Callback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                Pager<Track> pager = tracksPager.tracks;
                if (pager.items.size() == 0) {
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
        if (adapter != null)
            adapter.clear();
    }

    private void populateTrackResultsList(final List<Track> trackList) {

        // sort by popularity
        Collections.sort(trackList, new Comparator<Track>() {
            @Override
            public int compare(Track lhs, Track rhs) {
                return rhs.popularity - lhs.popularity;
            }
        });

        updateListView(trackList);

    }

    @UiThread
    void updateListView(final List<Track> trackList) {
        // Make the new adapter if needed
        if (adapter == null) {
            adapter = new TrackResultsAdapter(getApplicationContext(), new ArrayList<TrackResult>());
            listView.setAdapter(adapter);
        }

        adapter.clear();

        // Add the non-null Albums
        int c = 0;
        for (Track track : trackList) {
            if (track == null) continue;
            c++;
            if (c > 10) break;
            adapter.add(new TrackResult(track));
        }

    }


}
