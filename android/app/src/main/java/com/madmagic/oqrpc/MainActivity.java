package com.madmagic.oqrpc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static  boolean b;
    public static TextView txtRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = true;

        if(!hasUsageStatsPermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

        Intent service = new Intent(MainActivity.this, MainService.class);
        if (!MainService.isRunning) {
            startService(service);
        }

        txtRunning = findViewById(R.id.txtRunning);
        txtRunning.setText(R.string.notRunning);

        Button b = findViewById(R.id.btnTerminate);
        b.setOnClickListener(v -> {
            if (MainService.isRunning) {
                stopService(service);
                ConnectionChecker.end();
            }
        });

        Button start = findViewById(R.id.btnStart);
        start.setOnClickListener(v -> {
            if (!MainService.isRunning) {
                startService(service);
            }
        });
    }

    @Override
    protected void onDestroy() {
        b = false;
        super.onDestroy();
    }

    boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }
}
