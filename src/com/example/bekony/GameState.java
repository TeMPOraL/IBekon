package com.example.bekony;

import java.util.ArrayList;
import java.util.List;

public class GameState {
	
	public static int BEACON_CAPTURE_TIMEOUT_MSEC = 10000;
	
	public static Player CURRENT_PLAYER = null;
	
	public static List<Player> ACTIVE_PLAYERS = new ArrayList<Player>();
	
	public static List<String> ACTIVE_BEACONS = new ArrayList<String>();
}
