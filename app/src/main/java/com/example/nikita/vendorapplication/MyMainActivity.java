package com.example.nikita.vendorapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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

public class MyMainActivity extends AppCompatActivity {

    //EditText unique_id;
    private String android_id;
    Button done;
    private EditText editTextName;
    private EditText editTextVehicle;
    private RequestQueue requestQueue;
    private String emi;
    private String name;
    private String vehicle;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalinfo);
        done = (Button)findViewById(R.id.buttonDone);
        editTextName = (EditText)findViewById(R.id.name);
        editTextVehicle = (EditText)findViewById(R.id.vehicle);
        //unique_id = (EditText)findViewById(R.id.editText);
        /*android_id = Secure.getString(getContext().getContentResolver(),
                Secure.ANDROID_ID);
        unique_id.setText(android_id);*/
        requestQueue = Volley.newRequestQueue(this);
        TelephonyManager TM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // IMEI No.
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
        //unique_id.setText(imeiNo);
        Toast.makeText(MyMainActivity.this, emi, Toast.LENGTH_LONG).show();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


    }

    private void register() {


            //Getting user data
            //username = editTextUsername.getText().toString().trim();
            //password = editTextPassword.getText().toString().trim();
            //phone = editTextPhone.getText().toString().trim();
            // Toast.makeText(MyVendorMainActivity.this,"Register",Toast.LENGTH_LONG).show();
            //Again creating the string request
            // if(phone != null && !phone.isEmpty() && !phone.equals("null")) {
        name = editTextName.getText().toString().trim();
        vehicle = editTextVehicle.getText().toString().trim();
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            //Toast.makeText(MyVendorMainActivity.this,"Register",Toast.LENGTH_LONG).show();

                                // Toast.makeText(MyVendorMainActivity.this,"My" + response,Toast.LENGTH_LONG).show();
                                //Creating the json object from the response
                                Toast.makeText(MyMainActivity.this,"my" + response,Toast.LENGTH_LONG).show();
                                //JSONObject jsonResponse = new JSONObject(response);
                                //Toast.makeText(MyVendorMainActivity.this, "json" + jsonResponse.toString(), Toast.LENGTH_LONG).show();
                                //If it is success
                                if (response.equalsIgnoreCase("Success")) {
                                    //Toast.makeText(MyVendorMainActivity.this,"json" + jsonResponse.toString(),Toast.LENGTH_LONG).show();
                                    //Asking user to confirm otp
                                    //confirmOtp();
                                    Toast.makeText(MyMainActivity.this,"registered successfully",Toast.LENGTH_LONG).show();
                                    Intent intent1 = new Intent(MyMainActivity.this,TestService.class);
                                    startActivity(intent1);
                                    finish();
                                }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            Toast.makeText(MyMainActivity.this, "Error", Toast.LENGTH_LONG).show();
                            //Toast.makeText(MyVendorMainActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding the parameters to the request
                    //params.put(Config.KEY_USERNAME, username);
                    //params.put(Config.KEY_PASSWORD, password);
                    params.put(Config.KEY_EMI, emi);
                    params.put(Config.KEY_NAME, name);
                    params.put(Config.KEY_VEHICLE, vehicle);
                    return params;
                }
            };

            //Adding request the the queue
            requestQueue.add(stringRequest);
        }
}
