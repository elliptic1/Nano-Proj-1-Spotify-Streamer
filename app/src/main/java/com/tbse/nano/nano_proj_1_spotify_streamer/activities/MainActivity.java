package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.UiThread;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.adapters.SearchResultsAdapter;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.SearchResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

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

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    public final static String TAG = "Nano1";
    private SearchResultsAdapter adapter;

    @ViewById(R.id.search_edittext)
    EditText editText;

    @ViewById(R.id.listView)
    ListView listView;

    @AfterViews
    void setEditTextTextChangedListener() {
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

                SpotifyApi api = new SpotifyApi();
                final SpotifyService spotify = api.getService();
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

    @UiThread
    private void clearSearchResultsList() {
        adapter.clear();
    }

    @ItemClick(R.id.listView)
    public void listArtistClicked(SearchResult searchResult) {
        Log.d(TAG, "artist clicked");
        Intent intent = new Intent(getApplicationContext(), ListTracksActivity_.class);
        intent.putExtra("artist", searchResult.getArtistName());
        startActivity(intent);
    }

    private void populateSearchResultsList(final List<Artist> sr) {

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
