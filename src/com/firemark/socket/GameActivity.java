package com.example.firemark.socket;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;


public class GameActivity extends Activity {

    SocketTask socketTask;
    TextView gameIdText;

    public ArrayList<Beacon> beacons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        socketTask = SocketTask.getSocketTask();
        socketTask.setActivity(this);
        gameIdText = (TextView)findViewById(R.id.gameIdText);
        gameIdText.setText(socketTask.gameId);
    }

}
