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
		
		spotifyPlayer.addToPlayingQueue(ctx.getBean(SpotifyTrack.class, "spotify:track:378iszndTZAR4dH8kwsLC6")); //Sia, Sean Paul - Cheap Thrills
		spotifyPlayer.addToPlayingQueue(ctx.getBean(SpotifyTrack.class, "spotify:track:3PKtemUKxiDBvBo7tpQ8bG")); //U2 - One
		spotifyPlayer.addToPlayingQueue(ctx.getBean(SpotifyTrack.class, "spotify:track:5ghIJDpPoe3CfHMGu71E6T")); //Nirvana - Smells like teen spirit
		spotifyPlayer.addToPlayingQueue(ctx.getBean(SpotifyTrack.class, "spotify:track:7Dbg5O9nNWu6SWxDjJ9qoq")); //Phil Collins - In the air tonight

	}

	@Bean
	public SpotifyPlayer spotifyPlayer() {
		return new SpotifyPlayer(
				// "C:\\Ignitify\\Ignitify\\backend\\ShpotifyBash\\spotify_mock.cmd");
				"C:\\devprivate\\backend\\ShpotifyBash\\spotify_mock.cmd");
	}
	
	@Bean
	public SpotifyAPIConfigurer spotifyAPIConfigurer() {
		return new SpotifyAPIConfigurer();
	}

}