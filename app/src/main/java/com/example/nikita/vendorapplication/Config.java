package com.example.nikita.vendorapplication;

/**
 * Created by Nikita on 09-09-2016.
 */
public class Config {
    //URLs to register.php and confirm.php file
    public static final String REGISTER_URL = "http://myvendor.pe.hu/AndroidOTP/vendor_register.php";
   // public static final String CONFIRM_URL = "http://myvendor.pe.hu/AndroidOTP/confirm.php";
   public static final String UPDATE_URL = "http://myvendor.pe.hu/AndroidOTP/update_register.php";
    //Keys to send username, password, phone and otp
    public static final String KEY_EMI = "emi";
    public static final String KEY_NAME = "name";
    public static final String KEY_VEHICLE = "vehicle";
    public static final String KEY_TIME = "time";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_STRLATITUDE = "str_latitude";
    public static final String KEY_STRLONGITUDE = "str_longitude";
    public static final String KEY_ENDLATITUDE = "end_latitude";
    public static final String KEY_ENDLONGITUDE = "end_longitude";
    public static final String KEY_STATUS = "status";
    public static final String KEY_CUSTOMER = "customer_id";
    public static final String KEY_DATE = "my_date";
    public static final String KEY_CURRENT = "current_time";
   // public static final String KEY_OTP = "otp"
   public static final String DATA_URL = "http://myvendor.pe.hu/AndroidOTP/confirm_emi.php?emi=";
    //public static final String KEY_NAME = "name";
    //public static final String KEY_ADDRESS = "address";
    //public static final String KEY_VC = "vc";
    public static final String JSON_ARRAY = "result";
    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "Success";
    public static final String CREDENTIALS_URL = "http://myvendor.pe.hu/AndroidOTP/update_credentials.php";
    public static final String DISTANCE_URL = "http://myvendor.pe.hu/AndroidOTP/update_distance.php";
    public static final String STR_URL = "http://myvendor.pe.hu/AndroidOTP/update_str.php";
    public static final String END_URL = "http://myvendor.pe.hu/AndroidOTP/update_end.php";
    public static final String STATUS_URL = "http://myvendor.pe.hu/AndroidOTP/update_status.php";
 public static final String STATUS1_URL = "http://myvendor.pe.hu/AndroidOTP/check_volley.php";
 public static final String FETCHCV_URL = "http://myvendor.pe.hu/AndroidOTP/fetch_cv.php?emi=";
 public static final String TIMETABLE_URL = "http://myvendor.pe.hu/AndroidOTP/register_timetable.php";
}
