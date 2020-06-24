package com.example.moobie.Outlet;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class OutletActivity extends AppCompatActivity {

    SessionManager sessionManager;
    String getServer;

    List<OutletItem> listitem;
    private Context context = OutletActivity.this;
    TextView textViewKosong, txtProduk;
    GifImageView imgLoading;
    ImageView imgBack, btnAdd;
    EditText editText;
    ExpandableHeightGridView gridView;
    RelativeLayout layBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getServer = user.get(sessionManager.SERVER);
        gridView   = findViewById(R.id.grid_outlethome);
        listitem = new ArrayList<OutletItem>();
        textViewKosong = findViewById(R.id.txtKosong);
        imgLoading = findViewById(R.id.loadingBar);
        imgBack = findViewById(R.id.imgBack);
        editText = findViewById(R.id.txtCari);
        btnAdd = findViewById(R.id.btnAddOutlet);
        layBack = findViewById(R.id.layBack);

        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(context, AddOutletActivity.class);
                startActivity(intent);
            }
        });



        getdata_alloutlet();

        editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int s, int b, int c) {
                getdata_alloutletsearch(cs.toString());

            }
            public void afterTextChanged(Editable editable) { }
            public void beforeTextChanged(CharSequence cs, int i, int j, int
                    k) { }
        });


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtVal3 = view.findViewById(R.id.txtIDOutlet);
                String text = txtVal3.getText().toString();
                Intent i = new Intent(context, EditOutletActivity.class);
                i.putExtra("server",getServer);
                i.putExtra("itemnumber",text);
                startActivity(i);
            }
        });



    }



    private void getdata_alloutlet () {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //loadingtab.setVisibility(View.GONE);
                            JSONArray array = new JSONArray(response);
                            imgLoading.setVisibility(View.GONE);
                            if (array.length() == 0){
                                gridView.setVisibility(View.GONE);
                                textViewKosong.setVisibility(View.VISIBLE);

                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    OutletItem itemList = new OutletItem(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getString("c"));
                                   listitem.add(itemList);

                                }

                                OutletAdapter adapter = new OutletAdapter(listitem, context);
                                gridView.setVisibility(View.VISIBLE);
                                textViewKosong.setVisibility(View.GONE);
                                gridView.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView.setVisibility(View.GONE);
                            textViewKosong.setVisibility(View.VISIBLE);
                            imgLoading.setVisibility(View.GONE);
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
                params.put("act", "getalloutlet");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    private void getdata_alloutletsearch (final String valCari) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //loadingtab.setVisibility(View.GONE);
                            JSONArray array = new JSONArray(response);
                            imgLoading.setVisibility(View.GONE);
                            listitem.clear();
                            if (array.length() == 0){
                                gridView.setVisibility(View.GONE);
                                textViewKosong.setVisibility(View.VISIBLE);

                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    OutletItem itemList = new OutletItem(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getString("c"));
                                    listitem.add(itemList);

                                }

                                OutletAdapter adapter = new OutletAdapter(listitem, context);
                                gridView.setVisibility(View.VISIBLE);
                                textViewKosong.setVisibility(View.GONE);
                                gridView.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView.setVisibility(View.GONE);
                            textViewKosong.setVisibility(View.VISIBLE);
                            imgLoading.setVisibility(View.GONE);
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
                params.put("act", "getalloutlet_search");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


}
