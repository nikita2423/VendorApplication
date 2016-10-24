package com.example.nikita.vendorapplication;

/**
 * Created by Nikita on 02-10-2016.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class UpdaterService extends Service{

    Updater updater;
    BroadcastReceiver broadcaster;
    Intent intent;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    double mLatitude = 0;
    double mLongitude = 0;
    static final public String BROADCAST_ACTION = "com.pavan.broadcast";
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 5000;
    private static final float LOCATION_DISTANCE = 10f;
    float mydistance =0;
    int c=0;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updater = new Updater();

        initializeLocationManager();
        MarkerPoints = new ArrayList<>();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        Log.d("acd", "Created");
        showMSg("Created");

        intent = new Intent(BROADCAST_ACTION);

    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {

        if (!updater.isRunning()) {
            updater.start();
            Log.d("acd", "Started");
            showMSg("Started");
            updater.isRunning = true;
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public synchronized void onDestroy() {
        super.onDestroy();

        if (updater.isRunning) {
            updater.interrupt();
            Log.d("acd", "Destroyed");
            showMSg("Destroyed");
            updater.isRunning = false;
            updater = null;
        }

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
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            mLocationListeners[1]);
                } catch (java.lang.SecurityException ex) {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "network provider does not exist, " + ex.getMessage());
                }
                try {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            mLocationListeners[0]);
                } catch (java.lang.SecurityException ex) {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "gps provider does not exist " + ex.getMessage());
                }
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



    public void showMSg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            showMSg(String.valueOf(c));
            mLastLocation.set(location);
            LatLng latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            if(c==0) {
                MarkerPoints.add(0, latLng);
                MarkerPoints.add(1,latLng);
                c++;
            }
            //LatLng point2 = new LatLng(11.723512, 78.466287);
            //MarkerPoints.add(1,point2);
            else if(c==1) {
                MarkerPoints.add(1, latLng);
                c++;
            }
            else{
                MarkerPoints.add(latLng);
            }
            if(MarkerPoints.size()>=3)
            {
                MarkerPoints.add(0,MarkerPoints.get(1));
                MarkerPoints.add(1,MarkerPoints.get(2));
                MarkerPoints.remove(2);
            }
            if(MarkerPoints.size()==2)
            {
                mydistance = mydistance + getDistance(MarkerPoints.get(0),MarkerPoints.get(1));
                //intent.putExtra("distance",mydistance);
            }
            intent.putExtra("distance",String.valueOf(mydistance));
            intent.putExtra("latitude1",String.valueOf(MarkerPoints.get(0).latitude));
            intent.putExtra("latitude2",String.valueOf(MarkerPoints.get(1).latitude));
            intent.putExtra("c", String.valueOf(c));
            sendBroadcast(intent);
            //intent.putExtra("latitude", String.valueOf(mLastLocation.getLatitude()));
            //intent.putExtra("longitude", String.valueOf(mLastLocation.getLongitude()));
            //sendBroadcast(intent);


        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
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

} // outer class end


