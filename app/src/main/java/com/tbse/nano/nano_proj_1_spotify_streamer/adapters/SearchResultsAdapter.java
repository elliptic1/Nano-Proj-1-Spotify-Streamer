package com.tbse.nano.nano_proj_1_spotify_streamer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tbse.nano.nano_proj_1_spotify_streamer.models.SearchResult;
import com.tbse.nano.nano_proj_1_spotify_streamer.views.SearchResultView;
import com.tbse.nano.nano_proj_1_spotify_streamer.views.SearchResultView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class SearchResultsAdapter extends BaseAdapter {

    List<SearchResult> searchResultList;

    @RootContext Context context;

    public SearchResultsAdapter() {
        super();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchResultView searchResultView;

        if (convertView == null) {
            searchResultView = SearchResultView_.build(context);
        } else {
            searchResultView = (SearchResultView) convertView;
        }

        searchResultView.bind(getItem(position));

        return searchResultView;
    }

    @Override
    public int getCount() {
        return searchResultList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public SearchResult getItem(int position) {
        return searchResultList.get(position);
    }

}
