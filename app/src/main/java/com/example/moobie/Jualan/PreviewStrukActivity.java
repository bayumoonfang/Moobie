package com.example.moobie.Jualan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.print.PrintHelper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moobie.BuildConfig;
import com.example.moobie.Function.ExpandableHeightGridView;
import com.example.moobie.Home.HomeActivity;
import com.example.moobie.R;
import com.example.moobie.SessionManager;
import com.example.moobie.publicURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreviewStrukActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private Context context = PreviewStrukActivity.this;
    String getServer, getDibayar, ordernumb, getOrder;
    RelativeLayout layBack,
            caraCash,
            caraCard,
            caraTransfer;
    ImageView btnShare, btnSimpanStruk;
    EditText txtDibayar;
    TextView txtTanggalJam;
    ListView listView;
    List<PreviewStrukItem> projectList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previewstruk);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getServer = user.get(sessionManager.SERVER);


        Bundle bundle = getIntent().getExtras();
        getDibayar = bundle.getString("dibayar");
        ordernumb = bundle.getString("ordernumb");
        getOrder = bundle.getString("giveorder");
        layBack = findViewById(R.id.layBack);
        btnShare = findViewById(R.id.btnShare);
        projectList = new ArrayList<>();




        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HomeActivity.class);
               // i.putExtra("dibayar",getDibayar);
                //i.putExtra("ordernumb", ordernumb);
                startActivity(i);
            }
        });

        getData();
        getDetailResumeProduk();

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout lay1 = (RelativeLayout) findViewById(R.id.layPrint);
                Bitmap bitmap =getBitmapFromView(lay1);
                try {
                    File file = new File(context.getExternalCacheDir(),"strukmoobi.png");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/png");
                    startActivity(Intent.createChooser(intent, "Share image via"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });





    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(context, CetakStrukActivity.class);
        i.putExtra("dibayar",getDibayar);
        i.putExtra("ordernumb", ordernumb);
        finish();
        startActivity(i);
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        }   else{
            canvas.drawColor(getResources().getColor(R.color.colorWhite));
        }
        view.draw(canvas);
        return returnedBitmap;
    }




    private void getDetailResumeProduk() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    PreviewStrukItem detailTransaksiResumeItem = new PreviewStrukItem(
                                            object.getString("a"),
                                            object.getString("b"),
                                            object.getString("k"),
                                            object.getInt("c")
                                    );
                                    projectList.add(detailTransaksiResumeItem);
                                }
                            } else {

                            }

                            final PreviewStrukAdapter detailTransaksiResumeAdapter = new PreviewStrukAdapter(
                                    projectList, context);
                            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridStruk);
                            gridView.setAdapter(detailTransaksiResumeAdapter);

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
                params.put("ordernumber", ordernumb);
                params.put("act", "get_orderdetail");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


    private void getData() {
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

                            TextView txt1 = (TextView) findViewById(R.id.txtNoFaktur);
                            TextView txt2 = (TextView) findViewById(R.id.txtTanggalFaktur);
                            TextView txt3 = (TextView) findViewById(R.id.txtCustomerFaktur);
                            TextView txt4 = (TextView) findViewById(R.id.txtCaraBayarFaktur);
                            TextView txt5 = (TextView) findViewById(R.id.txtSubTotalFaktur);
                            TextView txt6= (TextView) findViewById(R.id.txtTaxFaktur);
                            TextView txt7 = (TextView) findViewById(R.id.txtServchargeFaktur);
                            TextView txt8 = (TextView) findViewById(R.id.txtTotalFaktur);

                            TextView txt9 = (TextView) findViewById(R.id.txtBayarFaktur);
                            TextView txt10 = (TextView) findViewById(R.id.txtKembalianFaktur);

                            txt1.setText(jsonObject.getString("a"));
                            txt2.setText(jsonObject.getString("d"));
                            txt3.setText(jsonObject.getString("e"));

                            if (jsonObject.getString("b").equals("100")) {
                                txt4.setText("Cash");
                            } else if (jsonObject.getString("b").equals("200")) {
                                txt4.setText("Card");
                            } else if (jsonObject.getString("b").equals("300")) {
                                txt4.setText("Transfer");
                            }


                            int convertedVal = Integer.parseInt(getDibayar);

                            //txt4.setText("Rp."+decimalFormat.format(convertedVal));
                            //txt5.setText("Rp."+decimalFormat.format(convertedVal - jsonObject.getInt("c") ));
                            txt5.setText("Rp."+decimalFormat.format(jsonObject.getInt("c")));
                            txt6.setText("Rp."+decimalFormat.format(jsonObject.getInt("g")));
                            txt7.setText("Rp."+decimalFormat.format(jsonObject.getInt("f")));
                            txt8.setText("Rp."+decimalFormat.format(jsonObject.getInt("h")));
                            txt9.setText("Rp."+decimalFormat.format(convertedVal));
                            txt10.setText("Rp."+decimalFormat.format(convertedVal - jsonObject.getInt("h") ));

                            if (getOrder.equals("1")) {
                                RelativeLayout lay2 = (RelativeLayout) findViewById(R.id.layPrint);
                                Bitmap bitmap =getBitmapFromView(lay2);
                                PrintHelper printHelper = new PrintHelper(context);
                                printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                                printHelper.printBitmap("Print Bitmap", bitmap);


                            }


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
                params.put("ordernumb", ordernumb);
                params.put("act", "getdata_cetakstruk");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }





}
