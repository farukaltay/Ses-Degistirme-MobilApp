package com.example.faruk.voicechanger.Fragments;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.faruk.voicechanger.R;

import java.util.Locale;

/**
 *** Faruk_Altay 11.11.2018. ***
 */

public class TTSFragment extends Fragment {

    TextToSpeech t1;
    EditText ed1;
    Button button;
    Button btnSustur;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.texttospeech,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ed1=(EditText)getView().findViewById(R.id.editText);
        button=(Button)getView().findViewById(R.id.btnKonus);
        btnSustur=(Button)getView().findViewById(R.id.btnSustur);
        t1=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = ed1.getText().toString();
                Toast.makeText(getActivity(), "Çalışıyor...",Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

            }

        });
        btnSustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t1 !=null){
                    t1.stop();
                }
            }
        });
    }


    }

