package com.jordykuijpers.spotify.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyPlayer;

@RestController
public class CurrentTrackController {
	@Autowired
	SpotifyPlayer spotifyPlayer;
	
	@RequestMapping("/CurrentTrack")
	@CrossOrigin(origins = "http://localhost:8002")
	public CurrentTrack getCurrentTrack() {
		CurrentTrack ct = new CurrentTrack();
		ct.setTrack(spotifyPlayer.getCurrentTrack());
		ct.setCurrentPlayingTime(spotifyPlayer.getCurrentPlayingTime());
		return ct;
	}
}
