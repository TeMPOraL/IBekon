package com.example.bekony;

import com.kontakt.sdk.android.device.Beacon;

public class GameBeacon {
	public GameBeacon(Beacon b) {
		beacon = b;
	}

	protected GameBeaconState state = GameBeaconState.CAPTURED;
	protected Player owner;	//nil = uncaptured
	
	protected Beacon beacon;
	
	protected long captureStartTime;
	
	public void tick() {
		//maybe something here one day
	}
	
	public void capturing() {
		long now = android.os.SystemClock.elapsedRealtime();
		if(state != GameBeaconState.IN_CAPTURE) {
			state = GameBeaconState.IN_CAPTURE;
			captureStartTime = now;
		}
		else {
			if(now - captureStartTime > GameState.BEACON_CAPTURE_TIMEOUT_MSEC) {
				state = GameBeaconState.CAPTURED;
				//send message to server
			}
		}
	}
	
	public void notCapturing() {
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
}
