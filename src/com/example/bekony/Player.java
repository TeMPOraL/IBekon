package com.example.bekony;

public class Player {
	protected String name;
	protected int score = 0;
	
	private long lastTimeIncremented = 0;
	
	public Player(String playerName) {
		name = playerName;
	}
	
	public String getId() {
		return name;
	}

	public int getScore() {
		return score;
	}
	
	public boolean timeToIncrementScore() {
		if((android.os.SystemClock.elapsedRealtime() - lastTimeIncremented) > GameState.BEACON_GAIN_TICK_MSEC) {
			return true;
		}
		return false;
	}
	
	public void incrementScore(int gain) {
		score += gain;
		lastTimeIncremented = android.os.SystemClock.elapsedRealtime();
	}
}
