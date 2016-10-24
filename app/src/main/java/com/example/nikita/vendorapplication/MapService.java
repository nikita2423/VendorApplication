package com.example.nikita.vendorapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Nikita on 12-09-2016.
 */
public class MapService extends Service {
    boolean flag = true;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        while(flag){
            Toast.makeText(this,"time",Toast.LENGTH_LONG).show();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
