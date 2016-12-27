package com.jordykuijpers.spotify.rest;

import com.jordykuijpers.spotify.SpotifyPlayer.ISpotifyPlayable;

public class CurrentTrack {
	private ISpotifyPlayable track;
	private String currentPlayingTime;

	public ISpotifyPlayable getTrack() {
		return track;
	}

	public void setTrack(ISpotifyPlayable track) {
		this.track = track;
	}

	public String getCurrentPlayingTime() {
		return currentPlayingTime;
	}

	public void setCurrentPlayingTime(String currentPlayingTime) {
		this.currentPlayingTime = currentPlayingTime;
	}

}
