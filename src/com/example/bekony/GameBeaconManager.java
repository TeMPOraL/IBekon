package com.example.bekony;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.widget.TextView;

import com.kontakt.sdk.android.device.Beacon;
import com.kontakt.sdk.core.Proximity;

public class GameBeaconManager {
	
	protected Hashtable<String, GameBeacon> beacons;
	
	TextView hello;
	
	public void init(TextView t) {
		hello = t;
		hello.setText("INIT!");
		
		beacons = new Hashtable<String, GameBeacon>();
	}
	
	public GameBeacon getGameBeaconFromBeacon(Beacon b) {
		GameBeacon gb = beacons.get(b.getMacAddress());
		if(gb == null) {
			registerBeacon(b);
			gb = beacons.get(b.getMacAddress());
		}
		return gb;
	}
	
	public void registerBeacon(Beacon b) {
		beacons.put(b.getMacAddress(), new GameBeacon(b));
	}
	
	public void onBeaconSeen(Beacon b) {
		
	}
	
	public void onBeaconInCaptureRange(Beacon b) {
		GameBeacon gb = getGameBeaconFromBeacon(b);
		//TODO comm with server
		gb.setState(GameBeaconState.IN_CAPTURE);
//		if(b.isOwnedBy(me)) {
			
//		}
//		else {
			//b.capturing();
//		}
	}
	
	private void onBeaconOutOfCaptureRange(Beacon b) {
		GameBeacon gb = getGameBeaconFromBeacon(b);
		gb.setState(GameBeaconState.CAPTURED);
	}
	
	public List<GameBeacon> getBeacons() {
		List<GameBeacon> beaconsList =  new ArrayList<GameBeacon>();
		Enumeration<GameBeacon> currentBeacons = beacons.elements();
		while(currentBeacons.hasMoreElements()) {
			beaconsList.add(currentBeacons.nextElement());
		}
		return beaconsList;
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
			else {
				onBeaconOutOfCaptureRange(b);
			}
		}
	}


}
