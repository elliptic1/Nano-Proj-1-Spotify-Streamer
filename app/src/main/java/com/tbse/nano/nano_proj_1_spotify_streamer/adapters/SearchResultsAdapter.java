package com.tbse.nano.nano_proj_1_spotify_streamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.tbse.nano.nano_proj_1_spotify_streamer.R;
import com.tbse.nano.nano_proj_1_spotify_streamer.models.SearchResult;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Image;

public class SearchResultsAdapter extends ArrayAdapter<SearchResult> {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResult searchResult = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.item_main_text_view);
        textView.setText(searchResult.getArtistName());

        TextView genreTV = (TextView) convertView.findViewById(R.id.item_main_genre);
        genreTV.setText(searchResult.getGenres());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_main_image);
        if (searchResult.getNumberOfImages() > 0) {
            Image image = searchResult.getFirstImage();
            if (image != null) {
                Glide.with(getContext())
                        .load(image.url)
                        .fitCenter()
                        .centerCrop()
                        .crossFade()
                        .into(imageView);
            }

        }

        return convertView;
    }

    public SearchResultsAdapter(Context context, ArrayList<SearchResult> objects) {

        super(context, 0, objects);
    }
}
