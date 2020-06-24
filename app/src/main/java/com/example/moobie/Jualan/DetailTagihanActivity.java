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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;

public class DetailTagihanActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private Context context = DetailTagihanActivity.this;
    String getServer, getOrdernumber;
    RelativeLayout layBack, layDibayarkan;
    Button btnBayar;
    EditText txtDibayar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailtagihan);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        Bundle bundle = getIntent().getExtras();
        getServer = bundle.getString("server");
        layBack = findViewById(R.id.layBack);
        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PembayaranActivity.class);
                i.putExtra("server",getServer);
                finish();
                startActivity(i);
            }
        });


        getTotal();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(context, PembayaranActivity.class);
        i.putExtra("server",getServer);
        finish();
        startActivity(i);
    }

    private void getTotal() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                            symbols.setGroupingSeparator(',');
                            symbols.setDecimalSeparator(',');
                            DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);

                            TextView txt1 = (TextView) findViewById(R.id.valSubTotal);
                            TextView txt2 = (TextView) findViewById(R.id.valServiceCharge);
                            TextView txt3 = (TextView) findViewById(R.id.valTax);
                            TextView txt4 = (TextView) findViewById(R.id.valTotal);

                            txt1.setText(decimalFormat.format(jsonObject.getInt("a")));
                            txt2.setText(decimalFormat.format(jsonObject.getInt("b")));
                            txt3.setText(decimalFormat.format(jsonObject.getInt("c")));
                            txt4.setText(decimalFormat.format(jsonObject.getInt("d")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(mContext, "Error Reading"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "getdata_totaljual");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }



}
