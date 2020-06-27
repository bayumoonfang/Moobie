package com.example.moobie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.moobie.Home.HomeActivity;
import com.example.moobie.Index.IndexActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;

    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;
///
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String USERNAME = "USERNAME";
    public static final String SERVER = "SERVER";

    public  SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String server){
        editor.putBoolean(LOGIN,true);
        editor.putString(USERNAME, name);
        editor.putString(SERVER, server);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLoggin()){
            Intent i = new Intent(context, IndexActivity.class);
            context.startActivity(i);
            ((HomeActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(SERVER, sharedPreferences.getString(SERVER, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, IndexActivity.class);
        context.startActivity(i);
        ((HomeActivity) context).finish();
    }
}
