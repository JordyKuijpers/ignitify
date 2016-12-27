package com.jordykuijpers.spotify.SpotifyPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jordykuijpers.spotify.SpotifyAPIConfigurer;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

@Component
@Scope("prototype")
public class SpotifyTrack implements ISpotifyPlayable {
	private List<SpotifyArtist> artists;
	private String title;
	private String URI;
	private int duration;
	
	private String initialURI;
	
	@Autowired
	SpotifyAPIConfigurer apiConfigurer;
	
	public SpotifyTrack() {
		System.out.println("Gekkebooooooys, gekkeboy!");
	}
	
	public SpotifyTrack(String URI) {
		this.initialURI = URI;
	}
	
	@PostConstruct
	public void initWithURI() {
		String URI = initialURI.substring(initialURI.lastIndexOf(':') + 1);
		
		/* Create a request object. */
		final ClientCredentialsGrantRequest credentialRequest = apiConfigurer.getApi().clientCredentialsGrant().build();
		/*
		 * Use the request object to make the request, either asynchronously
		 * (getAsync) or synchronously (get)
		 */
		try {
			final ClientCredentials clientCredentials = credentialRequest.get();
			apiConfigurer.getApi().setAccessToken(clientCredentials.getAccessToken());
		} catch (Throwable t) {
			System.out.println("An error occurred while getting the acces token.");
		}
		
		final TrackRequest request = apiConfigurer.getApi().getTrack(URI).build();
		Track track;

		try {
			track = request.get();
			this.setURI(track.getUri());
			this.setTitle(track.getName());

			List<SimpleArtist> trackArtistList = track.getArtists();
			List<SpotifyArtist> spotifyArtists = new ArrayList<SpotifyArtist>();
			for (SimpleArtist a : trackArtistList)
				spotifyArtists.add(new SpotifyArtist(a.getName()));

			this.setArtists(spotifyArtists);
			this.setDuration(track.getDuration());

		} catch (IOException | WebApiException e) {
			e.printStackTrace();
		}
	}
	
	
	@JsonIgnore
	public String getURI(boolean onlyID) {
		if (onlyID) {
			String fullURI = this.getURI();
			return fullURI.substring("spotify:track:".length());
		} else
			return this.getURI();
	}

	@JsonIgnore
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

	@JsonIgnore
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
	
	@Override
	public String toString() {
		return new String(this.getArtistsAsFormattedString() + " - " + this.getTitle() + " (" + this.getDurationAsFormattedString() + ")\n");
	}
}
