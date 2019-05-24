package com.example.faruk.voicechanger.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.MediaExtractor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.faruk.voicechanger.R;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 *** Faruk_Altay 11.11.2018. ***
 */

public class EfektFragment extends Fragment {

    public int soundFile;
    MediaRecorder mRecorder = null;
    String mFileName = null;
    public int deger = 0;
    boolean a=false;
    Random random = new Random();
    public int i=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.efekt,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button button1 = (Button) getView().findViewById(R.id.button1);
        final Button button2 = (Button) getView().findViewById(R.id.button2);
        final Button button3 = (Button) getView().findViewById(R.id.button3);
        final Button button6 = (Button) getView().findViewById(R.id.button4);
        final Button button7 = (Button) getView().findViewById(R.id.button5);
        final Button button8 = (Button) getView().findViewById(R.id.button6);
        final Button button9 = (Button) getView().findViewById(R.id.button8);
        final Button button10 = (Button) getView().findViewById(R.id.button7);
        final Button button11 = (Button) getView().findViewById(R.id.button9);


        button10.setEnabled(false);
        button11.setEnabled(false);




        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.button1:
                        soundFile = R.raw.burp1;
                        playSound1();

                        break;
                    case R.id.button2:
                        soundFile = R.raw.air_horn;
                        playSound1();

                        break;
                    case R.id.button3:
                        soundFile = R.raw.angry_chipmunk;
                        playSound1();

                        break;
                    case R.id.button4:
                        soundFile = R.raw.pew_pew;
                        playSound1();

                        break;
                    case R.id.button5:
                        soundFile = R.raw.silly_snoring;
                        playSound1();

                        break;
                    case R.id.button6:
                        soundFile = R.raw.strange_growl;
                        playSound1();

                        break;
                    case R.id.button8:
                        startRecording();
                        if (deger==1) {
                            button11.setEnabled(true);
                            button9.setEnabled(false);
                        }else { }

                        break;

                    case R.id.button7:
                        playRecording();
                        break;

                    case R.id.button9:
                        stopRecording();
                        button9.setEnabled(true);
                        button10.setEnabled(true);
                        button11.setEnabled(false);
                        break;


                }



            }
        };
        button1.setOnClickListener(Listener);
        button2.setOnClickListener(Listener);
        button3.setOnClickListener(Listener);
        button6.setOnClickListener(Listener);
        button7.setOnClickListener(Listener);
        button8.setOnClickListener(Listener);
        button9.setOnClickListener(Listener);
        button10.setOnClickListener(Listener);
        button11.setOnClickListener(Listener);






    }



    private void playSound1() {
        MediaPlayer player = MediaPlayer.create(this.getActivity(),soundFile );
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

    }


    private void playRecording() {
        MediaPlayer player = new MediaPlayer();

        try {
            player.setDataSource(mFileName);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
        Toast.makeText(getActivity()," Parça Çalınıyor!",Toast.LENGTH_SHORT).show();


    }

    private void startRecording()
    {
        try {

            final EditText dosyaadi = (EditText) getView().findViewById(R.id.editText2);

            String folder_main = "Voice Changer";

            File f = new File(Environment.getExternalStorageDirectory(), folder_main);
            if (!f.exists()) {
                f.mkdirs();
            }
            File f1 = new File(Environment.getExternalStorageDirectory() + "/" + folder_main, "VoiceChanger");
            if (!f1.exists()) {
                f1.mkdirs();
            }




            File dos=new File(Environment.getExternalStorageDirectory()+ "/Voice Changer/VoiceChanger/" + dosyaadi.getText().toString() + ".mp3");
            if(!dos.exists()) {


                if (dosyaadi.getText().toString().length() != 0) {

                    mFileName = Environment.getExternalStorageDirectory() + "/Voice Changer/VoiceChanger/" + dosyaadi.getText().toString() + ".mp3";

                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile(mFileName);
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                    }
                    mRecorder.start();
                    Toast.makeText(getActivity(), "Kayıt Ediliyor...", Toast.LENGTH_SHORT).show();
                    deger = 1;
                } else {

                    Toast.makeText(getActivity(), "Dosya Adını Boş Geçmeyiniz.", Toast.LENGTH_SHORT).show();
                }
            }else{
                final Button button99 = (Button) getView().findViewById(R.id.button8);
                final Button button111 = (Button) getView().findViewById(R.id.button9);

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                if (dosyaadi.getText().toString().length() != 0) {

                                    mFileName = Environment.getExternalStorageDirectory() + "/Voice Changer/VoiceChanger/" + dosyaadi.getText().toString() + ".mp3";

                                    mRecorder = new MediaRecorder();
                                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                                    mRecorder.setOutputFile(mFileName);
                                    try {
                                        mRecorder.prepare();
                                    } catch (IOException e) {
                                    }
                                    mRecorder.start();
                                    Toast.makeText(getActivity(), "Kayıt Ediliyor...", Toast.LENGTH_SHORT).show();
                                    button99.setEnabled(false);
                                    button111.setEnabled(true);
                                } else {

                                    Toast.makeText(getActivity(), "Dosya Adını Boş Geçmeyiniz...", Toast.LENGTH_SHORT).show();
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                button99.setEnabled(true);
                                button111.setEnabled(false);
                                Toast.makeText(getActivity(), "Kayıt İptal Edildi... ", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Aynı İsimde Ses Dosyası Mevcu.Mevcut Ses Dosyasının Üstüne Yazmak İstediğinizden Eminmisiniz?").setPositiveButton("EVET", dialogClickListener).setNegativeButton("HAYIR", dialogClickListener).show();

            }
        }
        catch(Exception e){}
    }


    private void stopRecording() {

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        Toast.makeText(getActivity(), "Ses Kaydı Başarılı.",Toast.LENGTH_SHORT).show();
    }
}




