package com.example.bekony; /**
 * Created by firemark on 22.11.14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SocketTask extends AsyncTask<Void, String, Boolean> {
    private Socket socket;
    private final String host = "192.168.1.240";
    private final Integer port = 8069;

    private InputStream in;
    private OutputStream out;
    public String gameId;

    private Activity activity;

    private String playerId;
    static private SocketTask task;
    
    private boolean alreadyRunning = false;

    static public SocketTask getSocketTask() {
        return task;
    }

    public SocketTask(String playerId) {
        this.playerId = playerId;
        task = this;
    }

    public void setActivity(Activity act){
        activity = act;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.i("SocketTask", "connect");
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
            if (socket.isConnected()) {
                in = socket.getInputStream();
                out = socket.getOutputStream();
                BufferedReader bin = new BufferedReader(
                        new InputStreamReader(in, "UTF-8")
                );
                sendConnect();
                String data = "";
                while((data = bin.readLine()) != null){
                    publishProgress(data);
                }
            }
        } catch(IOException e){
            Log.i("IOErr", e.toString());
            return false;
        }
        return true;
    }

    protected void onProgressUpdate(String... values) {
        for(String data: values) {
            try {
                Log.i("SocketTask", data);
                JSONObject obj = new JSONObject(data);
                String status = obj.getString("status");
                String command = obj.getString("command");
                if (status != null && status.equals("OK")){
                    Log.i("SocketTask", obj.toString());
                    if ("JOIN".equals(command))
                        recvJoinGameInfo(obj);
                    else if ("HOST".equals(command))
                        recvHostGameInfo(obj);
                    else if ("SYNC".equals(command))
                        recvSync(obj);
                }
            }catch(Exception e){
                Log.i("Err", e.toString());
            }
        }
    }

    private void sendCommand(String command, JSONObject req) throws IOException, JSONException {
        JSONObject obj  = new JSONObject();
        obj.put("command", command.toUpperCase());
        obj.put("request", req);
        String serialized = obj.toString();
        serialized.concat("\r\n");
        out.write(serialized.getBytes());
    }

    public void sendConnect() {
        Log.i("SocketTask", "CONNECT");
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", playerId);
            sendCommand("connect", obj);
        }catch(Exception e){
            Log.i("Err", e.toString());
        }
    }

    public void sendHost(GameSettings gameSettings, ArrayList<RemoteBeacon> beacons) {
        Log.i("SocketTask", "HOST");
        try {
            JSONObject obj = new JSONObject();
            JSONObject json_settings = new JSONObject();
            JSONArray  json_beacons = new JSONArray();
            json_settings.put("beaconDelay", gameSettings.beaconDelay);
            json_settings.put("goneTime", gameSettings.goneTime);
            json_settings.put("victoryPoints", gameSettings.victoryPoints);
            json_settings.put("pointsPerTrick", gameSettings.pointsPerTrick);
            json_settings.put("tickPeriod", gameSettings.tickPeriod);

            for(RemoteBeacon beacon: beacons){
                JSONObject json_beacon = new JSONObject();
                json_beacon.put("beaconId", beacon.id);
                //json_beacon.put("alias", beacon.alias);
                json_beacons.put(json_beacon);
            }

            obj.put("settings", json_settings);
            obj.put("beacons", json_beacons);

            sendCommand("host", obj);
        }catch(IOException e){
            Log.i("Err", e.toString());
        }catch(JSONException e){
            Log.i("Err", e.toString());
        }
    }

    public void sendJoin(String gameId) {
        Log.i("SocketTask", "JOIN");
        try {
            JSONObject obj = new JSONObject();
            obj.put("gameId", gameId);
            sendCommand("join", obj);
        }catch(Exception e){
            Log.i("Err", e.toString());
        }
    }

    public void recvJoinGameInfo(JSONObject obj) {
        Log.i("JOIN INFO", obj.toString());
        Intent itent = new Intent(activity, GameActivity.class);
        try {
            gameId = obj.getJSONObject("response").getString("gameId");
            //TODO set settings
        }catch(JSONException e){};
        if(!GameState.GAME_RUNNING) {
        	activity.startActivity(itent);
        }
        else {
        	GameState.resetGameStartTime();
        }
    }

    public void recvHostGameInfo(JSONObject obj) {
        Log.i("HOST INFO", obj.toString());
        Intent itent = new Intent(activity, GameActivity.class);
        try {
            gameId = obj.getJSONObject("response").getString("gameId");
            //TODO set settings
        }catch(JSONException e){};
        if(!GameState.GAME_RUNNING) {
        	activity.startActivity(itent);
        }
        else {
        	GameState.resetGameStartTime();
        }
    }

    public void recvSync(JSONObject obj) {
        Log.i("SYNC", obj.toString());
        ArrayList<RemoteBeacon> beacons = new ArrayList<RemoteBeacon>();
        try {
            JSONArray json_beacons = obj.getJSONObject("response").getJSONArray("beacons");
            for(int i=0; i < json_beacons.length(); i++){
                JSONObject json_beacon = json_beacons.getJSONObject(i);
                RemoteBeacon beacon = new RemoteBeacon();
                beacon.id = json_beacon.getString("beaconId"); //mac
                beacon.state = json_beacon.getString("captured"); //'capture' / 'inCapture'
                beacon.owner = json_beacon.getString("owner"); //player id
                beacon.currentCapturingTime = json_beacon.getInt("currentCapturingTime"); //5
                beacon.movementLockTime = json_beacon.getInt("movementLockTime"); //N/A
                beacons.add(beacon);
            }
        }catch(JSONException e){};
        //((GameActivity)activity).beacons = beacons;
        ServerInterface.beacons.clear();
        ServerInterface.beacons.addAll(beacons);
    }

    public void sendCaptured(RemoteBeacon beacon) {
        Log.i("SocketTask", "SEND CAPTURED");
        try {
            JSONObject obj = new JSONObject();
            obj.put("beaconId", beacon.id);
            sendCommand("captured", obj);
        }catch(Exception e){
            Log.i("Err", e.toString());
        }
    }

    public void sendCapture(RemoteBeacon beacon) {
        Log.i("SocketTask", "SEND CAPTURE");
        try {
            JSONObject obj = new JSONObject();
            obj.put("beaconId", beacon.id);
            sendCommand("capture", obj);
        }catch(Exception e){
            Log.i("Err", e.toString());
        }
    }
}
