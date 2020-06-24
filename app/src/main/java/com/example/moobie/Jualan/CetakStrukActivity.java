package com.example.moobie.Jualan;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.example.moobie.Home.HomeActivity;
import com.example.moobie.R;
import com.example.moobie.SessionManager;
import com.example.moobie.publicURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CetakStrukActivity extends AppCompatActivity {

    SessionManager sessionManager;
    private Context context = CetakStrukActivity.this;
    String getServer, getDibayar, ordernumb;
    RelativeLayout layBack,
            caraCash,
            caraCard,
            caraTransfer;
    Button btnJualan, btnSimpanStruk, btnCetakStruk;
    EditText txtDibayar;
    TextView txtTanggalJam;
    ImageView imgClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetakstruk);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getServer = user.get(sessionManager.SERVER);
        txtTanggalJam = findViewById(R.id.txt3);
        btnJualan = findViewById(R.id.btnJualan);
        imgClose = findViewById(R.id.imgClose);
        btnSimpanStruk = findViewById(R.id.button2);
        btnCetakStruk = findViewById(R.id.button1);


        Bundle bundle = getIntent().getExtras();
        getDibayar = bundle.getString("dibayar");
        ordernumb = bundle.getString("ordernumb");



        MediaPlayer mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.coin);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(false);
        //mMediaPlayer.start();


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String[]monthName={"Januari","Februari","Maret", "April", "Mei", "Juni", "Juli",
                "Agustus", "September", "Oktober", "November",
                "Desember"};
        String month=monthName[c.get(Calendar.MONTH)];
        int year=c.get(Calendar.YEAR);
        int date=c.get(Calendar.DATE);

        txtTanggalJam.setText(date+"-"+month+"-"+year+", "+formattedDate);
        btnJualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, JualanActivity.class);
                finish();
                startActivity(i);
            }
        });



        btnCetakStruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PreviewStrukActivity.class);
                i.putExtra("dibayar",getDibayar);
                i.putExtra("ordernumb", ordernumb);
                i.putExtra("giveorder", "1");
                startActivity(i);
            }
        });


        btnSimpanStruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PreviewStrukActivity.class);
                i.putExtra("dibayar",getDibayar);
                i.putExtra("ordernumb", ordernumb);
                i.putExtra("giveorder", "2");
                startActivity(i);

            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HomeActivity.class);
                finish();
                startActivity(i);
            }
        });
        getData();
    }


    @Override
    public void onBackPressed(){
        Intent i = new Intent(context, JualanActivity.class);
        finish();
        startActivity(i);
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

                            TextView txt1 = (TextView) findViewById(R.id.txtr1);
                            TextView txt2 = (TextView) findViewById(R.id.txtr2);
                            TextView txt3 = (TextView) findViewById(R.id.txtr3);
                            TextView txt4 = (TextView) findViewById(R.id.txtr4);
                            TextView txt5 = (TextView) findViewById(R.id.txtr5);

                            txt1.setText(jsonObject.getString("a"));
                            if (jsonObject.getString("b").equals("100")) {
                                txt2.setText("Cash");
                            } else if (jsonObject.getString("b").equals("200")) {
                                txt2.setText("Card");
                            } else if (jsonObject.getString("b").equals("300")) {
                                txt2.setText("Transfer");
                            }

                            txt3.setText("Rp."+decimalFormat.format(jsonObject.getInt("h")));
                            int convertedVal = Integer.parseInt(getDibayar);

                            txt4.setText("Rp."+decimalFormat.format(convertedVal));
                            txt5.setText("Rp."+decimalFormat.format(convertedVal - jsonObject.getInt("h") ));

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
