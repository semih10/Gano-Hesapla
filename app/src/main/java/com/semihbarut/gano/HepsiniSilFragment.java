package com.semihbarut.gano;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HepsiniSilFragment extends DialogFragment {
    Button btnHesapla;
    Button btnSil;
    ListviewAdapter adapter1;
    Button btnEvet;
    Button btnHayir;
    ArrayList<String> dersler= new ArrayList<String>();
    ArrayList<String> krediler= new ArrayList<String>();
    ArrayList<String> notlar= new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_hepsini_sil,null);

        btnEvet=view.findViewById(R.id.btnYes);
        btnHayir=view.findViewById(R.id.btnNo);

        Bundle bundle = getArguments();
        dersler=bundle.getStringArrayList("dersler");
        krediler=bundle.getStringArrayList("krediler");
        notlar=bundle.getStringArrayList("notlar");

        btnEvet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                içerikSil("Dersler");
                içerikSil("KrediSayıları");
                içerikSil("HarfNotları");

                dersler.clear();
                krediler.clear();
                notlar.clear();

                btnHesapla.setEnabled(false);
                btnSil.setEnabled(false);

                adapter1.notifyDataSetChanged();
            }
        });

        btnHayir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
    public void içerikSil(String dosyaAdı) {
        String textToSave = "";
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput(dosyaAdı + ".txt", MODE_PRIVATE);
            fileOutputStream.write(textToSave.getBytes());
            fileOutputStream.close();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void  sendAdapter(ListviewAdapter adapter){
        this.adapter1=adapter;

    }
    public void  sendSilButton(Button button){
        this.btnSil=button;

    }
    public void  sendHesaplaButton(Button button){
        this.btnHesapla=button;

    }
}
