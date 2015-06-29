package com.tbse.nano.nano_proj_1_spotify_streamer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tbse.nano.nano_proj_1_spotify_streamer.models.TrackResult;
import com.tbse.nano.nano_proj_1_spotify_streamer.views.TrackResultView;
import com.tbse.nano.nano_proj_1_spotify_streamer.views.TrackResultView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class TrackResultsAdapter extends BaseAdapter {

    List<TrackResult> trackResultList;

    @RootContext Context context;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrackResultView trackResultView;

        if (convertView == null) {
            trackResultView = TrackResultView_.build(context);
        } else {
            trackResultView = (TrackResultView) convertView;
        }

        trackResultView.bind(getItem(position));

        return trackResultView;
    }

    @Override
    public int getCount() {
        return trackResultList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public TrackResult getItem(int position) {
        return trackResultList.get(position);
    }

    public TrackResultsAdapter() {
        super();
    }
}
