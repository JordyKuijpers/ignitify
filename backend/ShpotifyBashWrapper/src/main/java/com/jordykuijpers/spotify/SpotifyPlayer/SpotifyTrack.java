package com.jordykuijpers.spotify.SpotifyPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

public class SpotifyTrack implements ISpotifyPlayable {
	private List<SpotifyArtist> artists;
	private String title;
	private String URI;
	private int duration;

	public SpotifyTrack(String URI) {
		if (URI.startsWith("spotify:track:"))
			this.URI = URI;
		else
			this.URI = null;
	}

	public SpotifyTrack(Track track) {
		this.setURI(track.getUri());
		this.setTitle(track.getName());

		List<SimpleArtist> trackArtistList = track.getArtists();
		List<SpotifyArtist> spotifyArtists = new ArrayList<SpotifyArtist>();
		for (SimpleArtist a : trackArtistList)
			spotifyArtists.add(new SpotifyArtist(a.getName()));

		this.setArtists(spotifyArtists);
		this.setDuration(track.getDuration());
	}

	public String getURI(boolean onlyID) {
		if (onlyID) {
			String fullURI = this.getURI();
			return fullURI.substring("spotify:track:".length());
		} else
			return this.getURI();
	}

	public String getURI() {
		return this.URI;
	}

	public void setURI(String URI) {
		this.URI = URI;
	}

	public List<SpotifyArtist> getArtists() {
		return artists;
	}

	public void setArtists(List<SpotifyArtist> artists) {
		this.artists = artists;
	}

	public String getArtistsAsFormattedString() {
		List<SpotifyArtist> artists = this.getArtists();
		if (artists != null) {
			if (artists.size() == 0)
				return "";
			else if (artists.size() == 1)
				return artists.get(0).getName();
			else {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < artists.size(); i++) {
					String a = artists.get(i).getName();
					if (i < artists.size() - 1) 
						sb.append(a + ", ");
					else
						sb.append(a);
				}
				return sb.toString();
			}
		} else
			return "N/A";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDuration() {
		return duration;
	}

	public String getDurationAsFormattedString() {
		return formatDurationAsString(this.getDuration());
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public static String formatDurationAsString(int duration) {
		return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
				TimeUnit.MILLISECONDS.toSeconds(duration)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
	}
}
