package com.example.moobie.Outlet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moobie.R;
import com.example.moobie.SessionManager;
import com.example.moobie.publicURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AddOutletActivity extends AppCompatActivity {
    SessionManager sessionManager;
    String getServer;
    EditText txtNama,txtAlamat, txtCity, txtPhone, txtCode;
    Button btnSimpan;
    private Context context = AddOutletActivity.this;
    RelativeLayout layoutloading, layBack;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addoutlet);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getServer = user.get(sessionManager.SERVER);
        txtNama = findViewById(R.id.txtNamaOutlet);
        txtAlamat = findViewById(R.id.txtAlamatOutlet);
        txtCity = findViewById(R.id.txtCityOutlet);
        txtPhone = findViewById(R.id.txtPhoneOutlet);
        txtCode = findViewById(R.id.txtCodeOutlet);
        btnSimpan = findViewById(R.id.btnSimpanAddOutlet);
        layoutloading = findViewById(R.id.layoutLoading);
        imgBack = findViewById(R.id.imgBack);
        layBack = findViewById(R.id.layBack);


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if     (txtNama.getText().toString().equals("") ||
                        txtAlamat.getText().toString().equals("") ||
                        txtCity.getText().toString().equals("") ||
                        txtPhone.getText().toString().equals("") ||
                        txtCode.getText().toString().equals("")){
                    Toast.makeText(context, "Data tidak boleh kosong ...", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    simpandata();
                }

            }
        });


        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(context, OutletActivity.class);
                startActivity(intent);
            }
        });


    }




    private void simpandata () {
        layoutloading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("Success");
                    String msgerror = jsonObject.getString("message");

                    if (success.equals("2")) {
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(context, msgerror, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (success.equals("3")) {
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(context, msgerror, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        layoutloading.setVisibility(View.GONE);
                        //Toast.makeText(context, msgerror, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, OutletActivity.class);
                        finish();
                        startActivity(i);
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                    layoutloading.setVisibility(View.GONE);
                    Toast.makeText(context, "Error3 " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(context, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("outlet_nama", txtNama.getText().toString());
                params.put("outlet_alamat", txtAlamat.getText().toString());
                params.put("outlet_city", txtCity.getText().toString());
                params.put("outlet_phone", txtPhone.getText().toString());
                params.put("outlet_code", txtCode.getText().toString());
                params.put("act", "add_outlet");
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




}
