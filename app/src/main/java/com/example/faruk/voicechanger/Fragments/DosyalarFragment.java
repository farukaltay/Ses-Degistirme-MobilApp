package com.example.faruk.voicechanger.Fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.faruk.voicechanger.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *** Faruk_Altay 11.11.2018. ***
 */

public class DosyalarFragment extends Fragment {

    ArrayList<String>liste = new ArrayList<>();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    String yol = Environment.getExternalStorageDirectory()+ "/Voice Changer/VoiceChanger/";
    String dosyaadi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ses_dosyalari,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String folder_main = "Voice Changer";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File f1 = new File(Environment.getExternalStorageDirectory() + "/" + folder_main, "VoiceChanger");
        if (!f1.exists()) {
            f1.mkdirs();
        }

        doldur();
        final ListView listView = (ListView)getView().findViewById(R.id.listview);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,liste);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(yol+"/"+liste.get(position));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    dosyaadi=liste.get(position);
                    Toast.makeText(getActivity(),"'"+dosyaadi+"' Parçası Çalınıyor!",Toast.LENGTH_SHORT).show();

                }
                catch (IOException e){

                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dosyaadi=listView.getItemAtPosition(position).toString();
                String sharePath =  Environment.getExternalStorageDirectory()+ "/Voice Changer/VoiceChanger/"+dosyaadi;
                Uri uri = Uri.parse(sharePath);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Ses Dosyasını Paylaş"));
                return false;
            }
        });

        Button durdur=(Button)getView().findViewById(R.id.btnDurdur);
        durdur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if(dosyaadi!=null) {
                    Toast.makeText(getActivity(), "'" + dosyaadi + "' Parçası Durduruldu!", Toast.LENGTH_SHORT).show();
                }
                else{}
                dosyaadi=null;
            }
        });
    }
    public void doldur(){
        File[] dosyalar = Environment.getExternalStoragePublicDirectory("Voice Changer/VoiceChanger").listFiles();
        for (int i = 0; i<dosyalar.length; i++){
            liste.add(dosyalar[i].getName());
        }
    }
}
