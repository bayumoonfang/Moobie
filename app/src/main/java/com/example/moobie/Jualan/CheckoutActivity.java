package com.example.moobie.Jualan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.example.moobie.Function.ExpandableHeightGridView;
import com.example.moobie.Home.CheckoutProdukItem;
import com.example.moobie.R;
import com.example.moobie.SessionManager;
import com.example.moobie.publicURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {


    SessionManager sessionManager;
    private Context context = CheckoutActivity.this;
    ListView listView;
    List<CheckoutProdukItem> projectList;
    String getServer, getVal1;
    RelativeLayout layBack, layLoading, layDelete2;
    Button btnBayar, btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        projectList = new ArrayList<>();
       // Bundle bundle = getIntent().getExtras();
        getServer = "forsale";
        layBack = findViewById(R.id.layBack);
        layLoading = findViewById(R.id.layLoading);
        btnBayar = findViewById(R.id.btnBayar);
        btnDelete = findViewById(R.id.btnDelete);



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                alertDialogBuilder.setMessage("Apakah anda yakin menghapus transaksi ini ?");
                alertDialogBuilder.setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                voidtransaksi();
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


        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, JualanActivity.class);
                finish();
                i.putExtra("server",getServer);
                startActivity(i);
            }
        });

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PembayaranActivity.class);
                i.putExtra("server",getServer);
                startActivity(i);
            }
        });


        getDetailResumeProduk();
        getTotal();


        final ExpandableHeightGridView expandableHeightGridView = findViewById(R.id.gridPilihan3);
        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView txtVal3 = view.findViewById(R.id.txtOrderNumber);
                final TextView txtval4 = view.findViewById(R.id.txtDetailResumeNamamenu);
                RelativeLayout img1 = view.findViewById(R.id.cekme2);
                RelativeLayout img2 = view.findViewById(R.id.layOut2);

                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String text = txtVal3.getText().toString();
                        final String text2 = txtval4.getText().toString();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                        alertDialogBuilder.setMessage("Apakah anda yakin menghapus '" +text2+ "' ?");
                        alertDialogBuilder.setPositiveButton("Iya",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        voidtransaksi2(text);
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

                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = txtVal3.getText().toString();
                        Intent i = new Intent(context, EditOrderActivity.class);
                        i.putExtra("server",getServer);
                        i.putExtra("ordernumber",text);
                        startActivity(i);
                    }
                });

               /* View layout = getLayoutInflater().inflate(R.layout.popup_qtycheckout,null);
                final PopupWindow popup = new PopupWindow(context);
                popup.setContentView(layout);
                popup.setOutsideTouchable(true);
                popup.setFocusable(true);
                popup.setWidth(400);
                popup.setHeight(580);
                popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);*/


            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(context, JualanActivity.class);
        startActivity(i);
    }


    private void voidtransaksi2 (final String stringku) {
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
                                getDetailResumeProduk();
                                getTotal();
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
                params.put("act", "order_pending_void2");
                params.put("id", stringku);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void voidtransaksi () {
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
                                Intent i = new Intent(context, JualanActivity.class);
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
                params.put("act", "order_pending_void");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getDetailResumeProduk() {
        layLoading.setVisibility(View.VISIBLE);
        btnBayar.setText("Loading...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            projectList.clear();
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    CheckoutProdukItem detailTransaksiResumeItem = new CheckoutProdukItem(
                                            object.getInt("c"),
                                            object.getString("a"),
                                            object.getString("i"),
                                            object.getString("b"),
                                            object.getString("j")
                                    );
                                    projectList.add(detailTransaksiResumeItem);
                                }
                            } else {
                                Intent i = new Intent(context, JualanActivity.class);
                                finish();
                                startActivity(i);
                            }

                            final CheckoutProdukAdapter detailTransaksiResumeAdapter = new CheckoutProdukAdapter(
                                    projectList, context);
                            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridPilihan3);
                            gridView.setAdapter(detailTransaksiResumeAdapter);
                            layLoading.setVisibility(View.GONE);
                            btnBayar.setText("Lanjutkan Pembayaran");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
                            layLoading.setVisibility(View.GONE);
                            btnBayar.setText("Lanjutkan Pembayaran");
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
                params.put("act", "getdetailresumeprodukpending");
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

                            TextView txtSubtotal = (TextView) findViewById(R.id.txtTotal);
                            TextView txt2 = (TextView) findViewById(R.id.valSubTotal);
                            TextView txt3 = (TextView) findViewById(R.id.valServiceCharge);
                            TextView txt4 = (TextView) findViewById(R.id.valTax);

                            txtSubtotal.setText("Rp."+decimalFormat.format(jsonObject.getInt("d")));
                            txt2.setText("Rp."+decimalFormat.format(jsonObject.getInt("a")));
                            txt3.setText("Rp."+decimalFormat.format(jsonObject.getInt("b")));
                            txt4.setText("Rp."+decimalFormat.format(jsonObject.getInt("c")));

                            /*TextView txtSubtotal = (TextView) findViewById(R.id.valSubTotal);
                            TextView txtServCharge = (TextView) findViewById(R.id.valServiceCharge);
                            TextView txtPPN = (TextView) findViewById(R.id.valTax);
                            TextView txtTotalBelanja = (TextView) findViewById(R.id.valTotalBelanja);

                            txtSubtotal.setText(decimalFormat.format(jsonObject.getInt("a")));
                            txtServCharge.setText(decimalFormat.format(jsonObject.getInt("b")));
                            txtPPN.setText(decimalFormat.format(jsonObject.getInt("c")));
                            txtTotalBelanja.setText("Rp."+decimalFormat.format(jsonObject.getInt("d")));*/




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
