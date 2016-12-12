package com.jordykuijpers.spotify.shpotifybash;

/*
spotify play                       Resumes playback where Spotify last left off.
	spotify play <song name>           Finds a song by name and plays it.
	spotify play album <album name>    Finds an album by name and plays it.
	spotify play artist <artist name>  Finds an artist by name and plays it.
	spotify play list <playlist name>  Finds a playlist by name and plays it.
	spotify play uri <uri>             Play songs from specific uri.

	spotify next                       Skips to the next song in a playlist.
	spotify prev                       Returns to the previous song in a playlist.
	spotify replay                     Replays the current track from the beginning.
	spotify pos <time>                 Jump to a specific time (in seconds) in the current song.
	spotify pause                      Pauses/Resumes Spotify playback.
	spotify quit                       Stops playback and quits Spotify.

	spotify vol up                     Increases the volume by 10%.
	spotify vol down                   Decreases the volume by 10%.
	spotify vol <amount>               Sets the volume to an amount between 0 and 100.
	spotify vol [show]                 Shows the current volume.

	spotify status                     Shows the play status, including the current song details.

	spotify share                      Displays the current song's Spotify URL and URI.
	spotify share url                  Displays the current song's Spotify URL and copies it to the clipboard.
	spotify share uri                  Displays the current song's Spotify URI and copies it to the clipboard.

	spotify toggle shuffle             Toggles shuffle playback mode.
	spotify toggle repeat              Toggles repeat playback mode.
 */

public interface IShpotifyBashWrapper {
	public enum ResourceType { TRACK, ALBUM, ARTIST, PLAYLIST, URI };

	public boolean playResource(ResourceType resource, String resourceData);

	public boolean next();
	public boolean previous();
	public boolean replay();
	public boolean position(int seconds);
	public boolean pause();
	public boolean quit();

	public boolean volumeUp();
	public boolean volumeDown();
	public boolean setVolume(int volumeInPercent);
	public int getVolume();

	public String status();

	public boolean toggleShuffle();
	public boolean toggleRepeat();
}
