package com.tbse.nano.nano_proj_1_spotify_streamer.models;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class MyTrack extends Track {

    public Track track;

    public MyTrack() {

    }

    public void setTrack(Track t) {
        this.track = t;
    }

    @Override
    public String toString() {
        return "name: " + track.name +
                "\nuri: " + track.uri +
                "\nartist: " + track.artists.get(0).name + "\n\n";
    }

    public static List<MyTrack> listOfTrackToListOfMyTrack(List<Track> trackList) {
        ArrayList<MyTrack> myTrackArrayList = new ArrayList<MyTrack>();
        if (trackList.size() == 0) return myTrackArrayList;
        for (Track t : trackList) {
            MyTrack myTrack = new MyTrack();
            myTrack.setTrack(t);
            myTrackArrayList.add(myTrack);
        }
        return myTrackArrayList;
    }
}
