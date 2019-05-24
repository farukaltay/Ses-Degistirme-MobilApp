package com.example.faruk.voicechanger.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.faruk.voicechanger.R;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class SesiDegistirFragment extends Fragment {

    FFmpeg ffmpeg;
    public int a;
    byte[] bData = null;
    ProgressDialog progressDoalog;

    private static final int RECORDER_SAMPLERATE = 44100;

    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;

    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private int waveSampling = 37000;//37000
    private String filename = "";

    int deger=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ses_degistir, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button btnBasla = (Button) getView().findViewById(R.id.button11);
        final Button btnDurdur = (Button) getView().findViewById(R.id.button12);
        final Button btncal1 = (Button) getView().findViewById(R.id.button13);




        final EditText dosyaadi = (EditText) getView().findViewById(R.id.editText5);
        btnDurdur.setEnabled(false);
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.button11:

                        startRecording();
                        if(deger==1) {
                            btnBasla.setEnabled(false);
                            btnDurdur.setEnabled(true);
                        }else{}
                        break;

                    case R.id.button12:
                        btnBasla.setEnabled(true);
                        btnDurdur.setEnabled(false);
                        btncal1.setEnabled(true);

                        stopRecording();


                        break;

                    case R.id.button13:
                        playRecording();


                        break;

                }

            }
        };

        btnBasla.setOnClickListener(Listener);
        btnDurdur.setOnClickListener(Listener);
        btncal1.setOnClickListener(Listener);


    }


    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    private void startRecording() {

        final EditText dosyaadi = (EditText) getView().findViewById(R.id.editText5);

        File dos = new File(Environment.getExternalStorageDirectory() + "/Voice Changer/VoiceChanger/" +dosyaadi.getText().toString() + ".wav");
        if (!dos.exists()) {
            Toast.makeText(getActivity(), "Kayıt Başladı!", Toast.LENGTH_SHORT).show();

            final RadioButton rb1 = (RadioButton) getView().findViewById(R.id.rb1);
            final RadioButton rb2 = (RadioButton) getView().findViewById(R.id.rb2);
            final RadioButton rb3 = (RadioButton) getView().findViewById(R.id.rb3);

            final Button btnBasla = (Button) getView().findViewById(R.id.button11);
            final Button btnDurdur = (Button) getView().findViewById(R.id.button12);
            final Button btncal1 = (Button) getView().findViewById(R.id.button13);

            btnBasla.setEnabled(false);
            btnDurdur.setEnabled(true);
            btncal1.setEnabled(false);


            if (rb1.isChecked()) {
                waveSampling = 37000;
                a=0;
            } else if (rb2.isChecked()) {
                waveSampling = 80000;
                a=1;
            } else if (rb3.isChecked()) {
                waveSampling = 25000;
                a=0;
            }

            recorder = new AudioRecord(MediaRecorder.AudioSource.CAMCORDER,
                    RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                    RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

            recorder.startRecording();

            isRecording = true;

            recordingThread = new Thread(new Runnable() {

                public void run() {

                    writeAudioDataToFile();

                }
            }, "AudioRecorder Thread");
            recordingThread.start();
        }
        else{

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:

                            Toast.makeText(getActivity(), "Kayıt Başladı!", Toast.LENGTH_SHORT).show();

                            final RadioButton rb1 = (RadioButton) getView().findViewById(R.id.rb1);
                            final RadioButton rb2 = (RadioButton) getView().findViewById(R.id.rb2);
                            final RadioButton rb3 = (RadioButton) getView().findViewById(R.id.rb3);


                            final Button btnBasla = (Button) getView().findViewById(R.id.button11);
                            final Button btnDurdur = (Button) getView().findViewById(R.id.button12);
                            final Button btncal1 = (Button) getView().findViewById(R.id.button13);

                            btnBasla.setEnabled(false);
                            btnDurdur.setEnabled(true);
                            btncal1.setEnabled(false);


                            if (rb1.isChecked()) {
                                waveSampling = 37000;
                                a=0;
                            } else if (rb2.isChecked()) {
                                a=1;
                                waveSampling = 80000;
                            } else if (rb3.isChecked()) {
                                a=0;
                                waveSampling = 25000;
                            }

                            recorder = new AudioRecord(MediaRecorder.AudioSource.CAMCORDER,
                                    RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                                    RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

                            recorder.startRecording();
                            isRecording = true;
                            recordingThread = new Thread(new Runnable() {

                                public void run() {

                                    writeAudioDataToFile();

                                }
                            }, "AudioRecorder Thread");
                            recordingThread.start();

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            deger = 0;
                            Toast.makeText(getActivity(), "Üzerine Kayıt Etme İptal Edildi... ", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Aynı İsimde Ses Dosyası Mevcu.Mevcut Ses Dosyasının Üstüne Yazmak İstediğinizden Eminmisiniz?").setPositiveButton("EVET", dialogClickListener).setNegativeButton("HAYIR", dialogClickListener).show();
            deger = 0;

            }
    }

    //Conversion of short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];

        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;
    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte
        final EditText dosyaadi = (EditText) getView().findViewById(R.id.editText5);
        filename=dosyaadi.getText().toString();
        if(filename.equals(""))
        {
            SimpleDateFormat df = new SimpleDateFormat("hh-mm-ss-SSS aa");
            Date today = Calendar.getInstance().getTime();
            filename = df.format(today);
        }
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Voice Changer/VoiceChanger/"+filename+".pcm";

        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format
            recorder.read(sData, 0, BufferElements2Rec);
            System.out.println("Short wirting to file" + sData.toString());
            try {
                // writes the data to file from buffer stores the voice buffer
                bData = short2byte(sData);

                os.write(bData, 0, BufferElements2Rec * BytesPerElement);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            File f1 = new File(filePath);
            File f2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Voice Changer/VoiceChanger/"+filename+".wav");
            try {
                rawToWave(f1, f2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, waveSampling); // sample rate
            writeInt(output, RECORDER_SAMPLERATE * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }
            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
        // Adding echo
        //Clone original Bytes
        byte [] bytesTemp = fullyReadFileToBytes(rawFile);
        byte[] temp = bytesTemp.clone();
        RandomAccessFile randomAccessFile = new RandomAccessFile(waveFile, "rw");
        //seek to skip 44 bytes
        randomAccessFile.seek(44);
        //Echo
        int N = RECORDER_SAMPLERATE / 8;
        for (int n = N + 1; n < bytesTemp.length; n++) {
            bytesTemp[n] = (byte) (temp[n] + .3 * temp[n - N]);
        }
        randomAccessFile.write(bytesTemp);
        randomAccessFile.close();
        rawFile.delete();

    }
    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }

    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

    private void stopRecording() {
        if (null != recorder) {


            isRecording = false;
            Toast.makeText(getActivity(), "Kayıt Başarı İle Kaydedildi!", Toast.LENGTH_SHORT).show();

            recorder.stop();
            recorder.release();


            recorder = null;
            recordingThread = null;


        }


    }


    private void playRecording() {

        MediaPlayer player = new MediaPlayer();
        if(filename.length()!=0) {
            try {
                player.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Voice Changer/VoiceChanger/" + filename + ".wav");
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.start();
            Toast.makeText(getActivity(), filename+" Parça Çalınıyor!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), " Önce Kayıt Yapınız!", Toast.LENGTH_SHORT).show();

        }







    }


    private void execFFmpegBinary(final String[] command) {

        try {

            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                @Override
                public void onFailure(String s) {
                    Toast.makeText(getActivity(), "HATA", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(String s) {


                    progressDoalog.cancel();
                    Toast.makeText(getActivity(), "Kayıt Başarı İle Kaydedildi!", Toast.LENGTH_SHORT).show();

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