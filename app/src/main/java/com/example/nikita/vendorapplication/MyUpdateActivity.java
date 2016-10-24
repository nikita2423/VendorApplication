package com.example.nikita.vendorapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikita on 17-09-2016.
 */
public class MyUpdateActivity extends AppCompatActivity {
    private EditText name, vehicle, phone;
    Button update;
     String emi1;
    private String str_emi;
    private String str_name;
    private String str_vehicle;
    private RequestQueue requestQueue1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalinfo);
        name = (EditText)findViewById(R.id.name);
        vehicle = (EditText)findViewById(R.id.vehicle);
        update = (Button)findViewById(R.id.buttonDone);
        requestQueue1 = Volley.newRequestQueue(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPhonePermission();
        }
        TelephonyManager TM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // IMEI No.
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                emi1 = TM.getDeviceId();
            }
        }
        else{
            emi1 = TM.getDeviceId();
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_details();
            }
        });

    }
    private void update_details(){
       str_name = name.getText().toString().trim();
        str_vehicle = vehicle.getText().toString().trim();
        str_emi = emi1;
        final ProgressDialog loading = ProgressDialog.show(this, "Updating", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Toast.makeText(MyUpdateActivity.this, response, Toast.LENGTH_LONG).show();
                        Toast.makeText(MyUpdateActivity.this,String.valueOf(response.equalsIgnoreCase("success")),Toast.LENGTH_LONG).show();
                        if (response.equalsIgnoreCase("success")) {

                            Toast.makeText(MyUpdateActivity.this,"updated successfully",Toast.LENGTH_LONG).show();
                            Intent intent1 = new Intent(MyUpdateActivity.this,MyMap1.class);
                            startActivity(intent1);
                            finish();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(MyUpdateActivity.this, "Error", Toast.LENGTH_LONG).show();
                        //Toast.makeText(MyVendorMainActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                //params.put(Config.KEY_USERNAME, username);
                //params.put(Config.KEY_PASSWORD, password);
                params.put(Config.KEY_EMI, str_emi);
                params.put(Config.KEY_NAME, str_name);
                params.put(Config.KEY_VEHICLE, str_vehicle);
                return params;
            }
        };

        //Adding request the the queue
        requestQueue1.add(stringRequest);
    }


    public static final int MY_PERMISSIONS_REQUEST_PHONE = 99;
    public boolean checkPhonePermission() {
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
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_PHONE);
            }
            return false;
        } else {
            return true;
        }
    }
}
