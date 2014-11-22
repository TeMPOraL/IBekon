package com.example.bekony;

import java.util.concurrent.CopyOnWriteArrayList;

public class ServerInterface {
	
	public static CopyOnWriteArrayList<RemoteBeacon> beacons = new CopyOnWriteArrayList<RemoteBeacon>();
	
	//output functions
	public static void notifyBeginCapture(String beaconId) {
		System.out.println("[" + GameState.CURRENT_PLAYER.getId() + "]" + "Begin capture: " + beaconId);

		RemoteBeacon r = new RemoteBeacon();
		r.id = beaconId;
		SocketTask.getSocketTask().sendCapture(r);
	}
	
	public static void notifyCaptured(String beaconId) {
		System.out.println("[" + GameState.CURRENT_PLAYER.getId() + "]" + "Captured: " + beaconId);
		
		RemoteBeacon r = new RemoteBeacon();
		r.id = beaconId;
		SocketTask.getSocketTask().sendCaptured(r);
	}
	
	//input functions
	public static void captureDenied(String beaconId) {
		
	}
}
