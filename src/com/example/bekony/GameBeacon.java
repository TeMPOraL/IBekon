package com.example.bekony;

import com.kontakt.sdk.android.device.Beacon;

public class GameBeacon {
	public GameBeacon(Beacon b) {
		beacon = b;
	}

	protected GameBeaconState state = GameBeaconState.CAPTURED;
	protected Player owner = null;	//nil = uncaptured
	protected Player attacker = null;
	
	protected Beacon beacon;
	
	protected long captureStartTime;
	
	protected int scoreGain = 10;
	
	public static String getBeaconId(Beacon b) {
		return b.getMacAddress();
	}
	
	public String getId() {
		return GameBeacon.getBeaconId(beacon);
	}
	
	public void tick() {
		//maybe something here one day
	}
	
	public void capturing() {
		long now = android.os.SystemClock.elapsedRealtime();
		if(state != GameBeaconState.IN_CAPTURE && owner != GameState.CURRENT_PLAYER) {
			state = GameBeaconState.IN_CAPTURE;
			captureStartTime = now;
			ServerInterface.notifyBeginCapture(getId());
		}
		else {
			if(now - captureStartTime > GameState.BEACON_CAPTURE_TIMEOUT_MSEC) {
				state = GameBeaconState.CAPTURED;
				owner = GameState.CURRENT_PLAYER;
				ServerInterface.notifyCaptured(getId());
			}
		}
	}
	
	public void notCapturing() {
		state = GameBeaconState.CAPTURED;
	}
	
	public void abortCapture() {
		state = GameBeaconState.CAPTURED;
	}
	
	public GameBeaconState getState() {
		return state;
	}

	public void setState(GameBeaconState newState) {
		state = newState;
	}
	
	public void setOwner(Player newOwner) {
		owner = newOwner;
	}
	
	public void setBeacon(Beacon b) {
		beacon = b;
	}
	
	public int getScoreGain() {
		return scoreGain;
	}
	
	public void setScoreGain(int gain) {
		scoreGain = gain;
	}

	public Player getOwner() {
		return owner;
	}
}
