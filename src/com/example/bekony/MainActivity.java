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
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
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
        
        gameBeaconManager = new GameBeaconManager();
        gameBeaconManager.init((TextView)findViewById(R.id.hello));
        System.out.println("YO!");
        
        List<String> items = new ArrayList<String>();
        items.add("Maka");
        items.add("Paka");
        
        final GameBeaconAdapter adapter = new GameBeaconAdapter(this, new ArrayList<GameBeacon>());
        ListView l = (ListView)findViewById(R.id.beaconView);
        l.setAdapter(adapter);
        
        beaconManager = BeaconManager.newInstance(this);
        //beaconManager.setMonitorPeriod(MonitorPeriod.MINIMAL);
        beaconManager.setMonitorPeriod(new MonitorPeriod(20*60*1000, 5000));
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
