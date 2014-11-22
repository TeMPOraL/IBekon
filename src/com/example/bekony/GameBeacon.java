package com.example.bekony;

import com.kontakt.sdk.android.device.Beacon;

public class GameBeacon {
	public GameBeacon(Beacon b) {
		beacon = b;
	}

	protected GameBeaconState state = GameBeaconState.NEUTRAL;
	protected Player owner = null;	//nil = uncaptured
	
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
	
	//--
	public boolean canBeginCapture() {
		return (state != GameBeaconState.OWNED) && (state != GameBeaconState.CAPTURING);
	}
	
	public void beginCapture() {
		captureStartTime = android.os.SystemClock.elapsedRealtime();
		state = GameBeaconState.CAPTURING;
		ServerInterface.notifyBeginCapture(getId());
	}
	
	public void finalizeCapture() {
		state = GameBeaconState.OWNED;
		owner = GameState.CURRENT_PLAYER;
		ServerInterface.notifyCaptured(getId());		
	}
	
	//--
	public void capturing() {
		long now = android.os.SystemClock.elapsedRealtime();
		if(canBeginCapture()) {
			beginCapture();
		}
		if((now - captureStartTime > GameState.BEACON_CAPTURE_TIMEOUT_MSEC) && !(state == GameBeaconState.OWNED)) {
			finalizeCapture();
		}
	}
	
	public void notCapturing() {
		//revert state
		if(owner == null) {
			state = GameBeaconState.NEUTRAL;
		}
		else if(owner == GameState.CURRENT_PLAYER) {
			state = GameBeaconState.OWNED;
		}
		else {
			state = GameBeaconState.ENEMY;
		}
	}
	
	public void abortCapture() {
		state = GameBeaconState.NEUTRAL;
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
