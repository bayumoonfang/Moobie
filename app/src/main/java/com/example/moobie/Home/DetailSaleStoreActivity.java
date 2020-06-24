package com.example.moobie.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.example.moobie.R;
import com.example.moobie.Report.DetailTransaksiResumeActivity;
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

public class DetailSaleStoreActivity extends AppCompatActivity {
    private Context mContext = DetailSaleStoreActivity.this;
    ImageView backArrow;
    String getBranch, getServer, getStore;
    TextView textView, textCard, textTransfer, textStartamount, textTotal, textCash, txttop2;
    ListView listView;
    List<DetailSalesStoreItem> projectList;
    ImageView imgSearch, imgClose, imgFilter;
    EditText searchForm;
    SessionManager sessionManager;
    RelativeLayout loading;
ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailstorehome);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        Bundle bundle = getIntent().getExtras();
        getBranch = bundle.getString("branch");
        getServer = bundle.getString("server");
        getStore = bundle.getString("storename");
        textView = findViewById(R.id.txtStorename);
        textCash = findViewById(R.id.txtCashtotal);
        textCard = findViewById(R.id.txtCardtotal);
        textTransfer = findViewById(R.id.txtTransfertotal);
        textStartamount = findViewById(R.id.txtStartamounttotal);
        textTotal = findViewById(R.id.txtTotalDetailhome);
        loading = findViewById(R.id.loading);
progressBar = findViewById(R.id.loadingpro2);
       // imgSearch = findViewById(R.id.imgSearch);
        imgClose = findViewById(R.id.imgClose);
        imgFilter = findViewById(R.id.imgFilter);
        searchForm = findViewById(R.id.searchform);
        txttop2 = findViewById(R.id.txttop2);

        //listView = findViewById(R.id.listview_detailhome);
        projectList = new ArrayList<>();
        textView.setText(getStore.toString());


        backArrow = findViewById(R.id.backHome);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

      /*  imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSearch.setVisibility(View.GONE);
                imgClose.setVisibility(View.VISIBLE);
                searchForm.setVisibility(View.VISIBLE);
                txttop2.setVisibility(View.GONE);
                searchForm.setFocusable(true);
                searchForm.requestFocus();
                searchForm.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });*/

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSearch.setVisibility(View.VISIBLE);
                imgClose.setVisibility(View.GONE);
                txttop2.setVisibility(View.VISIBLE);
                searchForm.setVisibility(View.GONE);
                searchForm.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });

        searchForm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent i = new Intent(mContext, SearchTransActivity.class);
                    i.putExtra("search",searchForm.getText().toString());
                    i.putExtra("branch",getBranch);
                    i.putExtra("server",getServer);
                    i.putExtra("storename",getStore);
                    startActivity(i);

                }
                return false;
            }
        });


        ExpandableHeightGridView expandableHeightGridView = findViewById(R.id.gridPilihan);
        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtVal1 = view.findViewById(R.id.txtTransOrderNumb);
                String text = txtVal1.getText().toString();
                Intent i = new Intent(mContext, DetailTransaksiResumeActivity.class);
                i.putExtra("branch",getBranch);
                i.putExtra("server",getServer);
                i.putExtra("ordernumber",text);

                startActivity(i);
            }
        });



        getCashhome();
        getCardhome();
        getTransferhome();
        getStartamounthome();
        gettotalhome();
        getTransstore();
    }




    private void getTransstore() {
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
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    DetailSalesStoreItem detailSalesStoreItem = new DetailSalesStoreItem(
                                            object.getInt("a"),
                                            object.getString("b"),
                                            object.getString("c")
                                    );
                                    projectList.add(detailSalesStoreItem);
                                }
                            } else {

                            }

                            final DetailSalesStoreListAdapter detailSalesStoreListAdapter = new DetailSalesStoreListAdapter(
                                    projectList, mContext);
                            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridPilihan);
                            gridView.setAdapter(detailSalesStoreListAdapter);

                            //listView.setAdapter(detailSalesStoreListAdapter);
                            //listView.setTextFilterEnabled(true);



                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(mContext, "Error Reading"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "gettransstore");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }

    private void getCashhome() {
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

                            Integer totalcash = object.getInt("TOTAL");
                            textCash.setText(decimalFormat.format(totalcash));

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "getcashtotal");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }



    private void getCardhome() {
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

                            Integer totalcard = object.getInt("TOTAL");
                            textCard.setText(decimalFormat.format(totalcard));

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "getcardtotal");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }




    private void getTransferhome() {
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

                            Integer totaltransfer = object.getInt("TOTAL");
                            textTransfer.setText(decimalFormat.format(totaltransfer));

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "gettransfertotal");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }



    private void getStartamounthome() {
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

                            Integer totalstartamount = object.getInt("TOTAL");
                            textStartamount.setText(decimalFormat.format(totalstartamount));

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "getstartamounttotal");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }




    private void gettotalhome() {
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

                            Integer totalval = object.getInt("TOTAL");
                            textTotal.setText(decimalFormat.format(totalval));

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "gettotal");
                params.put("branch", getBranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }






}
