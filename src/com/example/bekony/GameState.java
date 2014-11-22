package com.example.bekony;

import java.util.ArrayList;
import java.util.List;

public class GameState {
	
	protected static final long DELAY_BETWEEN_GAME_TICKS = 100;

	public static int BEACON_CAPTURE_TIMEOUT_MSEC = 10000;
	
	public static int BEACON_GAIN_TICK_MSEC = 5000;
	
	public static long GAME_START_TIME_MSEC = 0;
	
	public static Player CURRENT_PLAYER = null;
	
	public static List<Player> ACTIVE_PLAYERS = new ArrayList<Player>();
	
	public static List<String> ACTIVE_BEACONS = new ArrayList<String>();
	
	public static boolean GAME_RUNNING = false;
}
