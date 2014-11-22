package com.example.bekony;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GameBeaconAdapter extends ArrayAdapter<GameBeacon> {

	public GameBeaconAdapter(Context context, List<GameBeacon> objects) {
		super(context, 0, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GameBeacon beacon = getItem(position);
		
		if(convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.beacon_list_item, parent, false);
		}
		
		TextView tvName = (TextView)convertView.findViewById(R.id.tvBeaconName);
		TextView tvState = (TextView)convertView.findViewById(R.id.tvBeaconState);
		TextView tvOwnerName = (TextView)convertView.findViewById(R.id.tvOwnerName);
		
		long timeSinceCaptured = 0;
		
		if(beacon.getState() == GameBeaconState.IN_CAPTURE) {
			timeSinceCaptured = (GameState.BEACON_CAPTURE_TIMEOUT_MSEC - (android.os.SystemClock.elapsedRealtime() - beacon.captureStartTime)) / 1000;	
		}
		
		String playerString = "<NEUTRAL>";
		if(beacon.owner != null) {
			playerString = beacon.owner.getId();
		}
		
		tvName.setText(beacon.beacon.getName() + " " + String.format("%.4f", beacon.beacon.getAccuracy()) + " (" + timeSinceCaptured + ")");
		tvState.setText(beacon.getState().toString());
		tvOwnerName.setText(playerString);
		
		return convertView;
	}
	
}
