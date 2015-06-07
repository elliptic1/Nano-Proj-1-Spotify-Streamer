package com.tbse.nano.nano_proj_1_spotify_streamer.models;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Image;

public class AlbumResult {

    private AlbumSimple album;

    public AlbumResult(AlbumSimple album) {
        this.album = album;
    }
    public AlbumSimple getAlbum() {
        if (album == null) {
            album = new AlbumSimple();
        }
        return album;
    }

    public int getNumberOfImages() {
        return getAlbum().images.size();
    }

    public Image getFirstImage() {
        if (getNumberOfImages() > 0) {
            return getAlbum().images.get(0);
        }
        return null;
    }

}
