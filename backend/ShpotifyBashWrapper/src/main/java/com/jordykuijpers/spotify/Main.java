package com.jordykuijpers.spotify;

import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyPlayer;
import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyTrack;

public class Main {

	public static void main(String[] args) {
		System.out.println("Kollektify - A democratized spotify REST endpoint by Jordy Kuijpers");

		SpotifyPlayer spotifyPlayer = new SpotifyPlayer("7da5c2e103f24d54ae168730e8905823",
				"b58659565d5d4c3f9097420bd1b3ea97", "C:\\devprivate\\backend\\ShpotifyBash\\spotify_mock.cmd");
		// SpotifyPlayer spotifyPlayer = new SpotifyPlayer("7da5c2e103f24d54ae168730e8905823",
		// 		"b58659565d5d4c3f9097420bd1b3ea97", "/Volumes/DevOps/Ignitify/vibify/shpotify/shpotify/spotify");
		new Thread(spotifyPlayer).start();

		spotifyPlayer.addToPlayingQueue(new SpotifyTrack("spotify:track:378iszndTZAR4dH8kwsLC6"));
		spotifyPlayer.addToPlayingQueue(new SpotifyTrack("spotify:track:3PKtemUKxiDBvBo7tpQ8bG"));
		spotifyPlayer.addToPlayingQueue(new SpotifyTrack("spotify:track:2CKPPzhBijcIyNx2gXWbpN"));
		spotifyPlayer.addToPlayingQueue(new SpotifyTrack("spotify:track:7Dbg5O9nNWu6SWxDjJ9qoq"));

	}

}
