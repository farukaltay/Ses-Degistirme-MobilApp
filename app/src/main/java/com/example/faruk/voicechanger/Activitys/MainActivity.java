package com.example.faruk.voicechanger.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.faruk.voicechanger.Fragments.DosyalarFragment;
import com.example.faruk.voicechanger.Fragments.SesKaydetFragment;
import com.example.faruk.voicechanger.Fragments.EfektFragment;
import com.example.faruk.voicechanger.Fragments.HakkindaFragment;
import com.example.faruk.voicechanger.Fragments.SesiDegistirFragment;
import com.example.faruk.voicechanger.Fragments.TTSFragment;
import com.example.faruk.voicechanger.R;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

/**
 *** Faruk_Altay 11.11.2018. ***
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    FFmpeg ffmpeg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SesiDegistirFragment sesiDegistirFragment = new SesiDegistirFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flcontent,sesiDegistirFragment)
                .addToBackStack(null)
                .commit();


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
       // getMenuInflater().inflate(R.menu.main, menu);
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
        int id = item.getItemId();
        if (id == R.id.nav_anasayfa) {
            DosyalarFragment dosyalarFragment = new DosyalarFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flcontent,dosyalarFragment)
                    .addToBackStack(null)
                    .commit();
        }
        if (id == R.id.nav_ses_degistir) {
            SesiDegistirFragment sesiDegistirFragment = new SesiDegistirFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flcontent,sesiDegistirFragment)
                    .addToBackStack(null)
                    .commit();
        }


        if (id == R.id.nav_ses_kayit) {
            SesKaydetFragment anaFragment = new SesKaydetFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flcontent,anaFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else if (id==R.id.nav_tts){
            TTSFragment ttsFragment = new TTSFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flcontent,ttsFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else if (id==R.id.nav_efekt){
            EfektFragment efektFragment = new EfektFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flcontent,efektFragment)
                    .addToBackStack(null)
                    .commit();

        }
        if (id == R.id.nav_video_ses_yakalama) {

            startActivity(new Intent(MainActivity.this,VideoSesYakalamaActivity.class));

        }
        else if (id == R.id.nav_hakkinda) {
            HakkindaFragment hakkindaFragment = new HakkindaFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flcontent,hakkindaFragment)
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void execFFmpegBinary(final String[] command) {

        try {

            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                @Override
                public void onFailure(String s) {
                }

                @Override
                public void onSuccess(String s) {

                }


                @Override
                public void onProgress(String s) {


                }


                @Override
                public void onStart() {


                }


                @Override
                public void onFinish() {


                }

            });

        } catch (FFmpegCommandAlreadyRunningException e) {
        }

    }

    private void loadFFMpegBinary() {

        try {

            if (ffmpeg == null) {
                ffmpeg = FFmpeg.getInstance(MainActivity.this);

            }

            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override public void onFailure() {

                }
                @Override public void onSuccess() {
                }

            });

        } catch (FFmpegNotSupportedException e) {

        } catch (Exception e) {}

    }


}
