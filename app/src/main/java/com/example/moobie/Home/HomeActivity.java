package com.example.moobie.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moobie.Jualan.JualanActivity;
import com.example.moobie.Outlet.OutletActivity;
import com.example.moobie.Produk.ProdukActivity;
import com.example.moobie.R;
import com.example.moobie.Report.DailyReportActivity;
import com.example.moobie.SessionManager;
import com.example.moobie.Customer.CustomerActivity;
import com.example.moobie.publicURL;
import com.google.android.material.navigation.NavigationView;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SessionManager sessionManager;
    String getServer, getUsername;
    GridView gridView;
    String [] countries = new String[] {
            "Produk Saya",
            "Outlet",
            "Customer",
            "Today Sale"
    };

    String [] countries2 = new String[] {
            "Kelola produk kamu disini",
            "Kelola semua Outlet ",
            "Management customer ",
            "Laporan penjualan harian"
    };


    int[] flags = new int[]{
            //R.drawable.imgundangan,
            R.drawable.ic_produkblue,
            //R.drawable.imgtur,
            R.drawable.ic_storeblue,
            //R.drawable.imgevent,
            R.drawable.ic_customerblue,
            R.drawable.ic_todaysale
    };

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Context context = HomeActivity.this;
    TextView txtTotalSales, txtNamaToko, txtNamaUser;
    Button btnJualan;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getServer = user.get(sessionManager.SERVER);
        getUsername = user.get(sessionManager.USERNAME);
        toolbar = findViewById(R.id.topLayout1);
        setSupportActionBar(toolbar);

        Calendar c = Calendar.getInstance();
        String[]monthName={"Januari","Februari","Maret", "April", "Mei", "Juni", "Juli",
                "Agustus", "September", "Oktober", "November",
                "Desember"};
        String month=monthName[c.get(Calendar.MONTH)];
        int year=c.get(Calendar.YEAR);
        int date=c.get(Calendar.DATE);


        ////Format formatter = new SimpleDateFormat("MMMM yyyy");
       // String s = formatter.format(new Date());

        TextView textView1 = (TextView) findViewById(R.id.txtDateMonth);
        textView1.setText(month+" "+year);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        btnJualan = findViewById(R.id.btnJualan);




        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        gridView = findViewById(R.id.customgrid);
        txtTotalSales = findViewById(R.id.txtNetsales);
        txtNamaToko = findViewById(R.id.txtNamaToko);


        List<HashMap<String,String>> aList = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<4;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", countries[i]);
            hm.put("txt2", countries2[i]);
            hm.put("flag", Integer.toString(flags[i]) );
            aList.add(hm);
        }

        String[] from = { "flag","txt","txt2"};
        int[] to = { R.id.os_images,R.id.os_texts,R.id.os_texts2};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.grid_iconhome, from, to);
        GridView gridView = (GridView) findViewById(R.id.customgrid);
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItem = parent.getItemAtPosition(position).toString();
                String selectedItem = String.valueOf(position);
                if (selectedItem.equals("0")) {
                    Intent intent = new Intent(context, ProdukActivity.class);
                    startActivity(intent);
                } else if (selectedItem.equals("1")) {
                    Intent intent = new Intent(context, OutletActivity.class);
                    startActivity(intent);
                }else if (selectedItem.equals("2")) {
                    Intent intent = new Intent(context, CustomerActivity.class);
                    startActivity(intent);
                } else if (selectedItem.equals("3")) {
                    Intent intent = new Intent(context, DailyReportActivity.class);
                    startActivity(intent);
                }
                //Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        btnJualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JualanActivity.class);
                startActivity(intent);
            }
        });

        getTotalSales();
        getCMS();
        getUserDetail2();



    }





    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        new StyleableToast
                .Builder(context)
                .text("Tekan sekali lagi untuk keluar aplikasi")
                .textColor(getResources().getColor(R.color.colorWhite))
                .backgroundColor(getResources().getColor(R.color.colorBlack))
                .length(Toast.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }




    private void getCMS() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            String a = object.getString("a");
                            txtNamaToko.setText(a);
                            txtNamaToko.setTextSize(19);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("act", "get_cms");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }




    private void getUserDetail2() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            String a = object.getString("a");
                            NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
                            View headerView = navigationView2.getHeaderView(0);
                            TextView navUsername = (TextView) headerView.findViewById(R.id.textView);
                            navUsername.setText(a);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("username", getUsername);
                params.put("act", "get_userdetail");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }



    private void getTotalSales() {
        txtTotalSales.setText("Loading...");
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
                            txtTotalSales.setText("Rp "+decimalFormat.format(totalcash));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error Reading" + e.toString(), Toast.LENGTH_SHORT).show();
                            txtTotalSales.setText("0");
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
                params.put("act", "today_gettotaltransaksi");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuLogout) {
            sessionManager.logout();
            finish();
        } else if (id == R.id.menuBeranda) {
            Intent intent = new Intent(context, HomeActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture){

    }



}
