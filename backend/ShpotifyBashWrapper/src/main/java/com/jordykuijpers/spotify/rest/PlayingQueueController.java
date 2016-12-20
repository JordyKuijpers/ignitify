package com.jordykuijpers.spotify.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.jordykuijpers.spotify.SpotifyPlayer.ISpotifyPlayable;
import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyPlayer;

/*
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
 */

@RestController
public class PlayingQueueController {

	@Autowired
	SpotifyPlayer spotifyPlayer;

	@RequestMapping("/PlayingQueue")
	public PlayingQueue getPlayingQueue() {
		List<ISpotifyPlayable> queueTrackList = Lists.newArrayList(spotifyPlayer.playingQueue.iterator());
		
		PlayingQueue pq = new PlayingQueue(queueTrackList);
		
		return pq;
	}
}
