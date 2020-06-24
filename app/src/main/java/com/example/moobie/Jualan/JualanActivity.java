package com.example.moobie.Jualan;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.example.moobie.Function.ExpandableHeightGridView;
import com.example.moobie.Home.HomeActivity;
import com.example.moobie.R;
import com.example.moobie.SessionManager;
import com.example.moobie.publicURL;
import com.google.android.material.tabs.TabLayout;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JualanActivity extends AppCompatActivity {
    SessionManager sessionManager;
    List<JualanItemEp2> listitem;
    private Context context = JualanActivity.this;
    String getServer;
    ExpandableHeightGridView gridView2;
    TextView textViewKosong, txtProduk, txtJumlahq;
    RelativeLayout layLoading, layBack, layClear, layKosong;
    Spinner spinner;
    ImageView imgBack, imgSearch, imgClear, btnAdd, btnFilter, btnCheckout;
    EditText txtSearch;
    Button btnSimpan;
    String jikakosong = "0";
    ProgressBar loadingbtn;
    TranslateAnimation animate , animate2;
String favmas = "0";


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jualan_ep2);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        getServer = user.get(sessionManager.SERVER);
        listitem = new ArrayList<JualanItemEp2>();
        gridView2   = findViewById(R.id.grid_produkjualan);
        btnFilter = findViewById(R.id.imgFilter);
        txtSearch = findViewById(R.id.txtCariJualProduk);
       // btnSimpan = findViewById(R.id.btnSimpan);
        layLoading = findViewById(R.id.layLoading);
        layKosong = findViewById(R.id.layKosong);
        layBack = findViewById(R.id.layBack);
        layClear = findViewById(R.id.layClear);
        loadingbtn = findViewById(R.id.loadingButton);
        btnCheckout = findViewById(R.id.btnCheckout);
        txtJumlahq = findViewById(R.id.textJumlah);
        btnCheckout.bringToFront();
        animate = new TranslateAnimation(0,layLoading.getWidth(),0,0);
        animate.setDuration(1500);
        animate.setFillAfter(true);
        animate2 = new TranslateAnimation(0,gridView2.getWidth(),0,0);
        animate2.setDuration(1500);
        animate2.setFillAfter(true);

        getdata_alldata();
        getdata_jumlahorder();
        getdata_totalorder();
        getdata_kategori2();

        ExpandableHeightGridView expandableHeightGridView = findViewById(R.id.grid_produkjualan);
        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtVal3 = view.findViewById(R.id.txtVal4);
                String text = txtVal3.getText().toString();
                //add_order(text);
                cek_varian(text);

            }
        });

        /*btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata_kategori();
            }
        });*/

        txtSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int s, int b, int c) {
                getdata_produksearch(cs.toString());

            }
            public void afterTextChanged(Editable editable) { }
            public void beforeTextChanged(CharSequence cs, int i, int j, int
                    k) { }
        });


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jikakosong == "1") {
                    new StyleableToast
                            .Builder(context)
                            .text("Keranjang masih kosong..")
                            .textColor(getResources().getColor(R.color.colorWhite))
                            .backgroundColor(getResources().getColor(R.color.colorBlack))
                            .length(Toast.LENGTH_SHORT)
                            .show();
                    return ;
                } else {
                    Intent i = new Intent(context, CheckoutActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        });
        /*btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jikakosong == "1") {
                    new StyleableToast
                            .Builder(context)
                            .text("Keranjang masih kosong..")
                            .textColor(getResources().getColor(R.color.colorWhite))
                            .backgroundColor(getResources().getColor(R.color.colorBlack))
                            .length(Toast.LENGTH_LONG)
                            .show();
                    return ;
                } else {
                    Intent i = new Intent(context, CheckoutActivity.class);
                    startActivity(i);
                }

            }
        });*/


        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HomeActivity.class);
                finish();
                i.putExtra("server",getServer);
                startActivity(i);
            }
        });


        layClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata_alldata_love2();
            }
        });


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    String filternya = tab.getText().toString();
                    if (filternya.equals("Last Order")) {
                        getdata_alldata_love ();
                    } else if (filternya.equals("Favorite")){
                        getdata_alldata_love2();
                    } else {
                        getdata_produkfilter(filternya);
                    }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(context, HomeActivity.class);
        finish();
        startActivity(i);
    }
    private void getdata_alldata () {
        layLoading.startAnimation(animate);
        layLoading.setVisibility(View.VISIBLE);
        layKosong.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            if (array.length() == 0){
                                gridView2.setVisibility(View.GONE);
                                layLoading.startAnimation(animate);
                                layLoading.setVisibility(View.GONE);
                                layKosong.setVisibility(View.VISIBLE);
                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    JualanItemEp2 itemList = new JualanItemEp2(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getInt("c"),
                                            object.getString("d"),
                                            object.getString("f"));
                                    listitem.add(itemList);
                                    layLoading.startAnimation(animate);
                                    layLoading.setVisibility(View.GONE);
                                    layKosong.setVisibility(View.GONE);
                                }

                                JualanAdapterEp2 adapter = new JualanAdapterEp2(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);
                            layLoading.startAnimation(animate);
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
    private void getdata_produksearch (final String searchVal ) {
        layKosong.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //loadingtab.setVisibility(View.GONE);
                            JSONArray array = new JSONArray(response);
                            listitem.clear();
                            if (array.length() == 0){
                                gridView2.setVisibility(View.GONE);
                                layKosong.setVisibility(View.VISIBLE);
                            }else{

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    JualanItemEp2 itemList = new JualanItemEp2(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getInt("c"),
                                            object.getString("d"),
                                            object.getString("f"));
                                    listitem.add(itemList);
                                }
                                JualanAdapterEp2 adapter = new JualanAdapterEp2(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);
                                layKosong.setVisibility(View.GONE);
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
                params.put("act", "getdata_produksearch");
                params.put("searchval", searchVal);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getdata_produkfilter (final String filterkota ) {
        gridView2.setVisibility(View.GONE);
        layLoading.startAnimation(animate);
        layLoading.setVisibility(View.VISIBLE);
        layKosong.setVisibility(View.GONE);
        //layLoading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //loadingtab.setVisibility(View.GONE);
                            JSONArray array = new JSONArray(response);
                            listitem.clear();
                            if (array.length() == 0){
                                gridView2.setVisibility(View.GONE);
                                layKosong.setVisibility(View.VISIBLE);
                                layLoading.startAnimation(animate);
                                layLoading.setVisibility(View.GONE);
                            }else{

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    JualanItemEp2 itemList = new JualanItemEp2(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getInt("c"),
                                            object.getString("d"),
                                            object.getString("f"));
                                    listitem.add(itemList);
                                }
                                JualanAdapterEp2 adapter = new JualanAdapterEp2(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);
                                layLoading.startAnimation(animate);
                                layLoading.setVisibility(View.GONE);
                                layKosong.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);
                            layLoading.startAnimation(animate);
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
    private void getdata_alldata_love () {
        gridView2.setVisibility(View.GONE);
        layLoading.startAnimation(animate);
        layLoading.setVisibility(View.VISIBLE);
        layKosong.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //loadingtab.setVisibility(View.GONE);
                            JSONArray array = new JSONArray(response);
                            listitem.clear();
                            if (array.length() == 0){
                                gridView2.setVisibility(View.GONE);
                                layLoading.startAnimation(animate);
                                layLoading.setVisibility(View.GONE);
                                layKosong.setVisibility(View.VISIBLE);
                            }else{

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    JualanItemEp2 itemList = new JualanItemEp2(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getInt("c"),
                                            object.getString("d"),
                                            object.getString("f"));
                                    listitem.add(itemList);
                                }
                                JualanAdapterEp2 adapter = new JualanAdapterEp2(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);
                                layLoading.startAnimation(animate);
                                layLoading.setVisibility(View.GONE);
                                layKosong.setVisibility(View.GONE);
                                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                                tabLayout.selectTab(tabLayout.getTabAt(1));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);
                            layLoading.startAnimation(animate);
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
                params.put("act", "getallproduk_love");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getdata_alldata_love2 () {
        gridView2.setVisibility(View.GONE);
        layLoading.startAnimation(animate);
        layLoading.setVisibility(View.VISIBLE);
        layKosong.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //loadingtab.setVisibility(View.GONE);
                            JSONArray array = new JSONArray(response);
                            listitem.clear();
                            if (array.length() == 0){
                                gridView2.setVisibility(View.GONE);
                                layLoading.startAnimation(animate);
                                layLoading.setVisibility(View.GONE);
                                layKosong.setVisibility(View.VISIBLE);
                            }else{

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    JualanItemEp2 itemList = new JualanItemEp2(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getInt("c"),
                                            object.getString("d"),
                                            object.getString("f"));
                                    listitem.add(itemList);
                                }
                                JualanAdapterEp2 adapter = new JualanAdapterEp2(listitem, context);
                                gridView2.setVisibility(View.VISIBLE);
                                gridView2.setAdapter(adapter);
                                layLoading.startAnimation(animate);
                                layLoading.setVisibility(View.GONE);
                                layKosong.setVisibility(View.GONE);
                                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                                tabLayout.selectTab(tabLayout.getTabAt(2));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading " + e.toString(), Toast.LENGTH_SHORT).show();
                            gridView2.setVisibility(View.GONE);
                            layLoading.startAnimation(animate);
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
                params.put("act", "getallproduk_favorite");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    /*private void getdata_kategori() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<String> categories2 = new ArrayList<String>();
                        Context wrapper =  new ContextThemeWrapper(context, R.style.NoPopupAnimation);
                        PopupMenu menu = new PopupMenu(wrapper, btnFilter, Gravity.END);
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                categories2.add("Pilih Kategori");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    menu.getMenu().add(Menu.NONE, i, i, object.getString("a"));
                                }
                            }

                            menu.show();
                            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    //Toast.makeText(context, item.getTitle() + "clicked", Toast.LENGTH_SHORT).show();
                                    getdata_produkfilter(item.getTitle().toString());
                                    return true;
                                }
                            });

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

    }*/
    private void add_order (final String itemnumber, final String varians) {
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
                                getdata_jumlahorder ();
                                getdata_totalorder ();
                                final TextView txtJumlah = findViewById(R.id.textJumlah);
                                final float startSize = 20; // Size in pixels
                                final float endSize = 14;
                                final int animationDuration = 600; // Animation duration in ms
                                ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
                                animator.setDuration(animationDuration);
                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        float animatedValue = (float) valueAnimator.getAnimatedValue();
                                        txtJumlah.setTextSize(animatedValue);
                                    }
                                });
                                animator.start();
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
                params.put("act", "order_pending_stepa");
                params.put("itemnumber", itemnumber);
                params.put("varians", varians);
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
                                getdata_jumlahorder ();
                                getdata_totalorder ();
                                final TextView txtJumlah = findViewById(R.id.txtJumlah);
                                final float startSize = 24; // Size in pixels
                                final float endSize = 18;
                                final int animationDuration = 600; // Animation duration in ms
                                ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
                                animator.setDuration(animationDuration);
                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        float animatedValue = (float) valueAnimator.getAnimatedValue();
                                        txtJumlah.setTextSize(animatedValue);
                                    }
                                });
                                animator.start();
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
    private void getdata_jumlahorder () {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String valdata = jsonObject.getString("data");
                            TextView txtJumlah = findViewById(R.id.textJumlah);
                            txtJumlah.setText(valdata);
                           /* ValueAnimator animator = ObjectAnimator.ofFloat(txtJumlah, "2", 14);
                            animator.setDuration(1000);
                            animator.start();*/


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
                params.put("act", "getdata_jumlahorder");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void getdata_totalorder () {
        //loadingbtn.setVisibility(View.VISIBLE);
       // Button btnSimpanTotal = findViewById(R.id.btnSimpan);
        //btnSimpanTotal.setText("Loading...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Integer valdata = jsonObject.getInt("data");
                            String valdata2 = jsonObject.getString("data");
                            if (valdata2.equals("0") || valdata2.equals("")) {
                                jikakosong = "1";
                                /* Button btnSimpanTotal = findViewById(R.id.btnSimpan);
                                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                symbols.setGroupingSeparator(',');
                                symbols.setDecimalSeparator(',');
                                DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                               btnSimpanTotal.setText("Checkout = Rp. 0");*/
                            } else {
                                jikakosong = "0";
                               /*  Button btnSimpanTotal = findViewById(R.id.btnSimpan);
                                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                symbols.setGroupingSeparator(',');
                                symbols.setDecimalSeparator(',');
                                DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                                btnSimpanTotal.setText("Checkout = Rp. "+ decimalFormat.format(valdata));*/
                            }
                            //loadingbtn.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //loadingbtn.setVisibility(View.GONE);
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
                params.put("act", "getdata_totalorder");
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
                                //tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
                                tabLayout.addTab(tabLayout.newTab().setText("Semua Menu"));
                                tabLayout.addTab(tabLayout.newTab().setText("Last Order"));
                                tabLayout.addTab(tabLayout.newTab().setText("Favorite"));
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
    private void cek_varian(final String kodeproduk) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("data").equals("1")) {
                                getdata_varians(kodeproduk);

                            } else {
                                add_order(kodeproduk, "");
                                //Toast.makeText(context, kodeproduk, Toast.LENGTH_SHORT).show();
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
                params.put("act", "cek_varian");
                params.put("kodeproduk", kodeproduk);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    private void getdata_varians(final String kodeproduk2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            ListView listView;
                            AlertDialog.Builder alertDialog = new
                                    AlertDialog.Builder(context);
                            View rowList = getLayoutInflater().inflate(R.layout.list_varian, null);
                            listView = rowList.findViewById(R.id.listView);
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            alertDialog.setView(rowList);
                            final AlertDialog dialog = alertDialog.create();
                            dialog.show();


                            if (array.length() > 0) {

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    adapter.add(object.getString("a"));
                                }

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //Toast.makeText(context, adapter.getItem(position), Toast.LENGTH_SHORT).show();
                                        add_order(kodeproduk2, adapter.getItem(position));
                                       dialog.dismiss();
                                    }
                                });
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
                params.put("act", "getdata_varians");
                params.put("kodeproduk", kodeproduk2);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


}
