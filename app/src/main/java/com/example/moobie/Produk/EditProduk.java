package com.example.moobie.Produk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.moobie.R;
import com.example.moobie.SessionManager;
import com.example.moobie.publicURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProduk extends AppCompatActivity {

    Picasso picasso;
    SessionManager sessionManager;
    String getServer, getItemnumber;
    private Context mContext = EditProduk.this;
    ImageView imgProduk, imgBack;
    EditText txtNameProduk, txtHargaProduk;
    TextView txtNamaFile, txtKategori, txtSatuan;
    RelativeLayout layoutHapus, layoutloading, layBack;
    Spinner spinner, spinner2;
    ProgressBar loading;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    int max_resolution_image = 800;
    Bitmap bitmap, decoded;
    String noimage = "0";
Button btnEditproduk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editproduk);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getServer = user.get(sessionManager.SERVER);
        imgProduk = findViewById(R.id.imgUpload);
        Bundle bundle = getIntent().getExtras();
        getItemnumber = bundle.getString("itemnumber");
        txtNameProduk = findViewById(R.id.txtNamaProduk);
        txtHargaProduk = findViewById(R.id.txtHargaProduk);
        layoutHapus = findViewById(R.id.layouthapus);
        imgBack = findViewById(R.id.imgBack);
        spinner = findViewById(R.id.spinnerkategori);
        spinner2 = findViewById(R.id.spinnersatuan);
        layoutloading = findViewById(R.id.layoutLoading);
        btnEditproduk = findViewById(R.id.btnSimpanEditProduk);
        txtKategori = findViewById(R.id.txtValKategori);
        txtSatuan = findViewById(R.id.txtValSatuan);
        layBack = findViewById(R.id.layBack);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(mContext, ProdukActivity.class);
                startActivity(intent);
            }
        });

        layoutHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
                alertDialogBuilder.setMessage("Apakah anda yakin menghapus item ini ?");
                        alertDialogBuilder.setPositiveButton("Iya",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        hapusProduk(getItemnumber);
                                    }
                                });
                alertDialogBuilder.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(mContext, ProdukActivity.class);
                startActivity(intent);
            }
        });

        imgProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                showFileChooser();
            }
        });

        btnEditproduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editproduk(getItemnumber);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinner2Val = parent.getSelectedItem().toString();
                txtKategori.setText(spinner2Val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinner3Val = parent.getSelectedItem().toString();
                txtSatuan.setText(spinner3Val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        getImgProduk();
        getItemDetail();


    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                noimage = "1";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getImgProduk();
            noimage = "0";

        }
    }
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        imgProduk.setImageBitmap(decoded);
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    private void hapusProduk (final String numberitem) {
        layoutloading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("Success");
                    String msgerror = jsonObject.getString("message");
                    if (success.equals("1")){
                        layoutloading.setVisibility(View.GONE);
                        finish();
                        Intent intent = new Intent(mContext, ProdukActivity.class);
                        startActivity(intent);
                    }else{
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(mContext, msgerror, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    layoutloading.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Error3 " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(mContext, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "act_hapusproduk");
                params.put("itemnumber", numberitem);
                return  params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
    private void getItemDetail() {
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
                                    String string1 = object.getString("a");
                                    String string2 = object.getString("b");
                                    txtNameProduk.setText(string1);
                                    txtHargaProduk.setText(string2);
                                    getdata_satuan(object.getString("d"));
                                    getdata_kategori(object.getString("c"));
                                }
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "get_produkdetail");
                params.put("itemnumber", getItemnumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }
    private void getdata_kategori(final String alpa2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<String> categories3 = new ArrayList<String>();
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                categories3.add(""+alpa2+"");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    categories3.add(object.getString("a"));
                                }
                            }

                            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, categories3);
                            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter3);
                            //spinner.setAdapter(new ArrayAdapter<String>(ProdukActivity.this, android.R.layout.simple_spinner_dropdown_item, category));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Error Reading "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Error Reading "+error.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
    private void getdata_satuan(final String alpa) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<String> categories3 = new ArrayList<String>();
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                categories3.add(""+alpa+"");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    categories3.add(object.getString("a"));
                                }
                            }

                            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, categories3);
                            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(dataAdapter3);
                            //spinner.setAdapter(new ArrayAdapter<String>(ProdukActivity.this, android.R.layout.simple_spinner_dropdown_item, category));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "Error Reading "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Error Reading "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("act", "getdata_satuanproduk");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
    private void getImgProduk() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            String a = object.getString("a");

                            if (a.equals("")) {
                                imgProduk.setImageResource(R.drawable.noimage);
                            } else {
                                Picasso.get()
                                        .load("http://duakata-dev.com/moobi/UAT2/media/images/photo/"+a+"")
                                        //.placeholder(R.drawable.user_placeholder)
                                        //.error(R.drawable.user_placeholder_error)
                                        //.centerCrop()
                                        .into(imgProduk);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                params.put("itemnumber", getItemnumber);
                params.put("act", "getimageproduk");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }


    private void editproduk(final String getItemnumberval) {
        layoutloading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("Success");
                    String msgerror = jsonObject.getString("message");

                    if (success.equals("2")) {
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(mContext, msgerror, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (success.equals("3")) {
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(mContext, msgerror, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        layoutloading.setVisibility(View.GONE);
                        //Toast.makeText(context, msgerror, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mContext, ProdukActivity.class);
                        finish();
                        startActivity(i);
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                    layoutloading.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Error3 " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(mContext, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("server", getServer);
                if (noimage.equals("1")) {
                    params.put("image", getStringImage(decoded));
                }
                params.put("noimage", noimage);
                params.put("namaproduk", txtNameProduk.getText().toString());
                params.put("harga", txtHargaProduk.getText().toString());
                params.put("kategori", txtKategori.getText().toString());
                params.put("satuan", txtSatuan.getText().toString());
                params.put("itemnumber", getItemnumberval);
                params.put("act", "edit_produk");
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

}
