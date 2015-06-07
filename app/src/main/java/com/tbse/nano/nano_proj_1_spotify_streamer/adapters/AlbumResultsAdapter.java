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
import com.tbse.nano.nano_proj_1_spotify_streamer.models.AlbumResult;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Image;

public class AlbumResultsAdapter extends ArrayAdapter<AlbumResult> {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumResult albumResult = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.album_result_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_album_image);
        if (albumResult.getNumberOfImages() > 0) {
            imageView.setVisibility(View.VISIBLE);
            Image image = albumResult.getFirstImage();
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

        TextView textView = (TextView) convertView.findViewById(R.id.item_album_text_view);
        textView.setText(albumResult.getAlbum().name);

        return convertView;
    }

    public AlbumResultsAdapter(Context context, ArrayList<AlbumResult> objects) {
        super(context, 0, objects);
    }
}
