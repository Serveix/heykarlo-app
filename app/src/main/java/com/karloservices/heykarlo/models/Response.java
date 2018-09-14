package com.karloservices.heykarlo.models;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.karloservices.heykarlo.R;
//import com.microsoft.speech.tts.Synthesizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Set;

//import com.microsoft.speech.tts.Voice;

/**
 * Created by eli on 11/13/17.
 */

public class Response extends AppCompatActivity {
    private String type;
    private String action;
    private String message;
    private String info;
    private static TextToSpeech tts;
    private static final Locale locSpanishMexico = new Locale("spa", "MX");
    private Voice maleVoice;

    public Response() {}

    public Response(String type, String action, String message, String info) {
        this.type   = type;
        this.action = action;
        this.message = message;
        this.info = info;
    }
    public void setMessage(String answer) { this.message = message; }
    public String getMessage() { return message; }

    public void say(Context appContext)
    {
        tts = new TextToSpeech(appContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(locSpanishMexico);

                    Log.v("TTS", "Text to speech locale set to SPANISH MEXICO");
                    Log.d("TTS", "Starting to say: " + message);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak(message, TextToSpeech.QUEUE_ADD, null, null);
                    } else {
                        // since this one is depracated from lollipop
                        tts.speak(message, TextToSpeech.QUEUE_ADD, null);
                    }
                    Log.i("TTS", "Said the answer successfully");

                }


            }
        });

    }

    public static void sayText(final String text, Context appContext)
    {
        tts = new TextToSpeech(appContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(locSpanishMexico);

                    Log.v("TTS", "Text to speech locale set to SPANISH MEXICO");
                    Log.d("TTS", "Starting to say: " + text);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
                    } else {
                        // since this one is depracated from lollipop
                        tts.speak(text, TextToSpeech.QUEUE_ADD, null);
                    }
                    Log.i("TTS", "Said the answer successfully");

                }


            }
        });

    }

    public void executeAction(final Context context) {
        if(action != null)
        {
            switch(action) {
                case "waze":
                    try
                    {
                        // Launch Waze to look for Hawaii:
                        String url = "https://waze.com/ul?q="+info+"&navigate=yes";
                        Log.d("ACTIVITY START", "Iniciando intent con url: " + Uri.parse(url) );
                        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );

                        context.startActivity(intent);
                    }
                    catch ( ActivityNotFoundException ex  )
                    {
                        // If Waze is not installed, open it in Google Play:
                        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                        context.startActivity(intent);
                    }
                    break;
                case "call":
                    try
                    {
                            Log.d("SR", "Intentando llamar");
                            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+5218118663382"));
                            context.startActivity(callIntent);
                    } catch (ActivityNotFoundException ex){
                        Log.e("ERROR CALL", ex.getMessage());
                    }
                    catch (SecurityException ex){
                        Log.e("ERROR CALL", ex.getMessage());
                    }
                    break;
                }
            }
        }
    }