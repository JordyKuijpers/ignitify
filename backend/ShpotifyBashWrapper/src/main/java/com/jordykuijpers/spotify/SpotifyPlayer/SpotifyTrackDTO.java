package com.jordykuijpers.spotify.SpotifyPlayer;

public class SpotifyTrackDTO {
	private String artists;
	private String title;
	private int duration;
	
	public SpotifyTrackDTO(String artists, String title, int duration) {
		super();
		this.artists = artists;
		this.title = title;
		this.duration = duration;
	}
	
	public String getArtists() {
		return artists;
	}
	public void setArtists(String artists) {
		this.artists = artists;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "SpotifyTrackDTO [artists=" + artists + ", title=" + title + ", duration=" + duration + "]";
	}
	
	
}
