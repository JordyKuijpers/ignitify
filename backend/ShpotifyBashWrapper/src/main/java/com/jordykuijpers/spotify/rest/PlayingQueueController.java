package com.jordykuijpers.spotify.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		return null;
	}
}
