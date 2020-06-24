package com.example.moobie.Report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.example.moobie.Function.ExpandableHeightGridView;
import com.example.moobie.Jualan.JualanActivity;
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

public class DetailTransaksiResumeActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private Context mContext = DetailTransaksiResumeActivity.this;
    TextView textView11, textView12
    , textView13,
            textView14,
    textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView22,
    textView23;
    ListView listView;
    List<DetailTransaksiResumeItem> projectList;
    String getBranch, getServer, getOrderNumber;
    ProgressBar progressBar;
    ImageView backArrow;
    RelativeLayout layBack;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailtransresume);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        projectList = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        getServer = user.get(sessionManager.SERVER);
        getOrderNumber = bundle.getString("ordernumber");

        textView3 = findViewById(R.id.txtTglTransaksi);
        textView4 = findViewById(R.id.txtDetailResumeCustid);
        textView5 = findViewById(R.id.txtDetailResumeCustnama);
        textView6 = findViewById(R.id.txtDetailResumeCustphone);
        textView7 = findViewById(R.id.txtDetailResumeCustaddress);
        progressBar = findViewById(R.id.load1);
        layBack = findViewById(R.id.layBack);
        textView11 = findViewById(R.id.textView11);
        textView12 = findViewById(R.id.textView12);
        textView13 = findViewById(R.id.textView13);
        textView14 = findViewById(R.id.textView14);

        backArrow = findViewById(R.id.imgArrow);
        textView22 = findViewById(R.id.txtOrderNumber);
        textView23 = findViewById(R.id.txtCaraBayar);
        textView22.setText(getOrderNumber);
        getdata_orderdetail();
        getdata_orderpayment();
        getdata_ordercustomer();
        getDetailResumeProduk();
        //getDetailResumeProduk();
       // getDetailResumeProduk2();
        //getDetailResumeProduk3();
        //getDetailResumeProduk4();

        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DailyReportActivity.class);
                finish();
                i.putExtra("server",getServer);
                startActivity(i);
            }
        });


    }

    private void getDetailResumeProduk() {
        progressBar.setVisibility(View.VISIBLE);
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
                                    DetailTransaksiResumeItem detailTransaksiResumeItem = new DetailTransaksiResumeItem(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getString("c")
                                    );
                                    projectList.add(detailTransaksiResumeItem);

                                }
                            }
                            final DetailTransaksiResumeAdapter detailTransaksiResumeAdapter = new DetailTransaksiResumeAdapter(
                                    projectList, mContext);
                            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridPilihan3);
                            gridView.setAdapter(detailTransaksiResumeAdapter);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
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
                params.put("ordernumber", getOrderNumber);
                params.put("act", "getdetailresumeproduk");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }


    private void getdata_orderdetail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            textView3.setText(jsonObject.getString("a"));
                            textView23.setText(jsonObject.getString("b"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new StyleableToast
                                    .Builder(mContext)
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
                params.put("act", "getdata_orderdetail");
                params.put("ordernumber", getOrderNumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
    private void getdata_orderpayment() {
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

                            textView11.setText(decimalFormat.format(jsonObject.getInt("a")));
                            textView12.setText(decimalFormat.format(jsonObject.getInt("b")));
                            textView13.setText(decimalFormat.format(jsonObject.getInt("c")));
                            textView14.setText(decimalFormat.format(jsonObject.getInt("d")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new StyleableToast
                                    .Builder(mContext)
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
                params.put("act", "getdata_orderpayment");
                params.put("ordernumber", getOrderNumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
    private void getdata_ordercustomer() {
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
                            textView4.setText(jsonObject.getString("a"));
                            textView5.setText(jsonObject.getString("b"));
                            textView6.setText(jsonObject.getString("c"));
                            textView7.setText(jsonObject.getString("d"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new StyleableToast
                                    .Builder(mContext)
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
                params.put("act", "getdata_ordercustomer");
                params.put("ordernumber", getOrderNumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }



}
