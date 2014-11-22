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
	
	public GameBeaconManager() {
		allowedBeacons.add("CB:07:48:2C:2F:AB");
		allowedBeacons.add("DD:99:6F:92:0E:DF");
		allowedBeacons.add("DD:5D:BA:6E:B1:F7");
		allowedBeacons.add("D0:15:D7:26:45:DC");
		allowedBeacons.add("FD:C1:2B:94:05:7E");
	}
	
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
		return false; //uncomment to enable filtering
		//return true;
	}
	
	public void updateABeacon(RemoteBeacon b) {
		GameBeacon gb = beacons.get(b.id);
		if(gb != null) {
			if(b.owner.equals(GameState.CURRENT_PLAYER.getId())) {
				if(b.state.equals("inCapture")) {
					gb.setState(GameBeaconState.CAPTURING);
				}
				else {
					gb.setState(GameBeaconState.OWNED);
				}
			}
			else if(b.owner.equals("neutral")) {
				if(b.state.equals("inCapture")) {
					if(gb.getState() != GameBeaconState.CAPTURING) {
						gb.setState(GameBeaconState.UNDER_ATTACK);
					}
				}
				else {
					gb.setState(GameBeaconState.NEUTRAL);
				}
			}
			else if(b.owner != null) {
				gb.setState(GameBeaconState.ENEMY);
			}
		}
	}
	
	public void syncStateWithServer() {
		
	}


}
