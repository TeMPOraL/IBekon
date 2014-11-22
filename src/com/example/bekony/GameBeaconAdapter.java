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
		
		tvName.setText(beacon.beacon.getName() + " " + String.format("%.4f", beacon.beacon.getAccuracy()) + " --- ");
		tvState.setText(beacon.getState().toString());
		
		return convertView;
	}
	
}
