package com.example.moobie.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import com.example.moobie.Report.DetailTransaksiResumeActivity;
import com.example.moobie.SessionManager;
import com.example.moobie.publicURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchTransActivity extends AppCompatActivity {

    ImageView img1, img2;
    private Context mContext = SearchTransActivity.this;
    ListView listView;
    List<SearchSalesStoreItem> projectList;
    SessionManager sessionManager;
    String getServer, getBranch, getSearch, getStore;
    TextView textView;
    RelativeLayout notFound,loading;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchtrans);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();

        //listView = findViewById(R.id.listview_detailsearchtrans);
        projectList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        getBranch = bundle.getString("branch");
        getServer = bundle.getString("server");
        getSearch = bundle.getString("search");
        getStore = bundle.getString("storename");
        textView = findViewById(R.id.txttop2);
        notFound = findViewById(R.id.notFound);
        loading = findViewById(R.id.loading);
        img2 = findViewById(R.id.imgFilter);

        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


        img1 = findViewById(R.id.backHome);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context wrapper = new ContextThemeWrapper(mContext, R.style.PopUpsearch);
                        PopupMenu popup = new PopupMenu(wrapper, img2);
                        popup.getMenuInflater().inflate(R.menu.popup_filtersearch, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                 @Override
                                 public boolean onMenuItemClick(MenuItem item) {
                                     //Toast.makeText(mContext,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                     getTransstore2(item.getTitle().toString());
                                     return true;
                                 }
                             });
                        popup.show();

                    }
                });

        getTransstore();

        ExpandableHeightGridView expandableHeightGridView = findViewById(R.id.gridPilihan5);
        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtVal1 = view.findViewById(R.id.txtTransOrderNumb2);
                String text = txtVal1.getText().toString();
                Intent i = new Intent(mContext, DetailTransaksiResumeActivity.class);
                i.putExtra("branch",getBranch);
                i.putExtra("server",getServer);
                i.putExtra("ordernumber",text);

                startActivity(i);
            }
        });
    }




    private void getTransstore() {
        loading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            loading.setVisibility(View.GONE);
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    SearchSalesStoreItem searchSalesStoreItem = new SearchSalesStoreItem(
                                            object.getInt("a"),
                                            object.getString("b"),
                                            object.getString("c")
                                    );
                                    projectList.add(searchSalesStoreItem);
                                }
                            } else {
                                notFound.setVisibility(View.VISIBLE);
                            }

                            final SearchSalesStoreListAdapter searchSalesStoreListAdapter = new SearchSalesStoreListAdapter(
                                    projectList, mContext);
                            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridPilihan5);
                            gridView.setAdapter(searchSalesStoreListAdapter);
                            //listView.setAdapter(searchSalesStoreListAdapter);
                            //listView.setTextFilterEnabled(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        //Toast.makeText(mContext, "Error Reading"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "gettransstore_search");
                params.put("branch", getBranch);
                params.put("search", getSearch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }




    private void getTransstore2( final String filterku) {
        loading.setVisibility(View.VISIBLE);
       // listView.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            loading.setVisibility(View.GONE);

                            final SearchSalesStoreListAdapter searchSalesStoreListAdapter = new SearchSalesStoreListAdapter(
                                    projectList, mContext);
                            projectList.clear();
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    SearchSalesStoreItem searchSalesStoreItem = new SearchSalesStoreItem(
                                            object.getInt("a"),
                                            object.getString("b"),
                                            object.getString("c")
                                    );
                                    projectList.add(searchSalesStoreItem);
                                }
                            } else {
                                notFound.setVisibility(View.VISIBLE);
                            }
                            searchSalesStoreListAdapter.notifyDataSetChanged();
                            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridPilihan5);
                            gridView.setAdapter(searchSalesStoreListAdapter);
                            gridView.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            //gridView.setVisibility(View.VISIBLE);
                            Toast.makeText(mContext, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        //listView.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "Error Reading"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "gettransstore_search");
                params.put("filter", filterku);
                params.put("branch", getBranch);
                params.put("search", getSearch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }



}
