package com.jordykuijpers.spotify.shpotifybash;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.SystemUtils;

public class ShpotifyBashWrapper implements IShpotifyBashWrapper {
	protected static final boolean DEV_IGNORE_DEPENDENCY_CHECK = true;
	protected static final boolean DEV_IGNORE_FILE_CORRECTNESS = true;
	protected static final boolean DEV_INHERIT_OUTPUT_STREAMING = true;
	protected static final boolean DEV_USES_MOCKUP_BASH = true;
	
	protected static final int PROCESS_TIMEOUT = 2000;
	
	protected File executingFile = null;

	public ShpotifyBashWrapper(String shpotifyFileLocation) throws Exception {
		if (!this.dependencyCheck())
			throw new Exception("Dependency check failed. This wrapper currently supports Mac OS X and Linux!");

		File f = new File(shpotifyFileLocation);
		if (f.exists() && !f.isDirectory()) {
			if (this.isCorrectBashFile(f)) {
				System.out.print("Jay! Correct file: ");
				System.out.println(f.toPath().toAbsolutePath());
				
				this.executingFile = f;
			} else
				throw new Exception("File was found, but it looks like you did NOT provide the shpotify bash file!");
		} else
			throw new Exception("Shpotify bash file not found on provided path: \"" + shpotifyFileLocation + "\"");
	}
	
	public boolean playResource(ResourceType resource, String resourceData) {
		if (resource == null || resourceData == null || resourceData.isEmpty())
			return false;
		
		ProcessResult pResult;
		List<String> argList = new ArrayList<String>();
		
		switch (resource) {
		case TRACK:
			break;
		case ALBUM:
			argList.add("album");
			break;
		case ARTIST:
			argList.add("artist");
			break;
		case PLAYLIST: 
			argList.add("list");
			break;
		case URI: 
			argList.add("uri");
			if (!resourceData.startsWith("spotify:")) {
				System.out.println("WARNING: \"" + resourceData + "\" is not a valid Spotify URI! Stopping execution of command...");
				return false;
			}
			break;
		default: 
			return false;
		}
		
		argList.add(resourceData);
		pResult = this.spotifyProcessExecutor("play", argList, ShpotifyBashWrapper.PROCESS_TIMEOUT);
		
		return pResult.isSuccessExitCode();
	}
	

	public boolean next() {
		return this.spotifyProcessExecutor("next", ShpotifyBashWrapper.PROCESS_TIMEOUT).isSuccessExitCode();
	}

	public boolean previous() {
		return this.spotifyProcessExecutor("prev", ShpotifyBashWrapper.PROCESS_TIMEOUT).isSuccessExitCode();
	}

	public boolean replay() {
		return this.spotifyProcessExecutor("replay", ShpotifyBashWrapper.PROCESS_TIMEOUT).isSuccessExitCode();
	}

	public boolean position(int seconds) {
		if (seconds < 0) {
			System.out.println("WARNING: \"" + seconds + " is not a valid input for seconds. Stopping execution of command...");
			return false;
		}
		
		return this.spotifyProcessExecutor("pos", Integer.toString(seconds), ShpotifyBashWrapper.PROCESS_TIMEOUT).isSuccessExitCode();
	}

	public boolean pause() {
		return this.spotifyProcessExecutor("pause", ShpotifyBashWrapper.PROCESS_TIMEOUT).isSuccessExitCode();
	}

	public boolean quit() {
		return this.spotifyProcessExecutor("quit", ShpotifyBashWrapper.PROCESS_TIMEOUT).isSuccessExitCode();
	}

	public boolean volumeUp() {
		return this.spotifyProcessExecutor("vol", "up", ShpotifyBashWrapper.PROCESS_TIMEOUT).isSuccessExitCode();
	}

	public boolean volumeDown() {
		return this.spotifyProcessExecutor("vol", "down", ShpotifyBashWrapper.PROCESS_TIMEOUT).isSuccessExitCode();
	}

	public boolean setVolume(int volumeInPercent) {
		if (volumeInPercent < 0 || volumeInPercent > 100) {
			System.out.println("WARNING: \"" + volumeInPercent + "\" is not a valid percentage! Stopping execution of command...");
			return false;
		}
		
		return this.spotifyProcessExecutor("vol", Integer.toString(volumeInPercent), ShpotifyBashWrapper.PROCESS_TIMEOUT).isSuccessExitCode();
	}

	public int getVolume() {
		ProcessResult pResult;
		if (!ShpotifyBashWrapper.DEV_USES_MOCKUP_BASH)
			pResult = this.spotifyProcessExecutor("vol", ShpotifyBashWrapper.PROCESS_TIMEOUT);
		else
			pResult = this.spotifyProcessExecutor("vol", "73", ShpotifyBashWrapper.PROCESS_TIMEOUT);
		
		String streamOutput = pResult.getStreamOutput();
		return Integer.parseInt(streamOutput.replaceAll("[^0-9]", ""));
	}

	public String status() {
		ProcessResult pResult = this.spotifyProcessExecutor("status", ShpotifyBashWrapper.PROCESS_TIMEOUT);
		return pResult.getStreamOutput();
	}

	public boolean toggleShuffle() {
		ProcessResult pResult = this.spotifyProcessExecutor("toggle", "shuffle", ShpotifyBashWrapper.PROCESS_TIMEOUT);
		return pResult.isSuccessExitCode();
	}

	public boolean toggleRepeat() {
		ProcessResult pResult = this.spotifyProcessExecutor("toggle", "repeat", ShpotifyBashWrapper.PROCESS_TIMEOUT);
		return pResult.isSuccessExitCode();
	}

	protected boolean dependencyCheck() {
		if (!SystemUtils.IS_OS_LINUX || !SystemUtils.IS_OS_MAC) {
			if (ShpotifyBashWrapper.DEV_IGNORE_DEPENDENCY_CHECK)
				return true;
			else
				return false;
		}
		else
			return true;
	}

	protected boolean isCorrectBashFile(File shpotifyFile) {
		if (ShpotifyBashWrapper.DEV_IGNORE_FILE_CORRECTNESS)
			return true;
		
		List<String> fileLines;
		try {
			fileLines = Files.readAllLines(shpotifyFile.toPath());

			if (fileLines.size() > 2) {
				if (fileLines.get(0).equals("#!/usr/bin/env bash") 
					&& fileLines.get(2).equals("# Copyright (c) 2012--2016 Harish Narayanan <mail@harishnarayanan.org>")) {
					return true;
				}
			}
		} catch (IOException e) {
			return false;
		}

		return false;
	}
	
	protected ProcessResult spotifyProcessExecutor(String command, long timeoutInMiliSec) {
		List<String> argsList = new ArrayList<String>();
		return this.spotifyProcessExecutor(command, argsList, timeoutInMiliSec);
	}

	protected ProcessResult spotifyProcessExecutor(String command, String args, long timeoutInMiliSec) {
		List<String> argsList = new ArrayList<String>();
		argsList.add(args);
		
		return this.spotifyProcessExecutor(command, argsList, timeoutInMiliSec);
	}
	
	protected ProcessResult spotifyProcessExecutor(String command, List<String> args, long timeoutInMiliSec) {
		if (this.executingFile == null || command == null)
			return new ProcessResult(DefaultExecutor.INVALID_EXITVALUE, null);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		 
		CommandLine cmdLine = new CommandLine(this.executingFile);
		cmdLine.addArgument(command);
		
		if (args != null) {
			for (String s : args) {
				if (s != null)
					cmdLine.addArgument(s, true);
			}
		}
		
		DefaultExecutor executor = new DefaultExecutor();
		ExecuteWatchdog watchDog = new ExecuteWatchdog(timeoutInMiliSec);
		executor.setWatchdog(watchDog);
		executor.setStreamHandler(streamHandler);
		
		try {
			System.out.println("In spotifyProcessExecutor: " + cmdLine.toString());
			int exitCode = executor.execute(cmdLine);
			String streamOutput = outputStream.toString();
			
			if (ShpotifyBashWrapper.DEV_INHERIT_OUTPUT_STREAMING)
				System.out.println("DEV_OUTPUT_STREAM: " + streamOutput);
			
			return new ProcessResult(exitCode, streamOutput);
		} catch (ExecuteException e) {
			e.printStackTrace();
			return new ProcessResult(DefaultExecutor.INVALID_EXITVALUE, null);
		} catch (IOException e) {
			e.printStackTrace();
			return new ProcessResult(DefaultExecutor.INVALID_EXITVALUE, null);
		}
	}
}
