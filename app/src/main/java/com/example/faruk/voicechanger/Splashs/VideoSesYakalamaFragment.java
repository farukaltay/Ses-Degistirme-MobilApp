package com.example.faruk.voicechanger.Splashs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.faruk.voicechanger.R;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

/**
 *** Faruk_Altay 11.11.2018. ***
 */

public class VideoSesYakalamaFragment extends Fragment {

    FFmpeg ffmpeg;
    public String filePath;
    public String yol;


    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_ses_yakalama,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final EditText editText=(EditText)getView().findViewById(R.id.editText3);

        final Button btnDosyaSec = (Button) getView().findViewById(R.id.button);
        final Button btnSesiKaydet = (Button) getView().findViewById(R.id.button10);

        btnDosyaSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermissionsAndOpenFilePicker();


            }
        });

        btnSesiKaydet.setOnClickListener(new View.OnClickListener() {
            final String cikisDosyasi = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Voice Changer/VoiceChanger/"+editText.getText().toString()+".mp3";

            final  String[] complexCommand= {"-y", "-i", yol, "-vn", "-ar", "44100", "-ac", "2", "-b:a", "256k", "-f", "mp3", cikisDosyasi};

            @Override
            public void onClick(View v) {
                loadFFMpegBinary();
                execFFmpegBinary(complexCommand);


            }

        });

    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(getActivity())
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withFilter(Pattern.compile(".*\\.jpg$")) // Filtreleme
                .withFilterDirectories(true)
                .withHiddenFiles(true)
                .withTitle("Video Seçimi")
                .start();
    }



   @Override
      public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
           String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

           if (path != null) {
               Log.d("Yol: ", path);
               Toast.makeText(getActivity(), "Seçilen Dosya: " + path, Toast.LENGTH_LONG).show();
           }
       }
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




    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    private void showError() {
        Toast.makeText(getActivity(), "Veri okumaya izin ver.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {
                    showError();
                }
            }
        }
    }







}
