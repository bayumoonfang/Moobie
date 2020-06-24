package com.example.moobie.Jualan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

public class CaraBayarActivity extends AppCompatActivity {

    SessionManager sessionManager;
    private Context context = CaraBayarActivity.this;
    String getServer, getOrdernumber, getCustno;
    RelativeLayout layBack,
            caraCash,
            caraCard,
            caraTransfer;
    Button btnBayar;
    EditText txtDibayar;
    TextView valCust;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carabayar);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        Bundle bundle = getIntent().getExtras();
        getServer = bundle.getString("server");
        caraCash = findViewById(R.id.layOut2);
        caraCard = findViewById(R.id.layOut3);
        caraTransfer = findViewById(R.id.layOut4);
        layBack = findViewById(R.id.layBack);

        caraCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cara_bayar("100");
            }
        });

        caraCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cara_bayar("200");
            }
        });


        caraTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cara_bayar("300");
            }
        });

        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PembayaranActivity.class);
                i.putExtra("server",getServer);
                startActivity(i);
            }
        });

    }


    private void cara_bayar (final String carabayar) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("Success");
                            String msgerror = jsonObject.getString("message");
                            if (success.equals("2")) {
                                Toast.makeText(context, msgerror, Toast.LENGTH_SHORT).show();
                                return;
                            }  else {
                                Intent i = new Intent(context, PembayaranActivity.class);
                                i.putExtra("server",getServer);
                                finish();
                                startActivity(i);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "order_pending_carabayar");
                params.put("carabayar", carabayar);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }



}
