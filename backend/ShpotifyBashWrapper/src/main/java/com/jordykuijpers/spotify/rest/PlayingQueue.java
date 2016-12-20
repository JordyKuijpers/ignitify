package com.jordykuijpers.spotify.rest;

import java.util.ArrayList;
import java.util.List;

import com.jordykuijpers.spotify.SpotifyPlayer.ISpotifyPlayable;
import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyTrack;

public class PlayingQueue {
	private List<ISpotifyPlayable> artists;
	private int tracksInQueue;
	
	public PlayingQueue(List<ISpotifyPlayable> artists) {
		super();
		
		if (artists == null)
			this.artists = new ArrayList<ISpotifyPlayable>();
		else
			this.artists = artists;
		
		if (artists != null)
			this.tracksInQueue = this.artists.size();
		else
			this.tracksInQueue = 0;
	}

	public List<ISpotifyPlayable> getArtists() {
		return artists;
	}

	public void setArtists(List<SpotifyTrack> artists) {
		if (artists == null)
			this.artists = new ArrayList<ISpotifyPlayable>();
		
		if (artists != null)
			this.tracksInQueue = this.artists.size();
		else
			this.tracksInQueue = 0;
	}

	public int getTracksInQueue() {
		return tracksInQueue;
	}
}
