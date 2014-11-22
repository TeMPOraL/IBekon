package com.example.firemark.socket;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;


public class HostActivity extends Activity {

    SocketTask socketTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        socketTask = SocketTask.getSocketTask();
        if (socketTask == null){
            String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            socketTask = new SocketTask(android_id);
            socketTask.execute();
        }

        socketTask.setActivity(this);
    }

    private Integer getInt(int id) {
        EditText editText = (EditText)findViewById(id);
        String text = editText.getText().toString();
        return Integer.parseInt(text);
    }

    private Double getFloat(int id) {
        EditText editText = (EditText)findViewById(id);
        String text = editText.getText().toString();
        return Double.parseDouble(text);
    }

    public void btnHostClick(View v){
        GameSettings settings = new GameSettings();
        settings.beaconDelay = getInt(R.id.beaconDelay);
        settings.goneTime = getFloat(R.id.goneTime);
        settings.victoryPoints = getInt(R.id.victoryPoints);
        settings.pointsPerTrick = getInt(R.id.pointsPerTick);
        settings.tickPeriod = getInt(R.id.tickPeriod);

        ArrayList<Beacon> beacons = new ArrayList<Beacon>();

        socketTask.sendHost(settings, beacons);
    }
}
