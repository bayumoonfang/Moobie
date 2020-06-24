package com.example.moobie.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResumeActivity extends AppCompatActivity {
    private static final String PREF_NAME = "";
    SessionManager sessionManager;
    Button button;

    private Context mContext = ResumeActivity.this;
    private TextView txtVal1, tctVal2, intVal1,
            txtDatehome, txtTotalhome,txtsettDate, txtnodata;

    ExpandableHeightGridView listView;
    List<StoreTransHomeItem> projectList;
    String getServer;
    RelativeLayout loading;
    ImageView imgsett;
    ProgressBar progressBar, progressBar2;
    TextView textView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        sessionManager = new SessionManager(this);

        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        //overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

        txtDatehome = findViewById(R.id.txtDatehome);
        //listView = findViewById(R.id.listview_home);
        txtTotalhome =findViewById(R.id.txtTotalhome);
        button = findViewById(R.id.refreshHomebtn);
        loading = findViewById(R.id.loading);
        txtsettDate = findViewById(R.id.txtsettDate);
        progressBar = findViewById(R.id.loadingpro);
        progressBar2 = findViewById(R.id.loadPro3);
        textView = findViewById(R.id.txtTotalhome);
        txtnodata = findViewById(R.id.txtnodata);
        ExpandableHeightGridView expandableHeightGridView = (ExpandableHeightGridView) findViewById(R.id.gridPilihan2);

        Date date = new Date();
        txtDatehome.setText(DateFormat.getDateInstance(DateFormat.LONG).format(date));
        projectList = new ArrayList<>();
        getServer = user.get(sessionManager.SERVER);


        //Bundle bundle = getIntent().getExtras();
        //getInvoice = bundle.getString("server");



        ImageView backArrow = (ImageView) findViewById(R.id.backHome);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sessionManager.logout();
                //finish();
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                finish();
                //finish();
                //Intent intent = new Intent(mContext, ResumeActivity.class);
                //startActivity(intent);
            }
        });



        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtVal1 = view.findViewById(R.id.idValList1);
                TextView txtVal2 = view.findViewById(R.id.storenameHome);
                TextView txtVal3 = view.findViewById(R.id.storeinfoHome);
                String text = txtVal1.getText().toString();
                String text2 = txtVal2.getText().toString();
                String text3 = txtVal3.getText().toString();
                Intent i = new Intent(mContext, SummaryDetailActivity.class);
                i.putExtra("branch",text);
                i.putExtra("storename",text2);
                i.putExtra("server",getServer);
                i.putExtra("storeaddress",text3);
                startActivity(i);
            }
        });




        imgsett = findViewById(R.id.imgsett);
        imgsett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context wrapper = new ContextThemeWrapper(mContext, R.style.PopUpsearch);
                PopupMenu popup = new PopupMenu(wrapper, imgsett);
                popup.getMenuInflater().inflate(R.menu.popup_homesett, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(mContext,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        changeDatereport(item.getTitle().toString());
                        return true;
                    }
                });
                popup.show();
            }
        });

        getstoreHome();
        gettotalHome();
        //getdatereport();
        //getDateformat();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            //overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        }
    }


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar aplikasi..", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                //finish();
            }
        }, 2000);
    }






    private void changeDatereport(final String valdate) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if(success.equals("1")){
                                Intent i = new Intent(mContext, ResumeActivity.class);
                                finish();
                                startActivity(i);
                            }else{
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "changedatetrans");
                params.put("valdate", valdate);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }







    private void getstoreHome() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            progressBar.setVisibility(View.GONE);
                            if (array.length() > 0) {
                                txtnodata.setVisibility(View.GONE);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    StoreTransHomeItem storeTransHomeItem = new StoreTransHomeItem(
                                            object.getInt("TOTAL"),
                                            object.getString("NAMA"),
                                            object.getString("ADDRESS"),
                                            object.getString("BRANCH")
                                    );


                                    projectList.add(storeTransHomeItem);
                                }
                            } else {
                                    txtnodata.setVisibility(View.VISIBLE);
                            }

                            StoreTransHomeListAdapter storeTransHomeListAdapter = new StoreTransHomeListAdapter(
                                    projectList, mContext);
                            //listView.setAdapter(storeTransHomeListAdapter);
                            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridPilihan2);
                            gridView.setAdapter(storeTransHomeListAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            txtnodata.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        txtnodata.setVisibility(View.GONE);
                        //Toast.makeText(mContext, "Error Reading"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "today_getlisttransaksi");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }


    private void gettotalHome() {
        progressBar2.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar2.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            Integer total = object.getInt("TOTAL");
                            /*Locale localeID = new Locale("in", "ID");
                            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                            txtTotalhome.setText(formatRupiah.format((double)total));*/

                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                            symbols.setGroupingSeparator(',');
                            symbols.setDecimalSeparator(',');
                            DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                            txtTotalhome.setText(decimalFormat.format(total));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar2.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        //Toast.makeText(mContext, "Error Reading"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "today_gettotaltransaksi");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }



    private void getDateformat() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        txtDatehome.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Error Reading"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "getdateformat");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }


}
