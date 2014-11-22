package com.example.bekony;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.widget.TextView;

import com.kontakt.sdk.android.device.Beacon;
import com.kontakt.sdk.core.Proximity;

public class GameBeaconManager {
	
	protected Hashtable<String, GameBeacon> beacons = new Hashtable<String, GameBeacon>();
	
	protected List<String> allowedBeacons = new ArrayList<String>();
	
	TextView hello;

	public int computeTotalScoreGain() {
		int gain = 0;
		List <GameBeacon> beacons = getBeacons();
		
		for(GameBeacon b : beacons) {
			if(b.getOwner() == GameState.CURRENT_PLAYER) {
				gain += b.getScoreGain();
			}
		}
		
		return gain;
	}
	
	public GameBeacon getGameBeaconFromBeacon(Beacon b) {
		GameBeacon gb = beacons.get(GameBeacon.getBeaconId(b));
		if(gb == null) {
			registerBeacon(b);
			gb = beacons.get(GameBeacon.getBeaconId(b));
		}
		gb.setBeacon(b); //this is evil.
		return gb;
	}
	
	public void registerBeacon(Beacon b) {
		beacons.put(GameBeacon.getBeaconId(b), new GameBeacon(b));
	}
	
	public void onBeaconSeen(Beacon b) {
		
	}
	
	public void onBeaconInCaptureRange(Beacon b) {
		GameBeacon gb = getGameBeaconFromBeacon(b);
		//TODO comm with server
		gb.capturing();
//		if(b.isOwnedBy(me)) {
			
//		}
//		else {
			//b.capturing();
//		}
	}
	
	private void onBeaconOutOfCaptureRange(Beacon b) {
		GameBeacon gb = getGameBeaconFromBeacon(b);
		gb.notCapturing();
	}
	
	public List<GameBeacon> getBeacons() {
		List<GameBeacon> beaconsList =  new ArrayList<GameBeacon>();
		Enumeration<GameBeacon> currentBeacons = beacons.elements();
		while(currentBeacons.hasMoreElements()) {
			beaconsList.add(currentBeacons.nextElement());
		}
		return beaconsList;
	}
	
	public Boolean isInCaptureRange(Beacon b) {
		return b.getProximity() == Proximity.IMMEDIATE;
	}

	public void onBeaconAppeared(Beacon beacon) {
		System.out.println("APPEARED");
		//here we don't care
	}

	public void onBeaconsUpdated(List<Beacon> beacons) {
		for(Beacon b : beacons) {
			if(allowedBeacon(b)) {
				if(isInCaptureRange(b)) {
					onBeaconInCaptureRange(b);
				}	
				else {
					onBeaconOutOfCaptureRange(b);
				}
			}
		}
	}

	private boolean allowedBeacon(Beacon b) {
		for(String s : allowedBeacons) {
			if(s.equals(GameBeacon.getBeaconId(b))) {
				return true;
			}
		}
		//return false; //uncomment to enable filtering
		return true;
	}
	
	public void syncStateWithServer() {
		
	}


}
