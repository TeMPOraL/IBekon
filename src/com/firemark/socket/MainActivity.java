package com.example.firemark.socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnJoinClick(View v){
        startActivity(new Intent(MainActivity.this, JoinActivity.class));
    }

    public void btnHostClick(View v){
        startActivity(new Intent(MainActivity.this, HostActivity.class));
    }
}
