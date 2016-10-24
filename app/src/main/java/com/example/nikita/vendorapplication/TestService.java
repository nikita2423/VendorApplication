package com.example.nikita.vendorapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikita on 02-10-2016.
 */
public class TestService extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {
    private static final String TAG = "LocationActivity";
    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation1, mCurrentLocation;
    Marker mCurrLocationMarker, mCurrLocationMarker1;
    LocationRequest mLocationRequest;
    TextView time_tv, latitude_tv, longitude_tv;
    BroadcastReceiver receiver;
    Intent serviceIntent;

    double mLatitude = 0;
    double mLongitude = 0;
    double mLatitude1 = 0;
    double mLongitude1 = 0;
    double str_latitude1 = 0;
    double str_longitude1 =0;
    String myLatitude, myLongitude;
    boolean flag = false;
    Button start,stop;
    RelativeLayout rl;
    TextView time,distance;
    TextView update_details;
    int i=0;
    double j=0, k=0;
    boolean flag1 = true;
    //String distance;
    String update_distance;
    String status;
    String customer_id1,vehicle1;
    String latitude, longitude, mytime;
    private RequestQueue requestQueue1, requestQueue2, requestQueue3, requestQueue4, requestQueue5, requestQueue6, requestQueue7;
    String emi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        setContentView(R.layout.routemap1);
        rl = (RelativeLayout)findViewById(R.id.belowlayout);
        start = (Button)rl.findViewById(R.id.start_button);
        stop = (Button)rl.findViewById(R.id.stop_button);
        time = (TextView)rl.findViewById(R.id.mytime);
        distance = (TextView)rl.findViewById(R.id.mydistance);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        MarkerPoints = new ArrayList<>();
        requestQueue1 = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);
        requestQueue3 = Volley.newRequestQueue(this);
        requestQueue4 = Volley.newRequestQueue(this);
        requestQueue5 = Volley.newRequestQueue(this);
        requestQueue6 = Volley.newRequestQueue(this);
        requestQueue7 = Volley.newRequestQueue(this);
        TelephonyManager TM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                emi = TM.getDeviceId();
            }
        }
        else{
            emi = TM.getDeviceId();
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String mytime = intent.getStringExtra("counter");

                String mydistance = intent.getStringExtra("distance");
              String myLatitude1 = intent.getStringExtra("Latitude");
               String myLongitude1 = intent.getStringExtra("Longitude");
                myLatitude = myLatitude1;
                myLongitude = myLongitude1;
                if(MarkerPoints.size()>2)
                {
                    MarkerPoints.clear();
                }
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, Config.END_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                //If it is success
                                if (response.equalsIgnoreCase("Success")) {

                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(TestService.this, "Error", Toast.LENGTH_LONG).show();
                                //Toast.makeText(MyVendorMainActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        //Adding the parameters to the request
                        params.put(Config.KEY_ENDLATITUDE,myLatitude);
                        params.put(Config.KEY_ENDLONGITUDE,myLongitude);
                        params.put(Config.KEY_EMI, emi);

                        return params;
                    }
                };

                //Adding request the the queue
                requestQueue3.add(stringRequest2);

                time.setText(mytime);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CREDENTIALS_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.equalsIgnoreCase("Success")) {
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(TestService.this, "Error", Toast.LENGTH_LONG).show();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(Config.KEY_TIME, time.getText().toString());
                        params.put(Config.KEY_EMI, emi);

                        return params;
                    }
                };

                //Adding request the the queue
                requestQueue1.add(stringRequest);
                distance.setText(mydistance);
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.DISTANCE_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(TestService.this,"my" + response,Toast.LENGTH_LONG).show();
                                if (response.equalsIgnoreCase("Success")) {
                                    Toast.makeText(TestService.this,"distance updated successfully",Toast.LENGTH_LONG).show();

                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(TestService.this, "Error", Toast.LENGTH_LONG).show();
                                //Toast.makeText(MyVendorMainActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        //Adding the parameters to the request
                        params.put(Config.KEY_DISTANCE, distance.getText().toString());
                        params.put(Config.KEY_EMI, emi);

                        return params;
                    }
                };

                //Adding request the the queue
                requestQueue2.add(stringRequest1);

            }

        };


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /*@Override
    protected void onResume() {
        super.onResume();

        serviceIntent = new Intent(getApplicationContext(),
                UpdaterService.class);
        startService(serviceIntent);

        registerReceiver(receiver, new IntentFilter(
                UpdaterService.BROADCAST_ACTION));

    }

    @Override
    protected void onPause() {
        super.onPause();

        stopService(serviceIntent);
        unregisterReceiver(receiver);

    }*/

    @Override
    public void onClick(View v) {
        if(v==start)
        {
            status = new String("running");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.STATUS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(MyMap1.this,"My" + response,Toast.LENGTH_LONG).show();
                            if (response.equalsIgnoreCase("Success")) {
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TestService.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(Config.KEY_STATUS,status);
                    params.put(Config.KEY_EMI, emi);

                    return params;
                }
            };

            //Adding request the the queue
            requestQueue4.add(stringRequest);
            serviceIntent = new Intent(getApplicationContext(),
                    UpdateService2.class);
            flag=true;
            //Intent serviceIntent = new Intent(this,bgservice.class);
            //serviceIntent.putExtra("str_latitude", String.valueOf(str_latitude1));
            //serviceIntent.putExtra("")
            this.startService(serviceIntent);
            startService(serviceIntent);

            registerReceiver(receiver, new IntentFilter(
                    UpdateService2.BROADCAST_ACTION));
        }
        else if(v==stop)
        {
            status = new String("stopped");
            Toast.makeText(TestService.this,status,Toast.LENGTH_LONG).show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.STATUS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Toast.makeText(MyMap1.this,"end" + response,Toast.LENGTH_LONG).show();

                            if (response.equalsIgnoreCase("Success")) {
                                //Toast.makeText(MyMap1.this,"status" + status,Toast.LENGTH_LONG).show();

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(TestService.this, "Error", Toast.LENGTH_LONG).show();
                            //Toast.makeText(MyVendorMainActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(Config.KEY_EMI, emi);
                    params.put(Config.KEY_STATUS,status);
                    return params;
                }
            };

            //Adding request the the queue
            requestQueue5.add(stringRequest);
            serviceIntent = new Intent(getApplicationContext(),
                    UpdateService2.class);
            stopService(serviceIntent);
            unregisterReceiver(receiver);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
      /*  if (MarkerPoints.size() > 1) {
            MarkerPoints.clear();
            mMap.clear();
        }*/
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        //startLocationUpdates();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
            if (mCurrLocationMarker1 != null) {
                mCurrLocationMarker1.remove();
            }
            mCurrentLocation1 = location;
            mLatitude1 = mCurrentLocation1.getLatitude();
            mLongitude1 = mCurrentLocation1.getLongitude();
            str_latitude1 = mLatitude1;
            str_longitude1 = mLongitude1;

            LatLng latLng = new LatLng(mLatitude1, mLongitude1);
            //MarkerPoints.add(latLng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");


            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            //animateMarker(mCurrLocationMarker,latLng,false);
            mCurrLocationMarker1 = mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
               /* LatLng latLng = new LatLng(Double.parseDouble(myLatitude), Double.parseDouble(myLongitude));
        MarkerPoints.add(latLng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");


        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        //animateMarker(mCurrLocationMarker,latLng,false);
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));*/
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    public static final int MY_PERMISSIONS_REQUEST_PHONE = 99;
    public boolean checkPhonePermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_PHONE_STATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_PHONE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_PHONE);
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10*1000);
        mLocationRequest.setFastestInterval(5*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(10);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}

