package com.example.nikita.vendorapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nikita on 04-10-2016.
 */
public class UpdateService2 extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    float mydistance = 0;
    Intent intent;
    int counter = 0;
    Updater updater;

    @Override
    public void onCreate() {
        super.onCreate();
        updater = new Updater();
        //previousBestLocation.setLatitude(0);
       // previousBestLocation.setLongitude(0);
        intent = new Intent(BROADCAST_ACTION);
    }
    class Updater extends Thread {

        public boolean isRunning = false;
        public long DELAY = 1000;

        int i = 0;

        @Override
        public void run() {
            super.run();

            isRunning = true;
            while (isRunning) {
                Log.d("acd", "Running...");
                // sendbroadcast
                sendResult(i + "");
                i++;
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isRunning = false;
                }
            } // while end
        } // run end

        public boolean isRunning() {
            return this.isRunning;
        }

    } // inner class end
    public void sendResult(String message) {
        intent.putExtra("counter", message);
        //intent.putExtra("distance",String.valueOf(mydistance));
        //intent.putExtra("latitude1",String.valueOf(MarkerPoints.get(0).latitude));
        //intent.putExtra("latitude2",String.valueOf(MarkerPoints.get(1).latitude));
        //intent.putExtra("time", new Date().toLocaleString());
        sendBroadcast(intent);
    }
    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {

        if (!updater.isRunning()) {
            updater.start();
            Log.d("acd", "Started");
            showMSg("Started");
            updater.isRunning = true;

        }

        return super.onStartCommand(intent, flags, startId);
    }


    public void showMSg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart(Intent intent, int startId) {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {

            // A new location is always better than no location
            return true;
        }
        //intent.putExtra("Latitude", String.valueOf(location.getLatitude()) + "  p: " + String.valueOf(currentBestLocation.getLatitude()));
        //intent.putExtra("Longitude", String.valueOf(location.getLongitude()) + " p: " + String.valueOf(currentBestLocation.getLongitude()));
        LatLng str1 = new LatLng(currentBestLocation.getLatitude(),currentBestLocation.getLongitude());
        LatLng des1 = new LatLng(location.getLatitude(),location.getLongitude());
        //mydistance = mydistance + getDistance(str1,des1);
        //intent.putExtra("distance",String.valueOf(mydistance));
        //sendBroadcast(intent);
        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {


            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            //intent.putExtra("Latitude", String.valueOf(currentBestLocation.getLatitude()));
            //intent.putExtra("Longitude", String.valueOf(currentBestLocation.getLongitude()));
           // intent.putExtra("Provider", String.valueOf(currentBestLocation.getProvider()));
            LatLng str = new LatLng(currentBestLocation.getLatitude(),currentBestLocation.getLongitude());
            LatLng des = new LatLng(location.getLatitude(),location.getLongitude());
            mydistance = mydistance + getDistance(str,des);
            intent.putExtra("distance",String.valueOf(mydistance));
            sendBroadcast(intent);
            return true;
        } else if (isNewer && !isLessAccurate) {
            //intent.putExtra("Latitude", String.valueOf(currentBestLocation.getLatitude()));
            //intent.putExtra("Longitude", String.valueOf(currentBestLocation.getLongitude()));
            //intent.putExtra("Provider", String.valueOf(currentBestLocation.getProvider()));
            LatLng str = new LatLng(currentBestLocation.getLatitude(),currentBestLocation.getLongitude());
            LatLng des = new LatLng(location.getLatitude(),location.getLongitude());
            mydistance = mydistance + getDistance(str,des);
            intent.putExtra("distance",String.valueOf(mydistance));
            sendBroadcast(intent);
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            //intent.putExtra("Latitude", String.valueOf(currentBestLocation.getLatitude()));
            //intent.putExtra("Longitude", String.valueOf(currentBestLocation.getLongitude()));
            //intent.putExtra("Provider", String.valueOf(currentBestLocation.getProvider()));
            LatLng str = new LatLng(currentBestLocation.getLatitude(),currentBestLocation.getLongitude());
            LatLng des = new LatLng(location.getLatitude(),location.getLongitude());
            mydistance = mydistance + getDistance(str,des);
            intent.putExtra("distance",String.valueOf(mydistance));
            sendBroadcast(intent);
            return true;
        }
        return false;
    }
    public float getDistance(LatLng my_latlong, LatLng frnd_latlong) {
        Location l1 = new Location("One");
        l1.setLatitude(my_latlong.latitude);
        l1.setLongitude(my_latlong.longitude);

        Location l2 = new Location("Two");
        l2.setLatitude(frnd_latlong.latitude);
        l2.setLongitude(frnd_latlong.longitude);

        float distance = l1.distanceTo(l2);
        //String dist = distance + " M";

        /*if (distance > 1000.0f) {
           float distance1 = distance / 1000.0f;
            dist = distance + " KM";
        }*/
        return distance;
    }


    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (updater.isRunning) {
            updater.interrupt();
            Log.d("acd", "Destroyed");
            showMSg("Destroyed");
            updater.isRunning = false;
            updater = null;
        }
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }




    public class MyLocationListener implements android.location.LocationListener
    {

        public void onLocationChanged(final Location loc)
        {
            Log.i("**************", "Location changed");

            if(isBetterLocation(loc, previousBestLocation)) {
                loc.getLatitude();
                loc.getLongitude();
                previousBestLocation = loc;
                String prevLatitude = String.valueOf(previousBestLocation.getLatitude());
                String prevLongitude = String.valueOf(previousBestLocation.getLongitude());
                Toast.makeText(getApplicationContext(),prevLatitude + " " + prevLongitude,Toast.LENGTH_LONG).show();
                //previousBestLocation = loc;

                intent.putExtra("Latitude", String.valueOf(loc.getLatitude()));
                intent.putExtra("Longitude", String.valueOf(loc.getLongitude()));
                //intent.putExtra("Provider", String.valueOf(loc.getProvider()));
                sendBroadcast(intent);

            }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
}

