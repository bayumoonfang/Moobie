package com.example.moobie.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.moobie.Function.ExpandableHeightGridView;
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

public class SummaryDetailActivity extends AppCompatActivity {
    private Context mContext = SummaryDetailActivity.this;
    SessionManager sessionManager;
    TextView textView, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9;
    String getBranch,getServer, getStore, getAddress;
    List<SummaryDetailItem> projectList;
    Button button, button2;
    ImageView imageView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summarydetail);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


        projectList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        getBranch = bundle.getString("branch");
        getServer = bundle.getString("server");
        getStore = bundle.getString("storename");
        getAddress = bundle.getString("storeaddress");

        textView = findViewById(R.id.txtSummaryStorename);
        textView2 = findViewById(R.id.txtSummaryAddress);
        textView3 = findViewById(R.id.txtSummaryCashiercount);
        textView4 = findViewById(R.id.txtSummaryCodeStore);
        textView5 = findViewById(R.id.txtSummaryProductCount);
        textView6 =findViewById(R.id.txtSummaryTotalSales);
        textView7 =findViewById(R.id.txtSummaryCashiercount);
        textView8 = findViewById(R.id.txtSummaryTotalpayment);
        textView9 = findViewById(R.id.txtSummaryDate);
        imageView = findViewById(R.id.backHome);
        button = findViewById(R.id.btnSummarySalesDetail);
        button2 =findViewById(R.id.btnSummaryPaymentDetail);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, SearchTransActivity.class);
                i.putExtra("search","");
                i.putExtra("branch",getBranch);
                i.putExtra("server",getServer);
                i.putExtra("storename",getStore);

                startActivity(i);
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailSaleStoreActivity.class);
                i.putExtra("branch",getBranch);
                i.putExtra("server",getServer);
                i.putExtra("storename",getStore);

                startActivity(i);
            }
        });


        textView.setText(getStore);
        textView2.setText(getAddress);
        textView4.setText(getBranch);


        getCountProduct();
        gettotalTrans();
        gettotalPayment();
        getCountKasir();
        getProdukSales();
    }



    private void getProdukSales() {
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
                                    SummaryDetailItem detailSalesStoreItem = new SummaryDetailItem(
                                            object.getString("b"),
                                            object.getString("c"),
                                            object.getString("a")
                                    );
                                    projectList.add(detailSalesStoreItem);
                                }
                            } else {

                            }

                            final SummaryDetailListAdapter detailSalesStoreListAdapter = new SummaryDetailListAdapter(
                                    projectList, mContext);
                            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridPilihan6);
                            gridView.setAdapter(detailSalesStoreListAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "today_getsummaryproduksales");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }



    private void getCountProduct() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            String a = object.getString("a");
                            textView5.setText(a);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("act", "today_getcountproduct");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }



    private void getCountKasir() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            String a = object.getString("a");
                            textView7.setText(a);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("act", "today_getcountkasir");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }


    private void gettotalTrans() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                            symbols.setGroupingSeparator(',');
                            symbols.setDecimalSeparator(',');
                            DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                            Integer totalcash = object.getInt("a");
                            String b = object.getString("b");
                            textView6.setText(decimalFormat.format(totalcash));
                            textView9.setText(b);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("act", "today_getsummary_storesale");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }



    private void gettotalPayment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                            symbols.setGroupingSeparator(',');
                            symbols.setDecimalSeparator(',');
                            DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                            Integer a = object.getInt("a");
                            textView8.setText(decimalFormat.format(a));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("act", "today_getsummary_storepayment");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

}
