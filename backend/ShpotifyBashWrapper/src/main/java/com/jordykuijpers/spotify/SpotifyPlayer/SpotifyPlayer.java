package com.jordykuijpers.spotify.SpotifyPlayer;

import java.util.Queue;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;

public class SpotifyPlayer implements Runnable {
	private Queue<ISpotifyPlayable> playingQueue;
	private String clientId;
	private String clientSecret;
	private Api api;

	public SpotifyPlayer(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}
	
	public void run() {

		if (initialize()) {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Heartbeat!");
				// TODO: Keep time
				// TODO: If track ends play next in queue
				// TODO: Delete track from Queue
				// TODO: Generate status info?
			}
		} else {
			System.out.println("Initialisation of SpotifyPlayer failed!");
		}
	}

	protected boolean initialize() {
		api = Api.builder().clientId(clientId).clientSecret(clientSecret).build();

		/* Create a request object. */
		final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

		boolean success = false;
		/*
		 * Use the request object to make the request, either asynchronously
		 * (getAsync) or synchronously (get)
		 */
		try {
			final ClientCredentials clientCredentials = request.get();
			api.setAccessToken(clientCredentials.getAccessToken());
			success = true;
		} catch (Throwable t) {
			System.out.println("An error occurred while getting the acces token.");
		}

		return success;
	}
}
