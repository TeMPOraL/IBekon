package com.example.bekony;

import java.util.List;

import android.widget.TextView;

import com.kontakt.sdk.android.device.Beacon;
import com.kontakt.sdk.core.Proximity;

public class GameBeaconManager {
	
	protected List<GameBeacon> beacons;
	
	TextView hello;
	
	public void init(TextView t) {
		hello = t;
		hello.setText("INIT!");
	}
	
	public GameBeacon beaconToGameBeacon(Beacon b) {
		return null;
	}
	
	public void onBeaconSeen(Beacon b) {
	
	}
	
	public void onBeaconInCaptureRange(Beacon b) {
		GameBeacon gb = beaconToGameBeacon(b);
//		if(b.isOwnedBy(me)) {
			
//		}
//		else {
			//b.capturing();
//		}
	}
	
	public List<GameBeacon> getBeacons() {
		return beacons;
	}

	public void onBeaconAppeared(Beacon beacon) {
		System.out.println("APPEARED");
		//here we don't care
	}

	public void onBeaconUpdated(List<Beacon> beacons) {
		System.out.println("UPDATED");
		for(Beacon b : beacons) {
			System.out.println("BEACON " + b.getMacAddress() + " " + b.getName() + " " + b.getMajor() + " " + b.getMinor() + " " + b.getProximity().toString());
			if(b.getProximity() == Proximity.IMMEDIATE) {
				onBeaconInCaptureRange(b);
			}
		}
	}
}
