package com.example.moobie.Jualan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;

public class EditOrderActivity extends AppCompatActivity {


    SessionManager sessionManager;
    private Context context = EditOrderActivity.this;
    String getServer, getOrdernumber;
    RelativeLayout layBack;
    Button btnTambah, btnKurang, btnHapus;
    Integer v1,hasil1, hasil2;
    EditText txtJumlah1, txtCatatan1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorder);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        Bundle bundle = getIntent().getExtras();
        getServer = user.get(sessionManager.SERVER);
        getOrdernumber = bundle.getString("ordernumber");
        layBack = findViewById(R.id.layBack);
        txtJumlah1 = findViewById(R.id.txtValJumlah);
        txtCatatan1 = findViewById(R.id.txtCatatan);
        btnTambah = findViewById(R.id.btnTambah);
        btnKurang = findViewById(R.id.btnKurang);
        btnHapus = findViewById(R.id.btnHapus);
        layBack = findViewById(R.id.layBack);



        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int v1 = Integer.parseInt(txtJumlah1.getText().toString());
                int hasil1 = v1 + 1;
                txtJumlah1.setText(hasil1+"");
            }
        });

        btnKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int v2 = Integer.parseInt(txtJumlah1.getText().toString());
                int hasil2 = v2 - 1;

                if (hasil2 <= 0) {
                    new StyleableToast
                            .Builder(context)
                            .text("Jumlah tidak boleh kurang dari 1")
                            .textColor(getResources().getColor(R.color.colorWhite))
                            .backgroundColor(getResources().getColor(R.color.colorBlack))
                            .length(Toast.LENGTH_LONG)
                            .show();

                } else {
                    txtJumlah1.setText(hasil2+"");
                }

            }
        });


        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            simpan_data();
            }
        });


        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                alertDialogBuilder.setMessage("Apakah anda yakin menghapus item ini ?");
                alertDialogBuilder.setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                hapusItemOrder();
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


        getDetail();

    }




    @Override
    public void onBackPressed() {
        simpan_data();
    }

    private void getDetail() {
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

                            TextView txtSubtotal = (TextView) findViewById(R.id.txtHarga);
                            TextView txtNamaMenu = (TextView) findViewById(R.id.txtNamaMenu);
                            EditText txtJumlah = (EditText) findViewById(R.id.txtValJumlah);
                            EditText txtCatatan = (EditText) findViewById(R.id.txtCatatan);

                           txtSubtotal.setText("Rp."+decimalFormat.format(jsonObject.getInt("b")));
                            txtNamaMenu.setText(jsonObject.getString("a"));
                            txtJumlah.setText(jsonObject.getString("d"));
                            txtCatatan.setText(jsonObject.getString("c"));


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
                params.put("id", getOrdernumber);
                params.put("act", "getdata_detailorderedit");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    private void simpan_data() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                           if(jsonObject.getString("success").equals("1")) {
                               finish();
                               Intent intent = new Intent(context, CheckoutActivity.class);
                               startActivity(intent);
                           } else {
                               Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("id", getOrdernumber);
                params.put("catatan", txtCatatan1.getText().toString());
                params.put("jumlah", txtJumlah1.getText().toString());
                params.put("act", "simpan_editorder");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    private void hapusItemOrder() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("success").equals("1")) {
                                finish();
                                Intent intent = new Intent(context, CheckoutActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("id", getOrdernumber);
                params.put("act", "hapus_editorderitem");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }



}
