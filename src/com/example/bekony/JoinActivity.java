package com.example.bekony;


import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;


public class JoinActivity extends Activity {

    SocketTask socketTask;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        text = (EditText)findViewById(R.id.beaconDelay);
        socketTask = SocketTask.getSocketTask();
        if (socketTask == null) {
            String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            socketTask = new SocketTask(android_id);
            socketTask.execute();
        }
        socketTask.setActivity(this);
    }

    public void btnJoinClick(View v){
        String gameId = text.getText().toString();
        socketTask.sendJoin(gameId);
    }
}
