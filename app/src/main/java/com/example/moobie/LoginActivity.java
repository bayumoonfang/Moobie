package com.example.moobie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moobie.Home.ResumeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private EditText usernameVal, passwordVal, serverVal;
    private Button btnLogin;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);



       usernameVal = findViewById(R.id.usernameVal);
        passwordVal = findViewById(R.id.passwordVal);
        serverVal = findViewById(R.id.serverVal);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUser = usernameVal.getText().toString().trim();
                String mPass = passwordVal.getText().toString().trim();
                String mServer = serverVal.getText().toString().trim();


                if (!mUser.isEmpty() || !mPass.isEmpty() || !mServer.isEmpty()){
                    login(mUser,mPass,mServer);
                }else{
                    Toast.makeText(LoginActivity.this, "Password atau Email Masih Kosong", Toast.LENGTH_SHORT).show();
                    //email.setError("Please insert email");
                    //password.setError("Please insert password");
                }
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
                            sessionManager = new SessionManager(LoginActivity.this);
                            sessionManager.createSession(username, server);
                            Intent intent = new Intent(LoginActivity.this, ResumeActivity.class);
                            finish();
                            startActivity(intent);


                    }else{
                        Toast.makeText(LoginActivity.this, msgerror, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error3 " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
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
