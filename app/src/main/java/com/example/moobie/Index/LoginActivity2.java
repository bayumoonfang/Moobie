package com.example.moobie.Index;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity2 extends AppCompatActivity {
ImageView btnBack;
private EditText txtUsername, txtPassword, txtServer;
Button btnMasuk;
SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login2_activity);
        //overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        btnBack = findViewById(R.id.btnBack);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtServer = findViewById(R.id.txtServer);
        btnMasuk = findViewById(R.id.btnMasuk);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUser = txtUsername.getText().toString().trim();
                String mPass = txtPassword.getText().toString().trim();
                String mServer = txtServer.getText().toString().trim();


                if (!mUser.isEmpty() || !mPass.isEmpty() || !mServer.isEmpty()) {
                    login(mUser, mPass, mServer);
                } else {
                    Toast.makeText(LoginActivity2.this, "Password atau Email Masih Kosong", Toast.LENGTH_SHORT).show();
                    //email.setError("Please insert email");
                    //password.setError("Please insert password");
                }
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity2.this, IndexActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }





    private void login (final String usernameval, final String passwordval, final String serverval) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, publicURL.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("Success");
                    String msgerror = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");
                    if (success.equals("1")){
                        String username = usernameval;
                        String server = serverval;
                        sessionManager = new SessionManager(LoginActivity2.this);
                        sessionManager.createSession(username, server);
                        Intent intent = new Intent(LoginActivity2.this, HomeActivity.class);
                        finish();
                        startActivity(intent);


                    }else{
                        Toast.makeText(LoginActivity2.this, msgerror, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity2.this, "Error3 " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity2.this, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", usernameval);
                params.put("password", passwordval);
                params.put("server", serverval);
                return  params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}




