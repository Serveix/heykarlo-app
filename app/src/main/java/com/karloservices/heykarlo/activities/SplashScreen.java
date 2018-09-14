package com.karloservices.heykarlo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.karloservices.heykarlo.R;
import com.karloservices.heykarlo.activities.StartMenuActivity;
import com.loopj.android.http.PersistentCookieStore;

import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

public class SplashScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread karloThread = new Thread(){
            @Override
            public void run() {
                PersistentCookieStore mCookieStore;
                boolean logguedIn = false;

                try {

                    // loader to show off
                    sleep(2000);

                    mCookieStore = new PersistentCookieStore( getApplicationContext() );

                    List<Cookie> cookies = mCookieStore.getCookies();

                    for (Cookie c : cookies) {

                        Log.d("COOKIE CHECK", "Cookie name: " + c.getName() + " y su contenido: " + c.getValue() );

                        if (c.getName().equals("session_token")) {
                            Log.d("USER LOGIN", "Usuario con sesion iniciada, iniciando HomeActivity");
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                            logguedIn = true;
                        }
                    }

                    if (!logguedIn) {
                        Log.d("USER LOGIN", "Usuario sin sesion iniciada, iniciando StartMenuActivity");
                        startActivity(new Intent(getApplicationContext(), StartMenuActivity.class));
                        finish();
                    }




                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        karloThread.start();


    }
}
