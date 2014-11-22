package com.example.bekony;

public class ServerInterface {
	
	//output functions
	public static void notifyBeginCapture(String beaconId) {
		System.out.println("[" + GameState.CURRENT_PLAYER.getId() + "]" + "Begin capture: " + beaconId);
	}
	
	public static void notifyCaptured(String beaconId) {
		System.out.println("[" + GameState.CURRENT_PLAYER.getId() + "]" + "Captured: " + beaconId);
	}
	
	//input functions
	public static void captureDenied(String beaconId) {
		
	}
	
	public static void updateFullGameState() {
		
	}
}
