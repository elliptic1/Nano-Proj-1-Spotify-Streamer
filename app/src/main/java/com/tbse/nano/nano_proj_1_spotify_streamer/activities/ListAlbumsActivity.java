package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.adapters.AlbumResultsAdapter;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.AlbumResult;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListAlbumsActivity extends Activity {

    private final static String TAG = MainActivity.TAG;
    private AlbumResultsAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.album_list);

        listView = (ListView) findViewById(R.id.listOfAlbums);

        // https://github.com/kaaes/spotify-web-api-android
        SpotifyApi api = new SpotifyApi();
        final SpotifyService spotify = api.getService();

        String artist = getIntent().getStringExtra("artist");

        spotify.searchAlbums(artist, new Callback<AlbumsPager>() {
            @Override
            public void success(AlbumsPager albumsPager, Response response) {
                Pager<AlbumSimple> pager = albumsPager.albums;
                if (pager.items.size() == 0) {
                    clearAlbumResultsList();
                    return;
                }
                populateAlbumResultsList(pager.items);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "failure: " + error.getBody());
            }
        });


    }

    private void clearAlbumResultsList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter != null)
                    adapter.clear();
            }
        });
    }

    private void populateAlbumResultsList(final List<AlbumSimple> sr) {

        final ListView listView = (ListView) findViewById(R.id.listOfAlbums);

        // Update the listview on the main thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Make the new adapter if needed
                if (adapter == null) {
                    adapter = new AlbumResultsAdapter(getApplicationContext(), new ArrayList<AlbumResult>());
                    listView.setAdapter(adapter);
                }

                adapter.clear();

                // Add the non-null Albums
                for (AlbumSimple album : sr) {
                    if (album == null) continue;
                    adapter.add(new AlbumResult(album));
                }

            }
        });
    }


}
