package com.jordykuijpers.spotify.SpotifyPlayer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import com.jordykuijpers.spotify.shpotifybash.IShpotifyBashWrapper;
import com.jordykuijpers.spotify.shpotifybash.ShpotifyBashWrapper;
import com.jordykuijpers.spotify.shpotifybash.IShpotifyBashWrapper.ResourceType;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

public class SpotifyPlayer implements Runnable {
	protected IShpotifyBashWrapper spotify = null;
	
	protected static final int STEPINTERVAL = 200;
	protected static final int EMPTY_PLAYING_QUEUE_SLEEPTIME = 1000;

	public static enum PlayerState {
		INIT, FETCH, PLAYING, ENDED, ERROR, NONE
	};

	private ConcurrentLinkedQueue<ISpotifyPlayable> playingQueue = new ConcurrentLinkedQueue<ISpotifyPlayable>();
	private String clientId;
	private String clientSecret;
	private Api api;

	private PlayerState playerState = PlayerState.INIT;
	private PlayerState previousPlayerState = PlayerState.NONE;

	private StopWatch stopWatch = new StopWatch();
	private long intervalDuration = 0;
	private long previousIntervalDuration = 0;

	private long currentPlayingTime = 0;

	private SpotifyTrackDTO currentTrack = null;
	
	boolean validSpotifyBash = false;
	
	private int delayCompensation = 500;

	public SpotifyPlayer(String clientId, String clientSecret, String spotifyBashLocation) {
		try {
			this.spotify = new ShpotifyBashWrapper(spotifyBashLocation);
			validSpotifyBash = true;
		} catch (Exception e) {
			e.printStackTrace();
			validSpotifyBash = false;
		}
		
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public void run() {
		if (initialize() && validSpotifyBash) {
			while (true) {
				previousIntervalDuration = intervalDuration;
				intervalDuration = 0;
				stopWatch.start();

				if (previousPlayerState != playerState)
					System.out.println("PlayerState: " + this.playerState.toString());

				switch (this.playerState) {
				case INIT:
					if (!this.playingQueue.isEmpty()) {
						this.changePlayerState(PlayerState.FETCH);
					} else
						this.changePlayerState(PlayerState.INIT);
					break;
				case FETCH:
					if (!this.playingQueue.isEmpty()) {
						/*
						 * 1. Poll new track from queue
						 */
						ISpotifyPlayable playableTrack = this.playingQueue.poll();
						if (playableTrack != null) {
							System.out.println("---------------------------------------");
							String trackURI = playableTrack.getURI(true);

							final TrackRequest request = api.getTrack(trackURI).build();
							Track track;

							try {
								track = request.get();

								/*
								 * 2. Get track info and determine play time
								 */
								String trackDurationInMinSec = this.formatMiliSecToMinSec(track.getDuration());

								List<SimpleArtist> trackArtistList = track.getArtists();
								StringBuilder trackArtists = new StringBuilder();
								for (int i = 0; i < trackArtistList.size(); i++) {
									trackArtists.append(trackArtistList.get(i).getName());
									if (trackArtistList.size() > 1 && i < trackArtistList.size() - 1)
										trackArtists.append(", ");
								}

								currentTrack = new SpotifyTrackDTO(trackArtists.toString(), track.getName(),
										track.getDuration());

								System.out.print("(" + trackDurationInMinSec + ") ");
								System.out.print(trackArtists.toString());
								System.out.print(" - ");
								System.out.println(track.getName());

								this.currentPlayingTime = 0;

								this.spotify.playResource(ResourceType.URI, playableTrack.getURI(false));
								this.changePlayerState(PlayerState.PLAYING);

							} catch (IOException | WebApiException e) {
								System.out.println("SpotifyPlayer [" + this.playerState.toString()
										+ "] ERROR: Trying to poll empty queue");
								this.changePlayerState(PlayerState.ERROR);
							}
						} else {
							System.out.println("SpotifyPlayer [" + this.playerState.toString()
									+ "] ERROR: Could not fetch track information! Internet Connection?");
							this.changePlayerState(PlayerState.ERROR);
						}
					} else {
						this.changePlayerState(PlayerState.INIT);
					}
					break;
				case PLAYING:
					this.currentPlayingTime += this.previousIntervalDuration;
					
					if (this.currentPlayingTime >= this.currentTrack.getDuration() - this.delayCompensation) {
						this.changePlayerState(PlayerState.ENDED);
					} else {
						System.out.println(currentTrack.getArtists() + " - " + currentTrack.getTitle() + "("
								+ this.formatMiliSecToMinSec(this.currentPlayingTime) + "/"
								+ this.formatMiliSecToMinSec(this.currentTrack.getDuration()) + ")");
						
						try {
							Thread.sleep(STEPINTERVAL);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						this.changePlayerState(PlayerState.PLAYING);
					}

					break;
				case ENDED:
					this.changePlayerState(PlayerState.INIT);
					break;
				default:
				case ERROR:
					break;
				}

				stopWatch.stop();
				intervalDuration = stopWatch.getTime();
				stopWatch.reset();
			}
		} else {
			System.out.println("Initialisation of SpotifyPlayer failed!");
		}

	}

	public void addToPlayingQueue(ISpotifyPlayable playable) {
		this.playingQueue.add(playable);
	}
	
	public void setDelayCompensation(int compensationInMiliSec) {
		this.delayCompensation = compensationInMiliSec;
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

	protected void changePlayerState(PlayerState newState) {
		this.previousPlayerState = this.playerState;
		this.playerState = newState;
	}

	protected String formatMiliSecToMinSec(long currentPlayingTime2) {
		return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(currentPlayingTime2),
				TimeUnit.MILLISECONDS.toSeconds(currentPlayingTime2)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPlayingTime2)));
	}
}
