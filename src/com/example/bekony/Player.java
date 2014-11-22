package com.example.bekony;

public class Player {
	protected String name;
	protected int score = 0;
	
	public Player(String playerName) {
		name = playerName;
	}
	
	public String getId() {
		return name;
	}
}
