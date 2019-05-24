package com.example.faruk.voicechanger.Activitys;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.faruk.voicechanger.R;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import java.io.File;
import java.util.regex.Pattern;

/**
 *** Faruk_Altay 11.11.2018. ***
 */

public class VideoSesYakalamaActivity extends AppCompatActivity {



    public String yol;
    String cikisDosyasi=null;
    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    FFmpeg ffmpeg=null;
    ProgressDialog progressDoalog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.video_ses_yakalama);



        final Button btnDosyaSec = (Button) findViewById(R.id.button);
        final Button btnSesiKaydet = (Button) findViewById(R.id.button10);

        btnDosyaSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                checkPermissionsAndOpenFilePicker();

            }
        });
        btnSesiKaydet.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                final  EditText editText=(EditText)findViewById(R.id.editText3);

                    if (editText.getText().length() != 0)
                    {
                        if (yol != null)
                        {
                           final File dos=new File(Environment.getExternalStorageDirectory()+ "/Voice Changer/VoiceChanger/" + editText.getText().toString() + ".mp3");
                            if(!dos.exists())
                            {

                                cikisDosyasi = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Voice Changer/VoiceChanger/" + editText.getText().toString() + ".mp3";
                                final String[] complexCommand = {"-y", "-i", yol, "-vn", "-ar", "44100", "-ac", "2", "-b:a", "256k", "-f", "mp3", cikisDosyasi};

                                loadFFMpegBinary();
                                execFFmpegBinary(complexCommand);

                                Toast.makeText(getApplicationContext(), editText.getText().toString() + "'Ses Dosyası Çıkartılıyor!", Toast.LENGTH_SHORT).show();

                            }else{


                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            switch (which)
                                            {
                                                case DialogInterface.BUTTON_POSITIVE:

                                                            EditText ed=(EditText)findViewById(R.id.editText3) ;
                                                            cikisDosyasi = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Voice Changer/VoiceChanger/" + ed.getText().toString() + ".mp3";
                                                            final String[] complexCommand = {"-y", "-i", yol, "-vn", "-ar", "44100", "-ac", "2", "-b:a", "256k", "-f", "mp3", cikisDosyasi};
                                                            loadFFMpegBinary();
                                                            execFFmpegBinary(complexCommand);
                                                            Toast.makeText(getApplicationContext(), ed.getText().toString() + "'Ses Dosyası Çıkartılıyor!", Toast.LENGTH_SHORT).show();




                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    Toast.makeText(getApplicationContext(), "Üzerine Yazma İptal Edildi... ", Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(VideoSesYakalamaActivity.this);
                                    builder.setMessage("Aynı İsimde Ses Dosyası Mevcut Ses Dosyasının Üstüne Yazmak İstediğinizden Eminmisiniz?").setPositiveButton("EVET", dialogClickListener).setNegativeButton("HAYIR", dialogClickListener).show();
                                }



                        } else
                            {
                            Toast.makeText(getApplicationContext(), "Video Seçimi Yapmadınız!", Toast.LENGTH_SHORT).show();

                        }
                    } else
                        {
                        Toast.makeText(getApplicationContext(), "Ses Dosyası Adını Boş Geçmeyiniz!", Toast.LENGTH_SHORT).show();
                    }
                }

        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            Intent intent =new Intent(getApplicationContext(),MainActivity.class);
            NavUtils.navigateUpTo(this,intent);
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }


    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withFilter(Pattern.compile(".*\\.mp4||.3gp||.wmv$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(false) // Set directories filterable (false by default)
                .withHiddenFiles(true)
                .withTitle("Video Seçimi")
                .start();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final TextView ed2=(TextView)findViewById(R.id.textView10);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            if (path != null) {
                Log.d("Yol: ", path);
                Toast.makeText(this, "Seçilen Dosya: " + path, Toast.LENGTH_LONG).show();
                yol=path;
                ed2.setText("Seçilen Video: "+yol);
            }
        }
    }


    private void execFFmpegBinary( final String[] command) {

        try {

            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                @Override
                public void onFailure(String s) {
                    Toast.makeText(getApplicationContext(),"Bir Hata Oluştu!",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(String s) {
                    Toast.makeText(getApplicationContext(),"Ses Dosyası Başarı İle Oluşturuldu!",Toast.LENGTH_SHORT).show();
                    progressDoalog.cancel();
                }

                @Override
                public void onProgress(String s)
                {
                    progressDoalog.show();

                }

                @Override
                public void onStart()
                {
                    progressDoalog = new ProgressDialog(VideoSesYakalamaActivity.this);
                    progressDoalog.setMessage("Ses Dosyası Çıkartılıyor...");
                    progressDoalog.setTitle("Durum");
                    progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDoalog.setCancelable(false);

                }

                @Override
                public void onFinish() {


                }

            });



        } catch (FFmpegCommandAlreadyRunningException e)
        {
           Toast.makeText(getApplicationContext(),"İşlem Bitene Kadar Lütfen Bekleyiniz!",Toast.LENGTH_SHORT).show();

        }

    }

    private void loadFFMpegBinary() {
        try {

            if (ffmpeg == null) {
                ffmpeg = FFmpeg.getInstance(getApplicationContext());


            }

            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override public void onFailure() {


                }
                @Override public void onSuccess() {


                }

            });

        } catch (FFmpegNotSupportedException e) {



        } catch (Exception e) {    Log.e("FFMEG-HATA:",ffmpeg.toString());
        }

    }




    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    private void showError() {
        Toast.makeText(this, "Veri okumaya izin ver", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openFilePicker();
                } else
                    {
                    showError();
                }
            }
        }
    }
}
