package com.tbse.nano.nano_proj_1_spotify_streamer.models;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class TrackResult implements Parcelable {

    private Track track;

    public TrackResult(Track track) {
        this.track = track;
    }

    protected TrackResult(Parcel in) {
    }

    public static final Creator<TrackResult> CREATOR = new Creator<TrackResult>() {
        @Override
        public TrackResult createFromParcel(Parcel in) {
            return new TrackResult(in);
        }

        @Override
        public TrackResult[] newArray(int size) {
            return new TrackResult[size];
        }
    };

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
        if (track == null || track.album == null || track.album.images == null
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
