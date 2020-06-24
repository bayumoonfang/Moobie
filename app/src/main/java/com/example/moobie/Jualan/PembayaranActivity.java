package com.example.moobie.Jualan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PembayaranActivity extends AppCompatActivity {

    SessionManager sessionManager;
    private Context context = PembayaranActivity.this;
    String getServer, stringpas, getCustno, getValTotal, getValDibayar;
    RelativeLayout layBack, layDibayarkan, layDetailtagihan, layCustomer, layCarabayar, btnBayar, rl,layBank;
    EditText txtDibayar, ValBayar, valCatatan, valBankNumb;
    TextView valCust, valBank, valTotal2;
    Integer v1 = 0;
    Integer v2 = 0;
    TextView btnBayar2, txtTotal3a;
    Button btnPas, btnSimpanPesanan, btnSimpan2;
Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        Bundle bundle = getIntent().getExtras();
        getServer = bundle.getString("server");
        getCustno = bundle.getString("custno");
        txtDibayar = findViewById(R.id.txtDibayarkan);
        layDibayarkan = findViewById(R.id.layOut6);
        layDetailtagihan =findViewById(R.id.layOut7);
        layCustomer =findViewById(R.id.layOut3);
        layCarabayar =findViewById(R.id.layOut5);
        btnBayar = findViewById(R.id.btnBayar);
        btnBayar2 = findViewById(R.id.btnBayar2);
        btnSimpan2 = findViewById(R.id.btnSimpan2);
        valTotal2 = findViewById(R.id.txtee2);
        rl = findViewById(R.id.layOut1);
        ValBayar = findViewById(R.id.valTotalnya);
        layBack = findViewById(R.id.layBack);
        valCatatan = findViewById(R.id.txtCatatan);
        valCust = findViewById(R.id.txtValCustomer);
        btnPas = findViewById(R.id.btnPas);
        btnSimpanPesanan = findViewById(R.id.btnSimpanPesanan);
layBank = findViewById(R.id.layBank);
        spinner =findViewById(R.id.txtValBank);
        valBank = findViewById(R.id.txtBank);
        valBankNumb = findViewById(R.id.txtNoBank);
        txtTotal3a = findViewById(R.id.txtValCaraBayar);
        txtDibayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDibayar.setText("");
            }
        });
        layDibayarkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDibayar.setText("");
            }
        });

        layDetailtagihan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailTagihanActivity.class);
                i.putExtra("server",getServer);
                startActivity(i);
            }
        });


        layCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CustomerOrderActivity.class);
                i.putExtra("server",getServer);
                startActivity(i);
            }
        });

        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CheckoutActivity.class);
                i.putExtra("server",getServer);
                finish();
                startActivity(i);
            }
        });


        layCarabayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CaraBayarActivity.class);
                i.putExtra("server",getServer);
                startActivity(i);
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinner2Val = parent.getSelectedItem().toString();
                valBank.setText(spinner2Val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                valBank.setText("");
            }
        });



        btnSimpan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           if(txtTotal3a.getText().toString().equals("200") || txtTotal3a.getText().toString().equals("300")) {
               if (valBank.getText().toString().equals(""))  {
                   new StyleableToast
                           .Builder(context)
                           .text("Text must be filled !")
                           .textColor(getResources().getColor(R.color.colorWhite))
                           .backgroundColor(getResources().getColor(R.color.colorBlack))
                           .length(Toast.LENGTH_LONG)
                           .show();
                   return;
               }

           }

                if (txtDibayar.getText().toString().equals("")) {

               new StyleableToast
                       .Builder(context)
                       .text("Text must be filled !")
                       .textColor(getResources().getColor(R.color.colorWhite))
                       .backgroundColor(getResources().getColor(R.color.colorBlack))
                       .length(Toast.LENGTH_LONG)
                       .show();
               return;
           } else {
                    v1 = Integer.parseInt(txtDibayar.getText().toString());
                    v2 = Integer.parseInt(stringpas);
                    if ( v1 < v2) {
                        new StyleableToast
                                .Builder(context)
                                .text("Invalid Amount !")
                                .textColor(getResources().getColor(R.color.colorWhite))
                                .backgroundColor(getResources().getColor(R.color.colorBlack))
                                .length(Toast.LENGTH_LONG)
                                .show();
                    } else {
                        paymentpesanan();
                    }
           }

            }
        });

        btnPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentpesanan_pas();
            }
        });


        btnSimpanPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                alertDialogBuilder.setMessage("Apakah anda yakin menyimpan pesanan ini ?");
                alertDialogBuilder.setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                simpanpesanan();
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

        getTotal();
        getCustomer();
        getdata_bank();

    }



    private void getdata_bank() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<String> categories2 = new ArrayList<String>();
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                categories2.add("Pilih Bank");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    categories2.add(object.getString("a"));
                                }
                            }

                            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories2);
                            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter2);
                            //spinner.setAdapter(new ArrayAdapter<String>(ProdukActivity.this, android.R.layout.simple_spinner_dropdown_item, category));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error Reading "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "getdata_bank");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    private void simpanpesanan() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent i = new Intent(context, JualanActivity.class);
                            finish();
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new StyleableToast
                                    .Builder(context)
                                    .text(e.toString())
                                    .textColor(getResources().getColor(R.color.colorWhite))
                                    .backgroundColor(getResources().getColor(R.color.colorBlack))
                                    .length(Toast.LENGTH_LONG)
                                    .show();

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
                params.put("act", "simpan_simpanpesanan");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    private void paymentpesanan_pas() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String ordernumb = jsonObject.getString("ordernumb");
                            Intent i = new Intent(context, CetakStrukActivity.class);
                            finish();
                            i.putExtra("dibayar",stringpas);
                            i.putExtra("ordernumb", ordernumb);
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new StyleableToast
                                    .Builder(context)
                                    .text(e.toString())
                                    .textColor(getResources().getColor(R.color.colorWhite))
                                    .backgroundColor(getResources().getColor(R.color.colorBlack))
                                    .length(Toast.LENGTH_LONG)
                                    .show();

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
                params.put("act", "simpan_paymentpesanan");
                params.put("uangpas", stringpas);
                params.put("bank", valBank.getText().toString());
                params.put("banknumb", valBankNumb.getText().toString());
                params.put("catatan", valCatatan.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    private void paymentpesanan() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String ordernumb = jsonObject.getString("ordernumb");
                            Intent i = new Intent(context, CetakStrukActivity.class);
                            finish();
                            i.putExtra("dibayar",txtDibayar.getText().toString());
                            i.putExtra("ordernumb", ordernumb);
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new StyleableToast
                                    .Builder(context)
                                    .text(e.toString())
                                    .textColor(getResources().getColor(R.color.colorWhite))
                                    .backgroundColor(getResources().getColor(R.color.colorBlack))
                                    .length(Toast.LENGTH_LONG)
                                    .show();
                            
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
                params.put("act", "simpan_paymentpesanan");
                params.put("bank", valBank.getText().toString());
                params.put("banknumb", valBankNumb.getText().toString());
                params.put("catatan", valCatatan.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

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

                            DecimalFormatSymbols symbols2 = new DecimalFormatSymbols();
                            DecimalFormat decimalFormat2 = new DecimalFormat("####");


                            TextView txtTotal = (TextView) findViewById(R.id.txtTotal);
                            TextView txtTotal2 = (TextView) findViewById(R.id.valTotalnya);
                            TextView txtTotal3 = (TextView) findViewById(R.id.txtValCaraBayar);
                            EditText txtDibayarkan = (EditText) findViewById(R.id.txtDibayarkan);
                            txtTotal.setText("Rp "+decimalFormat.format(jsonObject.getInt("d")));
                            valTotal2.setText("Rp "+decimalFormat.format(jsonObject.getInt("d")));
                            txtTotal2.setText(jsonObject.getString("d"));
                            txtDibayarkan.setText(decimalFormat2.format(jsonObject.getInt("d")));
                            stringpas = decimalFormat2.format(jsonObject.getInt("d"));

                            if (jsonObject.getString("e").equals("100")) {
                                txtTotal3.setText("Cash");
                                layBank.setVisibility(View.GONE);
                            } else if (jsonObject.getString("e").equals("200")) {
                                txtTotal3.setText("Card");
                                layBank.setVisibility(View.VISIBLE);
                            } else if (jsonObject.getString("e").equals("300")) {
                                txtTotal3.setText("Transfer");
                                layBank.setVisibility(View.VISIBLE);
                            }

                            btnBayar2.setText("Bayar");
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
    private void getCustomer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            valCust.setText(jsonObject.getString("data"));

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
                params.put("act", "getdata_ordercustomer2");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onBackPressed() {

        Intent i = new Intent(context, CheckoutActivity.class);
        i.putExtra("server",getServer);
        startActivity(i);

    }
}
