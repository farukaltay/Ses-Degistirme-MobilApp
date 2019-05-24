package com.example.faruk.voicechanger.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faruk.voicechanger.Activitys.VideoSesYakalamaActivity;
import com.example.faruk.voicechanger.R;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *** Faruk_Altay 11.11.2018. ***
 */

public class SesKaydetFragment extends Fragment {

    FFmpeg ffmpeg;
    MediaRecorder nRecorder;
    MediaPlayer nPlayer;
    String cikisDosyasi =null;
    double hiz=0.0;
    int deger=0;
    ProgressDialog progressDoalog;
    public String uzanti;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.ses_kayit,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button btnKayitBaslat = (Button) getView().findViewById(R.id.btnKayitBaslat);
        final Button btnKayitDurdur = (Button) getView().findViewById(R.id.btnKayitDurdur);
        final Button btnOynat = (Button) getView().findViewById(R.id.btnOynat);
        final EditText ed = (EditText) getView().findViewById(R.id.etDosyaadi);
        String folder_main = "Voice Changer";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File f1 = new File(Environment.getExternalStorageDirectory() + "/" + folder_main, "VoiceChanger");
        if (!f1.exists()) {
            f1.mkdirs();
        }
        btnKayitDurdur.setEnabled(false);
        btnOynat.setEnabled(false);

        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.btnKayitBaslat:
                        kaydet();
                     if(deger==1) {
                         btnKayitDurdur.setEnabled(true);
                         btnKayitBaslat.setEnabled(false);
                         btnOynat.setEnabled(false);
                     }

                        break;
                    case R.id.btnKayitDurdur:
                        btnKayitDurdur();

                        btnKayitBaslat.setEnabled(true);
                        btnKayitDurdur.setEnabled(false);
                        btnOynat.setEnabled(true);
                        break;
                    case R.id.btnOynat:
                        btnOynat();
                        btnKayitBaslat.setEnabled(true);
                        btnKayitDurdur.setEnabled(false);

                        break;


                }

            }
        };
        btnKayitBaslat.setOnClickListener(Listener);
        btnKayitDurdur.setOnClickListener(Listener);
        btnOynat.setOnClickListener(Listener);
    }

    public void kaydet(){

        try {
            final RadioButton mp3 = (RadioButton) getView().findViewById(R.id.rbmp3);
            final RadioButton wav = (RadioButton) getView().findViewById(R.id.rbwav);
            final EditText dosyaadi = (EditText) getView().findViewById(R.id.etDosyaadi);
            final EditText hizi=(EditText)getView().findViewById(R.id.editText4);
            if (mp3.isChecked()) {
                uzanti = ".mp3";
            } else if (wav.isChecked()) {
                uzanti = ".wav";
            } else {
                uzanti = ".mp3";
            }
            hiz=Double.parseDouble(hizi.getText().toString());
            if(hiz>=0.5 && hiz<=2.0) {
                File dos = new File(Environment.getExternalStorageDirectory() + "/Voice Changer/VoiceChanger/" + dosyaadi.getText().toString() + uzanti);
                if (!dos.exists()) {
                    if (dosyaadi.getText().toString().length() != 0) {
                        cikisDosyasi = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Voice Changer/VoiceChanger/" + dosyaadi.getText().toString() + uzanti;
                        nRecorder = new MediaRecorder();
                        nRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        nRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        nRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        nRecorder.setOutputFile(cikisDosyasi);
                        deger = 1;

                        try {
                            nRecorder.prepare();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        nRecorder.start();

                        Toast.makeText(getActivity(), "Kayıt başlıyor...", Toast.LENGTH_SHORT).show();

                    } else {

                        deger = 0;
                        Toast.makeText(getActivity(), "Dosya adını boş geçmeyiniz.", Toast.LENGTH_SHORT).show();

                    }
                } else {


                    final Button btnkayitdurdur = (Button) getView().findViewById(R.id.btnKayitDurdur);
                    final Button btnkayitbaslat = (Button) getView().findViewById(R.id.btnKayitBaslat);
                    final Button btnoynat = (Button) getView().findViewById(R.id.btnOynat);

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:

                                    if (dosyaadi.getText().toString().length() != 0) {

                                        cikisDosyasi = Environment.getExternalStorageDirectory() + "/Voice Changer/VoiceChanger/" + dosyaadi.getText().toString() + ".mp3";

                                        nRecorder = new MediaRecorder();
                                        nRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                        nRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                        nRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                                        nRecorder.setOutputFile(cikisDosyasi);

                                        try {
                                            nRecorder.prepare();
                                        } catch (IOException e) {
                                        }
                                        nRecorder.start();
                                        Toast.makeText(getActivity(), "Kayıt Ediliyor...", Toast.LENGTH_SHORT).show();
                                        btnkayitdurdur.setEnabled(true);
                                        btnkayitbaslat.setEnabled(false);
                                        btnoynat.setEnabled(false);


                                    } else {

                                        Toast.makeText(getActivity(), "Dosya Adını Boş Geçmeyiniz...", Toast.LENGTH_SHORT).show();
                                    }

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    deger = 0;
                                    Toast.makeText(getActivity(), "Kayıt İptal Edildi... ", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Aynı İsimde Ses Dosyası Mevcu.Mevcut Ses Dosyasının Üstüne Yazmak İstediğinizden Eminmisiniz?").setPositiveButton("EVET", dialogClickListener).setNegativeButton("HAYIR", dialogClickListener).show();
                    deger = 0;
                }
            }else
                {
                Toast.makeText(getActivity(), "Ses Hızını 0.5 İle 2.0 Arasında Seçiniz.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}
    }

    public void btnKayitDurdur()
    {

            nRecorder.stop();
            nRecorder.release();
            nRecorder = null;
            final EditText dosyaadi = (EditText) getView().findViewById(R.id.etDosyaadi);


        File dos = new File(Environment.getExternalStorageDirectory() + "/Voice Changer/VoiceChanger/" + dosyaadi.getText().toString() + uzanti);
        File dos2 = new File(Environment.getExternalStorageDirectory() + "/Voice Changer/VoiceChanger/" + dosyaadi.getText().toString()+".mp3");

        dos.renameTo(dos2);

        String yol2=Environment.getExternalStorageDirectory()+ "/Voice Changer/VoiceChanger/"+dosyaadi.getText().toString()+".mp3";
        String yol4=Environment.getExternalStorageDirectory()+ "/Voice Changer/VoiceChanger/"+dosyaadi.getText().toString()+".mp3";
        final String[] seshızı = {"-y", "-i", yol2,"-filter:a","atempo="+hiz,"-vn", yol4}; //0.5 ile 2.0 arasında değer olmalı

        loadFFMpegBinary();
        execFFmpegBinary(seshızı);

    }

    public void btnOynat(){
        btnDurdur();

            nPlayer =new MediaPlayer();
            try {
                nPlayer.setDataSource(cikisDosyasi);
                nPlayer.prepare();
                nPlayer.start();
                Toast.makeText(getActivity(),"Kayıt oynatılıyor...",Toast.LENGTH_SHORT).show();
                nPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

    public void btnDurdur(){

        if(nPlayer!=null){

            nPlayer=null;

        }
    }
    private void execFFmpegBinary(final String[] command) {

        try {

            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                @Override
                public void onFailure(String s) {
                    Toast.makeText(getActivity(),"Bir Hata Oluştu!",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(String s) {
                    final EditText dosyaadi = (EditText) getView().findViewById(R.id.etDosyaadi);


                    File dos = new File(Environment.getExternalStorageDirectory() + "/Voice Changer/VoiceChanger/" + dosyaadi.getText().toString() + uzanti);
                    File dos2 = new File(Environment.getExternalStorageDirectory() + "/Voice Changer/VoiceChanger/" + dosyaadi.getText().toString()+".mp3");

                    dos2.renameTo(dos);

                    Toast.makeText(getActivity(),"Ses Dosyası Başarı İle Oluşturuldu!",Toast.LENGTH_SHORT).show();
                    progressDoalog.cancel();
                }


                @Override
                public void onProgress(String s) {
                    progressDoalog.show();


                }


                @Override
                public void onStart() {
                    progressDoalog = new ProgressDialog(getActivity());
                    progressDoalog.setMessage("Ses Dosyası Oluşturuluyor...");
                    progressDoalog.setTitle("Durum");
                    progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDoalog.setCancelable(false);

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
                ffmpeg = FFmpeg.getInstance(getActivity());

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
