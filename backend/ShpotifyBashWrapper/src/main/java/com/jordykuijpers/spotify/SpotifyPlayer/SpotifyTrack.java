package com.jordykuijpers.spotify.SpotifyPlayer;

public class SpotifyTrack implements ISpotifyPlayable {
	private String URI;
	
	public SpotifyTrack(String URI) {
		if (URI.startsWith("spotify:track:"))
			this.URI = URI;
		else
			this.URI = null;
	}

	public String getURI() {
		return this.URI;
	}

	public String getURI(boolean onlyID) {
		if (onlyID) {
			String fullURI = this.getURI();
			return fullURI.substring("spotify:track:".length());
		}
		else
			return this.getURI();
	}
}
