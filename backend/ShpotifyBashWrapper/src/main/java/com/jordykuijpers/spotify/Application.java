package com.jordykuijpers.spotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyPlayer;
import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyTrack;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
    	ConfigurableApplicationContext ctx = new SpringApplication(Application.class).run(args);
    	SpotifyPlayer spotifyPlayer = ctx.getBean("spotifyPlayer", SpotifyPlayer.class);
		new Thread(spotifyPlayer).start();

		spotifyPlayer.addToPlayingQueue(new SpotifyTrack("spotify:track:378iszndTZAR4dH8kwsLC6"));
		spotifyPlayer.addToPlayingQueue(new SpotifyTrack("spotify:track:3PKtemUKxiDBvBo7tpQ8bG"));
		spotifyPlayer.addToPlayingQueue(new SpotifyTrack("spotify:track:2CKPPzhBijcIyNx2gXWbpN"));
		spotifyPlayer.addToPlayingQueue(new SpotifyTrack("spotify:track:7Dbg5O9nNWu6SWxDjJ9qoq"));
    }
    
	@Bean
	public SpotifyPlayer spotifyPlayer() {
		return new SpotifyPlayer("7da5c2e103f24d54ae168730e8905823", "b58659565d5d4c3f9097420bd1b3ea97",
				"C:\\devprivate\\backend\\ShpotifyBash\\spotify_mock.cmd");
	}
}