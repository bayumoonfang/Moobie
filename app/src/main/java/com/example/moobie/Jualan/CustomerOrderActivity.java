package com.example.moobie.Jualan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.moobie.Function.ExpandableHeightGridView;
import com.example.moobie.R;
import com.example.moobie.SessionManager;
import com.example.moobie.publicURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerOrderActivity extends AppCompatActivity {

    SessionManager sessionManager;
    private Context context = CustomerOrderActivity.this;
    String getServer, getOrdernumber;
    RelativeLayout layBack, layDibayarkan;
    Button btnBayar;
    EditText txtDibayar, txtCari;
    List<CustomerOrderItem> listitem;
    ExpandableHeightGridView gridView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordercustomer);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        listitem = new ArrayList<CustomerOrderItem>();
        HashMap<String, String> user = sessionManager.getUserDetail();
        Bundle bundle = getIntent().getExtras();
        getServer = bundle.getString("server");
        gridView   = findViewById(R.id.grid_customerorder);
        txtCari   = findViewById(R.id.txtCari);
        layBack = findViewById(R.id.layBack);
        getdata_alldata ();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtVal3 = view.findViewById(R.id.txtNoCustomer);
                String text = txtVal3.getText().toString();
              add_customer(text);
            }
        });

        txtCari.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int s, int b, int c) {
                getdata_alldata_search(cs.toString());

            }
            public void afterTextChanged(Editable editable) { }
            public void beforeTextChanged(CharSequence cs, int i, int j, int
                    k) { }
        });

        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PembayaranActivity.class);
                finish();
                i.putExtra("server",getServer);
                startActivity(i);
            }
        });

    }




    private void getdata_alldata () {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //loadingtab.setVisibility(View.GONE);
                            JSONArray array = new JSONArray(response);
                            if (array.length() == 0){
                                gridView.setVisibility(View.GONE);
                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    CustomerOrderItem itemList = new CustomerOrderItem(
                                            object.getString("d"),
                                            object.getString("a"),
                                            object.getString("e"),
                                            object.getString("b"));
                                    listitem.add(itemList);
                                }

                                CustomerOrderAdapter adapter = new CustomerOrderAdapter(listitem, context);
                                gridView.setVisibility(View.VISIBLE);
                                gridView.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView.setVisibility(View.GONE);

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
                params.put("act", "getallcustomer");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getdata_alldata_search (final String valCari) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //loadingtab.setVisibility(View.GONE);
                            JSONArray array = new JSONArray(response);
                            listitem.clear();
                            if (array.length() == 0){
                                gridView.setVisibility(View.GONE);
                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    CustomerOrderItem itemList = new CustomerOrderItem(
                                            object.getString("d"),
                                            object.getString("a"),
                                            object.getString("e"),
                                            object.getString("b"));
                                    listitem.add(itemList);
                                }

                                CustomerOrderAdapter adapter = new CustomerOrderAdapter(listitem, context);
                                gridView.setVisibility(View.VISIBLE);
                                gridView.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView.setVisibility(View.GONE);

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
                params.put("valcari", valCari);
                params.put("act", "getallcustomer_search");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void add_customer (final String custno) {
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
                                Intent i = new Intent(context, PembayaranActivity.class);
                                i.putExtra("server",getServer);
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
                params.put("act", "order_pending_customer");
                params.put("custno", custno);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }



}
