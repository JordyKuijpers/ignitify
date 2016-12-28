package com.jordykuijpers.spotify.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.jordykuijpers.spotify.SpotifyPlayer.ISpotifyPlayable;
import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyPlayer;

@RestController
public class PlayingQueueController {

	@Autowired
	SpotifyPlayer spotifyPlayer;

	@RequestMapping("/PlayingQueue")
	@CrossOrigin(origins = "http://localhost:8002")
	public PlayingQueue getPlayingQueue() {
		List<ISpotifyPlayable> queueTrackList = Lists.newArrayList(spotifyPlayer.playingQueue.iterator());
		
		PlayingQueue pq = new PlayingQueue(queueTrackList);
		
		return pq;
	}
}
