package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.adapters.SearchResultsAdapter;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.ParcelableArtist;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.SearchResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hugo.weaving.DebugLog;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_main)
@DebugLog
public class MainActivity extends Activity {

    public final static String TAG = "Nano1";
    @Bean
    SearchResultsAdapter adapter;

    @ViewById(R.id.search_view)
    SearchView searchView;

    @ViewById(R.id.listView)
    ListView listView;

    private boolean hasBeenRestored;

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        MainActivity.mediaPlayer = mediaPlayer;
    }

    @Override
    protected void onStart() {
        super.onStart();
        hasBeenRestored = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("parcelableArtists", parcelableArtists);
        if (searchView.getQuery() != null)
            outState.putString("searchText", searchView.getQuery().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        hasBeenRestored = true;
        parcelableArtists = savedInstanceState.getParcelableArrayList("parcelableArtists");
        searchView.setQuery(savedInstanceState.getString("searchText"), false);
        searchView.setIconified(false);
    }

    private static MediaPlayer mediaPlayer;
    private ArrayList<ParcelableArtist> parcelableArtists;

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {

            Log.d(TAG, "Enter was pressed!");
            adapter.clear();

            SpotifyApi api = new SpotifyApi();
            final SpotifyService spotify = api.getService();
            spotify.searchArtists("*" + searchView.getQuery().toString() + "*", new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    Pager<Artist> pager = artistsPager.artists;
                    if (pager.items.size() == 0) {
                        showNoSearchResultsToast();
                        return;
                    }

                    parcelableArtists = new ArrayList<ParcelableArtist>();
                    for (Artist artist : pager.items) {
                        ParcelableArtist parcelableArtist = new ParcelableArtist(artist);
                        parcelableArtists.add(parcelableArtist);
                    }

                    populateSearchResultsList(parcelableArtists);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "failure: " + error.getBody());
                    showBadNetworkToast();
                }
            });

            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @UiThread
    void showNoSearchResultsToast() {
        Toast.makeText(getApplicationContext(), "No Search Results!", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void showBadNetworkToast() {
        Toast.makeText(getApplicationContext(), "Bad Network!", Toast.LENGTH_SHORT).show();
    }

    @ItemClick(R.id.listView)
    public void listArtistClicked(SearchResult searchResult) {
        Log.d(TAG, "artist clicked");
        Intent intent = new Intent(getApplicationContext(), ListTracksActivity_.class);
        intent.putExtra("artist", searchResult.getArtistName());
        startActivity(intent);
    }

    @Background
    void populateSearchResultsList(final List<ParcelableArtist> sr) {

        if (sr == null) {
            Log.e(TAG, "called populate with null list");
            return;
        }

        // sort by popularity
        Collections.sort(sr, new Comparator<ParcelableArtist>() {
            @Override
            public int compare(ParcelableArtist lhs, ParcelableArtist rhs) {
                return rhs.getArtist().popularity - lhs.getArtist().popularity;
            }
        });

        makeNewAdapter(sr);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasBeenRestored) {
            populateSearchResultsList(parcelableArtists);
        }
    }

    @AfterViews
    void setAdapter() {
        listView.setAdapter(adapter);
        searchView.setOnQueryTextListener(onQueryTextListener);
    }

    @UiThread
    void makeNewAdapter(final List<ParcelableArtist> sr) {
        adapter.clear();
        for (ParcelableArtist parcelableArtist : sr) {
            if (parcelableArtist == null) continue;
            adapter.add(new SearchResult(parcelableArtist.getArtist()));
        }
    }


}
