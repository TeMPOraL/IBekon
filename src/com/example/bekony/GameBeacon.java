package com.example.bekony;

import com.kontakt.sdk.android.device.Beacon;

public class GameBeacon {
	public GameBeacon(Beacon b) {
		beacon = b;
	}

	protected GameBeaconState state;
	protected Player owner;	//nil = uncaptured
	
	protected Beacon beacon;
}
