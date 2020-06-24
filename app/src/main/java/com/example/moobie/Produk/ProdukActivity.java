package com.example.moobie.Produk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class ProdukActivity extends AppCompatActivity {
    SessionManager sessionManager;
    List<ProdukItem> listitem;
    private Context context = ProdukActivity.this;
    String getServer;
    ExpandableHeightGridView gridView2;
    TextView textViewKosong, txtProduk;
    GifImageView imgLoading;
    Spinner spinner;
    ImageView imgBack, imgClear, btnAdd;
EditText txtSearch;
RelativeLayout layBack, layKosong, layLoading, imgSearch;

    private Context mContext = ProdukActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String getEmail =  user.get(sessionManager.USERNAME);
        listitem = new ArrayList<ProdukItem>();
        getServer = user.get(sessionManager.SERVER);
        gridView2   = findViewById(R.id.grid_produkhome);
        textViewKosong = findViewById(R.id.txtKosong);
        imgLoading = findViewById(R.id.loadingBar);
       // spinner = findViewById(R.id.spinner_category);
        imgBack = findViewById(R.id.imgBack);
        imgSearch = findViewById(R.id.imgSearch);
        imgClear = findViewById(R.id.imgClear);
        txtSearch = findViewById(R.id.txtSearch);
        txtProduk = findViewById(R.id.txtProduk);
        btnAdd = findViewById(R.id.btnAddProduk);
        layBack = findViewById(R.id.layBack);
        layLoading = findViewById(R.id.layLoading);
        layKosong= findViewById(R.id.layKosong);



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
                Intent intent = new Intent(context, AddProdukActivity.class);
                startActivity(intent);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSearch.setVisibility(View.GONE);
                txtProduk.setVisibility(View.GONE);
                imgClear.setVisibility(View.VISIBLE);
                txtSearch.setVisibility(View.VISIBLE);
                txtSearch.setFocusable(true);
                txtSearch.requestFocus();
                txtSearch.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            }
        });

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSearch.setVisibility(View.VISIBLE);
                txtProduk.setVisibility(View.VISIBLE);
                imgClear.setVisibility(View.GONE);
                txtSearch.setVisibility(View.GONE);
                txtSearch.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                tabLayout.selectTab(tabLayout.getTabAt(0));
            }
        });

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getdata_produksearch(txtSearch.getText().toString());
                }
                return false;
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int s, int b, int c) {
               getdata_produksearch(cs.toString());

            }
            public void afterTextChanged(Editable editable) { }
            public void beforeTextChanged(CharSequence cs, int i, int j, int
                    k) { }
        });



       /* spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinner2Val = parent.getSelectedItem().toString();
                if (spinner2Val.equals("Semua Kategori")) {
                    getdata_allproduk();
                } else {
                    //getdata_allprodukfilter_category(spinner2Val);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

       getdata_kategori2();


        ExpandableHeightGridView expandableHeightGridView = findViewById(R.id.grid_produkhome);
        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtVal3 = view.findViewById(R.id.txtItemNumber);
                String text = txtVal3.getText().toString();
                Intent i = new Intent(mContext, EditProduk.class);
                i.putExtra("server",getServer);
                i.putExtra("itemnumber",text);
                startActivity(i);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String filternya = tab.getText().toString();
                getdata_produkfilter(filternya);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





    }


    private void getdata_kategori() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<String> categories2 = new ArrayList<String>();
                        categories2.add("Semua Kategori");
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {

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
                params.put("act", "getdata_kategoriproduk");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getdata_allproduk () {
        layKosong.setVisibility(View.GONE);
        layLoading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //loadingtab.setVisibility(View.GONE);
                            JSONArray array = new JSONArray(response);
                            //imgLoading.setVisibility(View.GONE);
                            if (array.length() == 0){
                                gridView2.setVisibility(View.GONE);
                                layKosong.setVisibility(View.VISIBLE);
                                layLoading.setVisibility(View.GONE);

                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    ProdukItem itemList = new ProdukItem(
                                            object.getString("a"),
                                            object.getString("b")+" - "+object.getString("c"),
                                            //object.getInt("c"),
                                            object.getString("d"),
                                            object.getString("f"));
                                    listitem.add(itemList);
                                    layKosong.setVisibility(View.GONE);
                                    layLoading.setVisibility(View.GONE);

                                }

                                ProdukAdapter adapter = new ProdukAdapter(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);
                            layKosong.setVisibility(View.GONE);
                            layLoading.setVisibility(View.GONE);
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
                params.put("act", "getallproduk");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getdata_kategori2() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                TabLayout tabLayout = findViewById(R.id.tabLayout);
                                tabLayout.addTab(tabLayout.newTab().setText("Semua Menu"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    tabLayout.addTab(tabLayout.newTab().setText(object.getString("a")));
                                }

                                if(tabLayout.getTabCount()<4)
                                {
                                    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                                } else {
                                    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                                }
                            }

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
                params.put("act", "getdata_kategoriproduk");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    private void getdata_produkfilter (final String filterkota ) {
        gridView2.setVisibility(View.GONE);
        layKosong.setVisibility(View.GONE);
        layLoading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            listitem.clear();
                            if (array.length() == 0){
                                gridView2.setVisibility(View.GONE);
                               layKosong.setVisibility(View.VISIBLE);
                                layLoading.setVisibility(View.GONE);
                            }else{

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    ProdukItem itemList = new ProdukItem(
                                            object.getString("a"),
                                            object.getString("b")+" - Rp. "+object.getString("c"),
                                            //object.getInt("c"),
                                            object.getString("d"),
                                            object.getString("f"));
                                    listitem.add(itemList);
                                    layKosong.setVisibility(View.GONE);
                                    layLoading.setVisibility(View.GONE);
                                }
                                ProdukAdapter adapter = new ProdukAdapter(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);
                            layKosong.setVisibility(View.GONE);
                            layLoading.setVisibility(View.GONE);
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
                params.put("act", "getallproduk_filter");
                if (filterkota.equals("Semua Menu")) {
                    params.put("filter", "");
                } else {
                    params.put("filter", filterkota);
                }

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getdata_produksearch (final String searchVal ) {
        gridView2.setVisibility(View.GONE);
        layKosong.setVisibility(View.GONE);
        layLoading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            listitem.clear();
                            if (array.length() == 0){
                                gridView2.setVisibility(View.GONE);
                                layKosong.setVisibility(View.VISIBLE);
                                layLoading.setVisibility(View.GONE);
                            }else{

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    ProdukItem itemList = new ProdukItem(
                                            object.getString("a"),
                                            object.getString("b")+" - Rp. "+object.getString("c"),
                                            //object.getInt("c"),
                                            object.getString("d"),
                                            object.getString("f"));
                                    listitem.add(itemList);
                                    layKosong.setVisibility(View.GONE);
                                    layLoading.setVisibility(View.GONE);
                                }
                                ProdukAdapter adapter = new ProdukAdapter(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);
                            layKosong.setVisibility(View.GONE);
                            layLoading.setVisibility(View.GONE);
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
                params.put("act", "getdata_produksearch");
                params.put("searchval", searchVal);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    /*private void getdata_allprodukfilter_category (final String filterkota ) {
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
                                gridView2.setVisibility(View.GONE);
                                textViewKosong.setVisibility(View.VISIBLE);
                                imgLoading.setVisibility(View.GONE);
                            }else{

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    ProdukItem itemList = new ProdukItem(
                                            object.getString("a"),
                                            object.getString("b")+" - "+object.getString("c"),
                                            //object.getInt("c"),
                                            object.getString("d"),
                                            object.getString("f"));
                                    listitem.add(itemList);
                                    imgLoading.setVisibility(View.GONE);
                                    textViewKosong.setVisibility(View.GONE);
                                    gridView2.setVisibility(View.VISIBLE);
                                }
                                ProdukAdapter adapter = new ProdukAdapter(listitem, context);
                                ExpandableHeightGridView gridView2 = (ExpandableHeightGridView) findViewById(R.id.grid_produkhome);
                                adapter.notifyDataSetChanged();
                                gridView2.setAdapter(adapter);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);
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
                params.put("act", "getallproduk_filter");
                params.put("filter", filterkota);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getdata_produksearch (final String searchVal ) {
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
                                gridView2.setVisibility(View.GONE);
                                textViewKosong.setVisibility(View.VISIBLE);
                                imgLoading.setVisibility(View.GONE);
                            }else{

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    ProdukItem itemList = new ProdukItem(
                                            object.getString("a"),
                                            object.getString("b")+" - "+object.getString("c"),
                                            //object.getInt("c"),
                                            object.getString("d"),
                                            object.getString("f"));
                                    listitem.add(itemList);
                                    imgLoading.setVisibility(View.GONE);
                                    textViewKosong.setVisibility(View.GONE);
                                    gridView2.setVisibility(View.VISIBLE);
                                }
                                ProdukAdapter adapter = new ProdukAdapter(listitem, context);
                                ExpandableHeightGridView gridView2 = (ExpandableHeightGridView) findViewById(R.id.grid_produkhome);
                                adapter.notifyDataSetChanged();
                                gridView2.setAdapter(adapter);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);
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
                params.put("act", "getdata_produksearch");
                params.put("searchval", searchVal);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }*/



}
