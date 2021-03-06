package com.jordykuijpers.spotify.SpotifyPlayer;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jordykuijpers.spotify.shpotifybash.IShpotifyBashWrapper;
import com.jordykuijpers.spotify.shpotifybash.ShpotifyBashWrapper;

@Component
@Scope("singleton")
public class SpotifyPlayer implements Runnable {
	protected IShpotifyBashWrapper spotify = null;
	
	protected static final int STEPINTERVAL = 200;
	protected static final int EMPTY_PLAYING_QUEUE_SLEEPTIME = 1000;

	public static enum PlayerState {
		INIT, FETCH, PLAYING, ENDED, ERROR, NONE
	};

	public ConcurrentLinkedQueue<SpotifyTrack> playingQueue = new ConcurrentLinkedQueue<SpotifyTrack>();

	private PlayerState playerState = PlayerState.INIT;
	private PlayerState previousPlayerState = PlayerState.NONE;

	private StopWatch stopWatch = new StopWatch();
	private long intervalDuration = 0;
	private long previousIntervalDuration = 0;

	private long currentPlayingTime = 0;

	private SpotifyTrack currentTrack = null;
	
	boolean validSpotifyBash = false;
	
	private int delayCompensation = 500;
	private boolean _purgeQueueFlag = false;

	public SpotifyPlayer(String spotifyBashLocation) {
		try {
			this.spotify = new ShpotifyBashWrapper(spotifyBashLocation);
			validSpotifyBash = true;
		} catch (Exception e) {
			e.printStackTrace();
			validSpotifyBash = false;
		}
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
					} else {
						try {
							Thread.sleep(STEPINTERVAL);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						this.changePlayerState(PlayerState.INIT);
					}
					break;
				case FETCH:
					if (!this.playingQueue.isEmpty()) {
						/*
						 * 1. Poll new track from queue
						 */
						SpotifyTrack playableTrack = this.playingQueue.poll();
						if (playableTrack != null) {
							System.out.println("---------------------------------------");
							//System.out.println(playableTrack.toString());
							currentTrack = playableTrack;
							this.changePlayerState(PlayerState.PLAYING);
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
						if (this._purgeQueueFlag)
							this.purgePlayingQueue(false);
						
						this.changePlayerState(PlayerState.ENDED);
					} else {
						System.out.println(currentTrack.getArtistsAsFormattedString() + " - " + currentTrack.getTitle() + "("
								+ SpotifyTrack.formatDurationAsString((int) this.currentPlayingTime) + "/"
								+ currentTrack.getDurationAsFormattedString() + ")");
						
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
					this.currentPlayingTime = 0;
					break;
				default:
				case ERROR:
					System.out.println("In error state!");
					try {
						Thread.sleep(STEPINTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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

	public void addToPlayingQueue(SpotifyTrack playable) {
		
		this.playingQueue.add(playable);
	}
	
	public void purgePlayingQueue(boolean waitUntilCurrentTrackEnds) {
		if (waitUntilCurrentTrackEnds) {
			if (this.playerState == PlayerState.PLAYING)
				this._purgeQueueFlag = true;
		}
		else {
			while (!this.playingQueue.isEmpty())
				this.playingQueue.poll();
			
			this._purgeQueueFlag = false;
		}
	}
	
	public SpotifyTrack getCurrentTrack() {
		return this.currentTrack;
	}
	
	public String getCurrentPlayingTime() {
		return SpotifyTrack.formatDurationAsString((int) this.currentPlayingTime);
	}
	
	public void setDelayCompensation(int compensationInMiliSec) {
		this.delayCompensation = compensationInMiliSec;
	}

	protected boolean initialize() {
		return true;
	}

	protected void changePlayerState(PlayerState newState) {
		this.previousPlayerState = this.playerState;
		this.playerState = newState;
	}
}
