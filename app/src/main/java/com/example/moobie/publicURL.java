package com.example.moobie;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class publicURL extends Activity {
    private Context mContext = publicURL.this;
    public static String URL_LOGIN = "https://duakata-dev.com/moobi/UAT2/m-moobi/login.php";
    public static String GET_TRANSAKSIHOME = "https://duakata-dev.com/moobi/UAT2/m-moobi/GET_TRANSAKSIHOME.php";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    public void errorMessage(){
        Toast.makeText(mContext, "Koneksi Terputus ", Toast.LENGTH_SHORT).show();
    }

}


