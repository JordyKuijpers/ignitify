package com.jordykuijpers.spotify.rest;

import java.util.ArrayList;
import java.util.List;

import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyTrack;

public class PlayingQueue {
	private List<SpotifyTrack> artists;
	private int tracksInQueue;
	
	public PlayingQueue(List<SpotifyTrack> artists) {
		super();
		
		if (artists == null)
			this.artists = new ArrayList<SpotifyTrack>();
		
		if (artists != null)
			this.tracksInQueue = this.artists.size();
		else
			this.tracksInQueue = 0;
	}

	public List<SpotifyTrack> getArtists() {
		return artists;
	}

	public void setArtists(List<SpotifyTrack> artists) {
		if (artists == null)
			this.artists = new ArrayList<SpotifyTrack>();
		
		if (artists != null)
			this.tracksInQueue = this.artists.size();
		else
			this.tracksInQueue = 0;
	}

	public int getTracksInQueue() {
		return tracksInQueue;
	}
}
