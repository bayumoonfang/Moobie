package com.example.moobie.Produk;

import android.content.Context;
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
import com.example.moobie.R;
import com.example.moobie.SessionManager;
import com.example.moobie.publicURL;

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

public class AddProdukActivity extends AppCompatActivity {
    private Context context = AddProdukActivity.this;
    ImageView imgUpload, imgBack;
    Button btnBrowse, btnUpload;
    TextView txtNamaFile, txtKategori, txtSatuan;
    Bitmap bitmap, decoded;
    int success;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    String getServer; // image quality 1 - 100;
    int max_resolution_image = 800;
    Intent intent;
    Uri fileUri;
    private Uri filePath;
    EditText txtNameProduk, txtHargaProduk;
    RelativeLayout layoutloading, layBack;
    Spinner spinner, spinner2;
    SessionManager sessionManager;

    private static final int STORAGE_PERMISSION_CODE = 123;
    String tag_json_obj = "json_obj_req";
    String noimage = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduk);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String getEmail =  user.get(sessionManager.USERNAME);
        getServer = user.get(sessionManager.SERVER);
        imgUpload = findViewById(R.id.imgUpload);
        spinner = findViewById(R.id.spinnerkategori);
        spinner2 = findViewById(R.id.spinnersatuan);
        txtKategori = findViewById(R.id.txtValKategori);
        txtSatuan = findViewById(R.id.txtValSatuan);
        imgBack = findViewById(R.id.imgBack);
        txtNameProduk = findViewById(R.id.txtNamaProduk);
        txtHargaProduk = findViewById(R.id.txtHargaProduk);
        layoutloading = findViewById(R.id.layoutLoading);
        layBack = findViewById(R.id.layBack);

        btnUpload = findViewById(R.id.btnSimpanAddProduk);
        //Upload_Btn.setOnClickListener(this);
        getdata_kategori();
        getdata_satuan();
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                showFileChooser();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtKategori.getText().toString().equals("Pilih Kategori")){
                    Toast.makeText(context, "Kategori Belum Dipilih", Toast.LENGTH_SHORT).show();
                    return;
                } else if (txtSatuan.getText().toString().equals("Pilih Satuan")){
                    Toast.makeText(context, "Satuan Belum Dipilih", Toast.LENGTH_SHORT).show();
                    return;
                } else if (txtNameProduk.getText().toString().equals("")){
                    Toast.makeText(context, "Nama produk masih kosong", Toast.LENGTH_SHORT).show();
                    return;
                } else if (txtHargaProduk.getText().toString().equals("")){
                    Toast.makeText(context, "Harga tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    simpanproduk();
                }

            }
        });



        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(context, ProdukActivity.class);
                startActivity(intent);
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
                txtKategori.setText("");
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
                txtKategori.setText("");
            }
        });

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
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                noimage = "1";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imgUpload.setImageResource(R.drawable.noimage);
            noimage = "0";

        }
    }
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        imgUpload.setImageBitmap(decoded);
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



    private void simpanproduk () {
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
                        Toast.makeText(context, msgerror, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (success.equals("3")) {
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(context, msgerror, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        layoutloading.setVisibility(View.GONE);
                        //Toast.makeText(context, msgerror, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, ProdukActivity.class);
                        finish();
                        startActivity(i);
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                    layoutloading.setVisibility(View.GONE);
                    Toast.makeText(context, "Error3 " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        layoutloading.setVisibility(View.GONE);
                        Toast.makeText(context, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("act", "add_produk");
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    private void getdata_kategori() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<String> categories2 = new ArrayList<String>();
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                categories2.add("Pilih Kategori");
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
    private void getdata_satuan() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.GET_TRANSAKSIHOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<String> categories3 = new ArrayList<String>();
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                categories3.add("Pilih Satuan");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    categories3.add(object.getString("a"));
                                }
                            }

                            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories3);
                            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(dataAdapter3);
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
                params.put("act", "getdata_satuanproduk");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


/*
    private void selectImage() {
        imgUpload.setImageResource(0);
        final CharSequence[] items = {"Camera", "Gallery",
                "Keluar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddProdukActivity.this);
        builder.setTitle("Tambahkan Photo");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);

                } else if (items[item].equals("Gallery")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals("Keluar")) {
                    dialog.dismiss();
                    imgUpload.setImageResource(R.drawable.noimage);
                }
            }
        });
builder.show();
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }



        if(isReadStorageAllowed()){

        } else {
            try {

                String path = getPath(filePath);
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
                        .addFileToUpload(path, "image") //Adding file
                        .setMethod("POST")
                        .addParameter("server", getServer) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        requestStoragePermission();





    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }


        //If permission is not granted returning false

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                //Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                //Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA  && resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    //imgUpload.setImageBitmap(photo);
                    setToImageView(getResizedBitmap(photo, max_resolution_image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    // mengambil gambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(AddProdukActivity.this.getContentResolver(), data.getData());
                    filePath = data.getData();
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            imgUpload.setImageResource(R.drawable.noimage);
        }
    }



    // Untuk menampilkan bitmap pada ImageView
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView

        imgUpload.setImageBitmap(decoded);
    }

    // Untuk resize bitmap
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
*/



}
