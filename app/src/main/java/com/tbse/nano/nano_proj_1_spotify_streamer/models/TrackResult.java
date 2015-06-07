package com.tbse.nano.nano_proj_1_spotify_streamer.models;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class TrackResult {

    private Track track;

    public TrackResult(Track track) {
        this.track = track;
    }
    public Track getTrack() {
        if (track == null) {
            track = new Track();
        }
        return track;
    }

    public AlbumSimple getAlbum() {
        if (track != null) {
            return track.album;
        }
        return null;
    }

    public int getNumberOfImages() {
        if (track == null | track.album == null | track.album.images == null
                || track.album.images.size() == 0) {
            return 0;
        }
        return track.album.images.size();
    }

    public Image getImage() {
        if (getNumberOfImages() > 0)
            return track.album.images.get(0);
        return null;
    }

}
