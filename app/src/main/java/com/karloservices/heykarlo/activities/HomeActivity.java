package com.karloservices.heykarlo.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.VideoView;

import com.karloservices.heykarlo.R;
import com.karloservices.heykarlo.models.Karlo;
import com.karloservices.heykarlo.models.Response;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                ISpeechRecognitionServerEvents {

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private  VideoView karloVideoView;
    private Uri sphereListening;
    private Uri sphereTalking;
    private boolean talkingToKarlo = false;
    private TextView mUserText;
    private TextView mKarloText;
    private Karlo karlo;


    MicrophoneRecognitionClient micClient = null;

    /**
     * Gets the default locale.
     * @return The default locale.
     */
    private String getDefaultLocale() {
        return "es-mx";
    }

    public String getPrimaryKey() {
        return this.getString(R.string.primaryKey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d("ACTIVITY STARTED", "Successfully HomeActivity onCreate(), on package " + getPackageName() );

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }

        int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }


        sphereListening = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.silence_state);
        sphereTalking   = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.talking_state);
        karloVideoView = (VideoView) this.findViewById(R.id.karloVideoView);

        Log.d("Karlo Sphere State", "Karlo is quiet" );

        mUserText = (TextView) findViewById(R.id.userText);
        mKarloText = (TextView) findViewById(R.id.helperText);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        startRecognizer();
        silenceKarlo();
    }

    public void silenceKarlo() {
        karloVideoView.setVideoURI( sphereListening );
        karloVideoView.start();
        karloVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    public void talkingKarlo() {
        karloVideoView.setVideoURI( sphereTalking );
        karloVideoView.start();
        karloVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }



    private void startRecognizer() {
        Log.i("RECOGNIZER", "STARTING RECOGNIZER...");

        if (this.micClient == null) {
            this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                    this,
                    SpeechRecognitionMode.LongDictation,
                    this.getDefaultLocale(),
                    this,
                    this.getPrimaryKey());
        }

        this.micClient.startMicAndRecognition();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_maps) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_exit) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /// ******************************************************** ///
    /// SPEECH RECOGNITION IMPLEMENTATION METHODS FOR SPEECH API ///
    /// ******************************************************** ///

    @Override
    public void onPartialResponseReceived(String response) {
        String[] words;
        String firstWord;
        Log.i("PARTIAL RESPONSE", response);


        this.silenceKarlo();

        words = response.split(" ", -1);


        Log.d("SR", "words:" + String.valueOf(words));
        Log.d("SR", "words length: " + words.length);

        if(words.length == 1) {

            firstWord = words[0];

            if(firstWord.equals("carlos") || firstWord.equals("carlo") ) {
                this.talkingToKarlo = true;
            }

        } else if(words.length > 1) {


                firstWord = words[0] + " " + words[1];
                Log.d("SR", "firstword: " + firstWord);

                if (firstWord.equals("oye carlo") || firstWord.equals("hey carlo") || firstWord.equals("hey carlos") ||
                        firstWord.equals("oye carlos") || firstWord.equals("hola carlo") || firstWord.equals("hola carlos") ||
                        firstWord.equals("ey carlo") || firstWord.equals("ey carlos"))
                    this.talkingToKarlo = true;

        }

        if(this.talkingToKarlo)
        {
            Log.i("SR", "Esta hablandole a Karlo.");
            mUserText.setText(response);
            mKarloText.setText("Estoy escuchando...");
        }
    }

    private String lastRecordatorio;

    @Override
    public void onFinalResponseReceived(final RecognitionResult response) {
        boolean isFinalDicationMessage = response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout;
        if (micClient != null && isFinalDicationMessage) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            this.micClient.endMicAndRecognition();

            Log.i("STT", "Reiniciando speech to text");
            this.micClient.startMicAndRecognition();
        }

        if(response.Results.length > 0)
        {
            String whatUserSaid = response.Results[0].DisplayText;
            Response answer;

            if(whatUserSaid.toLowerCase().indexOf("verdad carlo".toLowerCase()) != -1 ||
                    (whatUserSaid.toLowerCase().indexOf("verdad carlos".toLowerCase()) != -1 )) {

                Response.sayText("Totalmente cierto", getApplicationContext());
                this.talkingKarlo();


            } else if(whatUserSaid.toLowerCase().indexOf("gracias carlo".toLowerCase()) != -1 ||
                    (whatUserSaid.toLowerCase().indexOf("gracias carlos".toLowerCase()) != -1 )) {

                Response.sayText("Para servirle, usuario", getApplicationContext());
                this.talkingKarlo();


            } else if(whatUserSaid.toLowerCase().indexOf("ultimo recordatorio".toLowerCase()) != -1 ||
                    (whatUserSaid.toLowerCase().indexOf("mis recordatorios".toLowerCase()) != -1 )) {

                Response.sayText("Claro. El unico recordatorio que tiene para mañana es: " + lastRecordatorio, getApplicationContext());
                this.talkingKarlo();

            }  else  if (this.talkingToKarlo) {
                this.talkingToKarlo = false;

                Log.i("FINAL RESPONSE", whatUserSaid);
                answer = karlo.respondTo(whatUserSaid);
                lastRecordatorio = answer.getMessage();

                answer.say(getApplicationContext());
                this.talkingKarlo();

                answer.executeAction(getApplicationContext());

            }
        }

    }

    @Override
    public void onIntentReceived(String s) {

    }

    @Override
    public void onError(int i, String s) {
        Log.wtf("WTF", "onError() SPEECH RECOG");

    }

    @Override
    public void onAudioEvent(boolean b) {

    }


}
