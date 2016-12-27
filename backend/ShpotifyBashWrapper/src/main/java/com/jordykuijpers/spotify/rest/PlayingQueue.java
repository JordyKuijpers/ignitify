package com.jordykuijpers.spotify.rest;

import java.util.ArrayList;
import java.util.List;

import com.jordykuijpers.spotify.SpotifyPlayer.ISpotifyPlayable;
import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyTrack;

public class PlayingQueue {
	private List<ISpotifyPlayable> tracks;
	private int tracksInQueue;
	
	public PlayingQueue(List<ISpotifyPlayable> tracks) {
		super();
		
		if (tracks == null)
			this.tracks = new ArrayList<ISpotifyPlayable>();
		else
			this.tracks = tracks;
		
		if (tracks != null)
			this.tracksInQueue = this.tracks.size();
		else
			this.tracksInQueue = 0;
	}

	public List<ISpotifyPlayable> getTracks() {
		return tracks;
	}

	
	public void setTracks(List<SpotifyTrack> tracks) {
		if (tracks == null)
			this.tracks = new ArrayList<ISpotifyPlayable>();
		
		if (tracks != null)
			this.tracksInQueue = this.tracks.size();
		else
			this.tracksInQueue = 0;
	}

	public int getTracksInQueue() {
		return tracksInQueue;
	}
}
