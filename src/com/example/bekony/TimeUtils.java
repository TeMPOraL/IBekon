package com.example.bekony;

public class TimeUtils {

	public static String countdownTimeStringFromCurrentAndEndTimes(long end, long current) {
		long deltaS = (end - current) / 1000;
		deltaS = (deltaS >= 0) ? deltaS : 0;
		long minutes = deltaS / 60;
		long seconds = deltaS % 60;
		
		return ((minutes < 10) ? "0" : "") + Long.toString(minutes) + ":" + ((seconds < 10) ? "0" : "") + Long.toString(seconds);
	}
}
