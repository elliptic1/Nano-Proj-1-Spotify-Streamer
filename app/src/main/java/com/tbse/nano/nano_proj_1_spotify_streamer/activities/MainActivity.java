package com.tbse.nano.nano_proj_1_spotify_streamer.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.adapters.SearchResultsAdapter;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.SearchResult;

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

    @ViewById(R.id.search_edittext)
    EditText editText;

    @ViewById(R.id.listView)
    ListView listView;

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        MainActivity.mediaPlayer = mediaPlayer;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        // TODO restore search results list

        super.onRestoreInstanceState(savedInstanceState);
    }

    private static MediaPlayer mediaPlayer;

    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                Log.d(TAG, "Enter was pressed!");
                adapter.clear();

                SpotifyApi api = new SpotifyApi();
                final SpotifyService spotify = api.getService();
                spotify.searchArtists("*" + editText.getText().toString() + "*", new Callback<ArtistsPager>() {
                    @Override
                    public void success(ArtistsPager artistsPager, Response response) {
                        Pager<Artist> pager = artistsPager.artists;
                        if (pager.items.size() == 0) {
                            showNoSearchResultsToast();
                            return;
                        }
                        populateSearchResultsList(pager.items);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "failure: " + error.getBody());
                        showBadNetworkToast();
                    }
                });

                return true;

            }
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
    void populateSearchResultsList(final List<Artist> sr) {

        // sort by popularity
        Collections.sort(sr, new Comparator<Artist>() {
            @Override
            public int compare(Artist lhs, Artist rhs) {
                return rhs.popularity - lhs.popularity;
            }
        });

        makeNewAdapter(sr);

    }

    @AfterViews
    void setAdapter() {
        listView.setAdapter(adapter);
        editText.setOnEditorActionListener(onEditorActionListener);
    }

    @UiThread
    void makeNewAdapter(final List<Artist> sr) {
        adapter.clear();
        for (Artist artist : sr) {
            if (artist == null) continue;
            adapter.add(new SearchResult(artist));
        }
    }


}
