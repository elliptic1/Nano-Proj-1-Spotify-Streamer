package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.adapters.SearchResultsAdapter;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.SearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends Activity {

    public final static String TAG = "Nano1";
    private SearchResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // https://github.com/kaaes/spotify-web-api-android
        SpotifyApi api = new SpotifyApi();
        final SpotifyService spotify = api.getService();

        EditText editText = (EditText) findViewById(R.id.search_edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().equals("")) {
                    clearSearchResultsList();
                    return;
                }

                spotify.searchArtists("*"+s.toString()+"*", new Callback<ArtistsPager>() {
                    @Override
                    public void success(ArtistsPager artistsPager, Response response) {
                        Pager<Artist> pager = artistsPager.artists;
                        if (pager.items.size() == 0) {
                            clearSearchResultsList();
                            return;
                        }
                        populateSearchResultsList(pager.items);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "failure: " + error.getBody());
                    }
                });


            }
        });

    }

    private void clearSearchResultsList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
            }
        });
    }

    private void populateSearchResultsList(final List<Artist> sr) {

        final ListView listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ListTracksActivity.class);
                intent.putExtra("artist", ((SearchResult)listView.getItemAtPosition(position)).getArtistName());
                startActivity(intent);
            }
        });

        // sort by popularity
        Collections.sort(sr, new Comparator<Artist>() {
            @Override
            public int compare(Artist lhs, Artist rhs) {
                return rhs.popularity - lhs.popularity;
            }
        });

        // Update the listview on the main thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Make the new adapter if needed
                if (adapter == null) {
                    adapter = new SearchResultsAdapter(getApplicationContext(), new ArrayList<SearchResult>());
                    listView.setAdapter(adapter);
                }

                adapter.clear();

                // Make an ArrayList from the non-null Artists
                for (Artist artist : sr) {
                    if (artist == null) continue;
                    adapter.add(new SearchResult(artist));
                }

            }
        });
    }



}
