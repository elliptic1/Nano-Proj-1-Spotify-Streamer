package com.tbse.nano.nano_proj_1_spotify_streamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.TrackResult;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Image;

public class TrackResultsAdapter extends ArrayAdapter<TrackResult> {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrackResult trackResult = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.track_result_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_album_image);
        if (trackResult.getNumberOfImages() > 0) {
            imageView.setVisibility(View.VISIBLE);
            Image image = trackResult.getImage();
            if (image != null) {
                Glide.with(getContext())
                        .load(image.url)
                        .fitCenter()
                        .centerCrop()
                        .crossFade()
                        .into(imageView);
            }

        } else {
            imageView.setVisibility(View.INVISIBLE);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.item_track_text_view);
        textView.setText(trackResult.getTrack().name);

        TextView albumTextView = (TextView) convertView.findViewById(R.id.item_track_album);
        albumTextView.setText(trackResult.getTrack().album.name);

        return convertView;
    }

    public TrackResultsAdapter(Context context, ArrayList<TrackResult> objects) {
        super(context, 0, objects);
    }
}
