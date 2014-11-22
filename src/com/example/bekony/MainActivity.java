package com.example.bekony;

import com.kontakt.sdk.android.configuration.ForceScanConfiguration;
import com.kontakt.sdk.android.configuration.MonitorPeriod;
import com.kontakt.sdk.android.connection.OnServiceBoundListener;
import com.kontakt.sdk.android.device.Region;
import com.kontakt.sdk.android.manager.BeaconManager;
import com.kontakt.sdk.android.device.Beacon;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
    private static final int REQUEST_CODE_ENABLE_BLUETOOTH = 1;

    private BeaconManager beaconManager;
    
    private GameBeaconManager gameBeaconManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //GAME INITIALIZATION STARTS HERE
        //I know I should abstract it out to a function, but I'm too sleepy.
        gameBeaconManager = new GameBeaconManager();
                
        GameState.CURRENT_PLAYER = new Player("Maka Paka");
        GameState.GAME_START_TIME_MSEC = android.os.SystemClock.elapsedRealtime();
        GameState.GAME_END_TIME_MSEC = GameState.GAME_START_TIME_MSEC + GameState.GAME_DURATION_MSEC;
        GameState.GAME_RUNNING = true;
        
        //GAME INITIALIZATION ENDS HERE
        
        //UI INIT
        TextView playerName = (TextView)findViewById(R.id.playerName);
        final TextView playerScore = (TextView)findViewById(R.id.score);
        final TextView playerScoreGain = (TextView)findViewById(R.id.scoreGain);
        final TextView countdown = (TextView)findViewById(R.id.timeLeft);
        
        playerName.setText(GameState.CURRENT_PLAYER.getId());
        playerScore.setText(Integer.toString(GameState.CURRENT_PLAYER.getScore()));
        playerScoreGain.setText("+9000/s");
        
        //UI update
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
        	@Override
        	public void run() {
        		runOnUiThread(new Runnable() {
        			@Override
        			public void run() {
        				if(GameState.CURRENT_PLAYER.timeToIncrementScore()) {
        					GameState.CURRENT_PLAYER.incrementScore(gameBeaconManager.computeTotalScoreGain());
        					playerScore.setText(Integer.toString(GameState.CURRENT_PLAYER.getScore()));
        				}
    					countdown.setText(TimeUtils.countdownTimeStringFromCurrentAndEndTimes(GameState.GAME_END_TIME_MSEC, android.os.SystemClock.elapsedRealtime()));
        			}
        		});
        		if(GameState.GAME_RUNNING) {
        			h.postDelayed(this, GameState.DELAY_BETWEEN_GAME_TICKS);
        		}
        	}
        }
        
        , GameState.DELAY_BETWEEN_GAME_TICKS);
                
        final GameBeaconAdapter adapter = new GameBeaconAdapter(this, new ArrayList<GameBeacon>());
        ListView l = (ListView)findViewById(R.id.beaconView);
        l.setAdapter(adapter);
        

        beaconManager = BeaconManager.newInstance(this);
        beaconManager.setMonitorPeriod(new MonitorPeriod(20*60*1000, 5000)); //EVEN DIRTIER BATTERY DRAINING HACK!!!
        beaconManager.setForceScanConfiguration(ForceScanConfiguration.DEFAULT); //DIRTY DIRTY BATTERY DRAINING HACK
        beaconManager.registerMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onMonitorStart() {
            	System.out.println("onMonitorStart");
            }

            @Override
            public void onMonitorStop() {
            	System.out.println("onMonitorStop");
            }

            @Override
            public void onBeaconsUpdated(final Region region, final List<Beacon> beacons) {
            	gameBeaconManager.onBeaconsUpdated(beacons);
            	runOnUiThread(new Runnable() {
            		@Override
            		public void run() {
            			adapter.clear();
            			adapter.addAll(gameBeaconManager.getBeacons());
            			int gain = gameBeaconManager.computeTotalScoreGain();
            			playerScoreGain.setText(((gain >= 0) ? "+" : "-") + Integer.toString(Math.abs(gain)));
            		}
            	});
            	System.out.println("onBeaconsUpdated");
            }

            @Override
            public void onBeaconAppeared(final Region region, final Beacon beacon) {
            	gameBeaconManager.onBeaconAppeared(beacon);
            }

            @Override
            public void onRegionEntered(final Region region) {
            	System.out.println("onRegionEntered");
            }

            @Override
            public void onRegionAbandoned(final Region region) {
            	System.out.println("onRegionAbandoned");
            }
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        if(!beaconManager.isBluetoothEnabled()) {
            final Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_CODE_ENABLE_BLUETOOTH);
        } else {
            connect();
        }
    }
    

    @Override
    protected void onStop() {
        super.onStop();
        beaconManager.stopMonitoring();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
        beaconManager = null;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_ENABLE_BLUETOOTH) {
            if(resultCode == Activity.RESULT_OK) {
                connect();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
                getActionBar().setSubtitle("Bluetooth not enabled");
            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connect() {
        try {
            beaconManager.connect(new OnServiceBoundListener() {
                @Override
                public void onServiceBound() {
                    try {
                        beaconManager.startMonitoring(Region.EVERYWHERE);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
