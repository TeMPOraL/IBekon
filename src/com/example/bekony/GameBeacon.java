package com.example.bekony;

import com.kontakt.sdk.android.device.Beacon;

public class GameBeacon {
	public GameBeacon(Beacon b) {
		beacon = b;
	}

	protected GameBeaconState state = GameBeaconState.CAPTURED;
	protected Player owner;	//nil = uncaptured
	
	protected Beacon beacon;
	
	public GameBeaconState getState() {
		return state;
	}

	public void setState(GameBeaconState newState) {
		state = newState;
	}
	
	public void setOwner(Player newOwner) {
		owner = newOwner;
	}
}
