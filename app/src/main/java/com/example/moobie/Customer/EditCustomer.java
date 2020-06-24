package com.example.moobie.Customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditCustomer extends AppCompatActivity {
    SessionManager sessionManager;
    String getServer, getItemnumber;
    EditText txtVal1,txtVal2, txtVal3, txtVal4;
    Button btnSimpan;
    private Context context = EditCustomer.this;
    RelativeLayout layoutloading, layBack, layHapus;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcustomer);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getServer = user.get(sessionManager.SERVER);
        txtVal1 = findViewById(R.id.txtVal1);
        txtVal2 = findViewById(R.id.txtVal2);
        txtVal3 = findViewById(R.id.txtVal3);

        btnSimpan = findViewById(R.id.btnSimpan);
        layoutloading = findViewById(R.id.layoutLoading);
        imgBack = findViewById(R.id.imgBack);
        layBack = findViewById(R.id.layBack);
        layHapus = findViewById(R.id.layHapus);
        Bundle bundle = getIntent().getExtras();
        getItemnumber = bundle.getString("itemnumber");
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if     (txtVal1.getText().toString().equals("") ||
                        txtVal2.getText().toString().equals("") ||
                        txtVal3.getText().toString().equals("")){
                    Toast.makeText(context, "Data tidak boleh kosong ...", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    simpandata(getItemnumber);
                }

            }
        });


        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(context, CustomerActivity.class);
                startActivity(intent);
            }
        });

        layHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                alertDialogBuilder.setMessage("Apakah anda yakin menghapus data ini ?");
                alertDialogBuilder.setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                hapusData(getItemnumber);
                            }
                        });
                alertDialogBuilder.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        getItemDetail();


    }


    private void getItemDetail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    txtVal1.setText(object.getString("a"));
                                    txtVal2.setText(object.getString("b"));
                                    txtVal3.setText(object.getString("c"));

                                }
                            }

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
                params.put("act", "get_detailcustomer");
                params.put("id_data", getItemnumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    private void simpandata (final String numberitem) {
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
                        Intent i = new Intent(context, CustomerActivity.class);
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
                params.put("data_nama", txtVal1.getText().toString());
                params.put("data_phone", txtVal2.getText().toString());
                params.put("data_alamat", txtVal3.getText().toString());
                params.put("id_data", numberitem);
                params.put("act", "edit_customer");
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void hapusData (final String numberitem2) {
        layoutloading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("Success");
                    String msgerror = jsonObject.getString("message");
                    if (success.equals("1")){
                        layoutloading.setVisibility(View.GONE);
                        finish();
                        Intent intent = new Intent(context, CustomerActivity.class);
                        startActivity(intent);
                    }else{
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(context, msgerror, Toast.LENGTH_SHORT).show();

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "act_hapuscustomer");
                params.put("id_data", numberitem2);
                return  params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }



}
