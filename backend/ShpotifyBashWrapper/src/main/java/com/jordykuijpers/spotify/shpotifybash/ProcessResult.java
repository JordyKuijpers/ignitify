package com.jordykuijpers.spotify.shpotifybash;

public class ProcessResult {
	private final int _exitCode;
	private final String _streamOutput;
	
	public ProcessResult(int exitCode, String streamOutput) {
		_exitCode = exitCode;
		_streamOutput = streamOutput;
	}
	
	public int getExitCode() {
		return this._exitCode;
	}
	
	public String getStreamOutput() {
		return this._streamOutput;
	}
	
	public boolean isSuccessExitCode() {
		return (_exitCode == 0);
	}
	
	public boolean isFailExitCode() {
		return !this.isSuccessExitCode();
	}
}
