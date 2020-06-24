package com.example.moobie.Report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moobie.Function.ExpandableHeightGridView;
import com.example.moobie.Home.HomeActivity;
import com.example.moobie.R;
import com.example.moobie.SessionManager;
import com.example.moobie.publicURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DailyReportActivity extends AppCompatActivity {
    SessionManager sessionManager;
    String getServer;
    List<DailyReportItem> listitem;
    private Context context = DailyReportActivity.this;
    ExpandableHeightGridView gridView2;
    EditText editText;
    RelativeLayout layUp, layDown, layKosong, layLoading, layBack;
    TextView textView;
    ImageView imgUp, imgDown;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todaysales);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getServer = user.get(sessionManager.SERVER);
        listitem = new ArrayList<DailyReportItem>();
        gridView2   = findViewById(R.id.grid_dailysales);
        editText = findViewById(R.id.txtCariDailySales);
        layUp = findViewById(R.id.layUp);
        layDown = findViewById(R.id.layDown);
        layKosong = findViewById(R.id.layKosong);
        layLoading = findViewById(R.id.layLoading);
        textView= findViewById(R.id.label2);
        imgUp= findViewById(R.id.imgUp);
        imgDown= findViewById(R.id.imgDown);
        layBack = findViewById(R.id.layBack);


        Locale localeID = new Locale("in", "ID");
        Calendar c = Calendar.getInstance(localeID);
        String[] days = new String[] { "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu" };
        String[]monthName={"Januari","Februari","Maret", "April", "Mei", "Juni", "Juli",
                "Agustus", "September", "Oktober", "November",
                "Desember"};
        String hari = days[c.get(Calendar.DAY_OF_WEEK) - 2];
        String month = monthName[c.get(Calendar.MONTH)];
        int year = c.get(Calendar.YEAR);
        int date = c.get(Calendar.DATE);


        textView.setText("("+hari+" , "+date+" "+month+" "+year+")");
        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
        });

        layUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata_sort(" ORDER BY SUM(PAID) ASC");
                imgUp.setColorFilter(ContextCompat.getColor(context, R.color.colorOrange2));
                imgDown.setColorFilter(ContextCompat.getColor(context, R.color.colorFullBlack));
            }
        });

        layDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata_sort(" ORDER BY SUM(PAID) DESC");
                imgUp.setColorFilter(ContextCompat.getColor(context, R.color.colorFullBlack));
                imgDown.setColorFilter(ContextCompat.getColor(context, R.color.colorOrange2));
            }
        });





        editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int s, int b, int c) {
                getdata_search(cs.toString());

            }
            public void afterTextChanged(Editable editable) { }
            public void beforeTextChanged(CharSequence cs, int i, int j, int
                    k) { }
        });

        getdata_alldata();
        getdata_total ();


        ExpandableHeightGridView expandableHeightGridView = findViewById(R.id.grid_dailysales);
        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtVal3 = view.findViewById(R.id.txtOrderNumber);
                String text = txtVal3.getText().toString();
                Intent i = new Intent(context, DetailTransaksiResumeActivity.class);
                i.putExtra("server",getServer);
                i.putExtra("ordernumber",text);
                startActivity(i);
            }
        });



    }


    private void getdata_alldata () {
        layLoading.setVisibility(View.VISIBLE);
        layKosong.setVisibility(View.GONE);
        gridView2.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() == 0){
                                layLoading.setVisibility(View.GONE);
                                layKosong.setVisibility(View.VISIBLE);
                                gridView2.setVisibility(View.GONE);

                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    DailyReportItem itemList = new DailyReportItem(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getString("d"),
                                            object.getInt("c"));
                                    listitem.add(itemList);
                                    layLoading.setVisibility(View.GONE);
                                    layKosong.setVisibility(View.GONE);
                                }

                                DailyReportAdapter adapter = new DailyReportAdapter(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);
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
                params.put("act", "getdata_dailysales");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getdata_search (final String cariku ) {
        layLoading.setVisibility(View.VISIBLE);
        layKosong.setVisibility(View.GONE);
        gridView2.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            listitem.clear();
                            if (array.length() == 0){
                                layLoading.setVisibility(View.GONE);
                                layKosong.setVisibility(View.VISIBLE);
                                gridView2.setVisibility(View.GONE);

                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    DailyReportItem itemList = new DailyReportItem(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getString("d"),
                                            object.getInt("c"));
                                    listitem.add(itemList);
                                    layLoading.setVisibility(View.GONE);
                                    layKosong.setVisibility(View.GONE);
                                }

                                DailyReportAdapter adapter = new DailyReportAdapter(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);


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
                params.put("act", "getdata_dailysales_search");
                params.put("filter", cariku);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getdata_sort (final String sortku ) {
        layLoading.setVisibility(View.VISIBLE);
        layKosong.setVisibility(View.GONE);
        gridView2.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            listitem.clear();
                            if (array.length() == 0){
                                layLoading.setVisibility(View.GONE);
                                layKosong.setVisibility(View.VISIBLE);
                                gridView2.setVisibility(View.GONE);

                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    DailyReportItem itemList = new DailyReportItem(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getString("d"),
                                            object.getInt("c"));
                                    listitem.add(itemList);
                                    layLoading.setVisibility(View.GONE);
                                    layKosong.setVisibility(View.GONE);
                                }

                                DailyReportAdapter adapter = new DailyReportAdapter(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);


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
                params.put("act", "getdata_dailysales");
                params.put("filter", sortku);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void getdata_total () {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            if (array.length() == 0){
                                gridView2.setVisibility(View.GONE);

                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                    symbols.setGroupingSeparator(',');
                                    symbols.setDecimalSeparator(',');
                                    DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                                    TextView textView = (TextView) findViewById(R.id.txtTotalSales);
                                    textView.setText("Rp "+decimalFormat.format(object.getInt("a")));

                                }

                                DailyReportAdapter adapter = new DailyReportAdapter(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);


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
                params.put("act", "getdata_dailysales_total");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


}
