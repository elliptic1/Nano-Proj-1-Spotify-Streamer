package com.tbse.nano.nano_proj_1_spotify_streamer.models;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

public class SearchResult {

    private Artist artist;

    public String getArtistName() {
        if (artist == null) {
            return "null artist";
        }
        return artist.name;
    }

    public int getNumberOfImages() {
        if (artist == null || artist.images == null) return 0;
        return artist.images.size();
    }

    public Image getFirstImage() {
        if (getNumberOfImages() > 0) {
            return artist.images.get(0);
        }
        return null;
    }

    public String getGenres() {
        StringBuilder stringBuilder = new StringBuilder("");
        if (artist == null || artist.genres == null || artist.genres.size() == 0) return "";
        stringBuilder.append(artist.genres.get(0));
        if (artist.genres.size() > 1) {
            for (String genre : artist.genres) {
                stringBuilder.append(", " + genre);
            }
        }
        return stringBuilder.toString();
    }

    public SearchResult(Artist artist) {
        this.artist = artist;
    }

}
