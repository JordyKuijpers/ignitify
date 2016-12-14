package com.jordykuijpers.spotify;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.jordykuijpers.spotify.SpotifyPlayer.SpotifyPlayer;
import com.jordykuijpers.spotify.shpotifybash.IShpotifyBashWrapper;
import com.jordykuijpers.spotify.shpotifybash.IShpotifyBashWrapper.ResourceType;
import com.jordykuijpers.spotify.shpotifybash.ShpotifyBashWrapper;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;

public class Main {

	public static void main(String[] args) {
		System.out.println("Ignitify - A democratized spotify REST endpoint by Jordy Kuijpers");
		
		try {
			IShpotifyBashWrapper spotify = new ShpotifyBashWrapper("C:\\devprivate\\backend\\ShpotifyBash\\spotify_mock.cmd");
			//IShpotifyBashWrapper spotify = new ShpotifyBashWrapper("/Volumes/DevOps/Ignitify/vibify/shpotify/shpotify/spotify");
			
//			spotify.playResource(ResourceType.TRACK, "Viva");
//			spotify.playResource(ResourceType.TRACK, "Viva la vida");
//			
//			spotify.playResource(ResourceType.ARTIST, "Coldplay");
//			spotify.playResource(ResourceType.ARTIST, "Phil Collins");
//			
//			spotify.playResource(ResourceType.ALBUM, "Bonfire");
//			spotify.playResource(ResourceType.ALBUM, "Brothers in Arms");
//			
//			spotify.playResource(ResourceType.PLAYLIST, "Discover");
//			spotify.playResource(ResourceType.PLAYLIST, "High Performance, concentration!");
//			
			spotify.playResource(ResourceType.URI, "spotify:track:7Dbg5O9nNWu6SWxDjJ9qoq");
//			spotify.playResource(ResourceType.URI, "---");
//			
//			spotify.next();
//			spotify.previous();
//			spotify.replay();
//			
//			spotify.position(10);
//			
//			spotify.pause();
//			spotify.quit();
//			
//			spotify.volumeUp();
//			spotify.volumeDown();
//			spotify.setVolume(100);
//			spotify.setVolume(200);
//			
//			System.out.println("Volume: " + spotify.getVolume());
//			
//			System.out.println("Status: " + spotify.status());
//			
//			spotify.toggleRepeat();
//			spotify.toggleShuffle();
			
			/*
			 * Spotify Web API test
			 */
			SpotifyPlayer spotifyPlayer = new SpotifyPlayer("7da5c2e103f24d54ae168730e8905823", "b58659565d5d4c3f9097420bd1b3ea97");
			new Thread(spotifyPlayer).start();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
