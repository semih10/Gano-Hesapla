package com.semihbarut.gano;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;


public class SilFragment extends DialogFragment {
    TextView textView;
    Button btnEvet;
    Button btnHayir;
    Button btnHesapla;
    Button btnSil;
    int position;
    ArrayList<String> dersler= new ArrayList<String>();
    ArrayList<String> krediler= new ArrayList<String>();
    ArrayList<String> notlar= new ArrayList<String>();
    ListviewAdapter adapter1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sil, null);

        btnEvet = v.findViewById(R.id.btnEvet);
        btnHayir = v.findViewById(R.id.btnHayir);
        textView=v.findViewById(R.id.textView);

        Bundle bundle = getArguments();
        position=bundle.getInt("position");
        dersler=bundle.getStringArrayList("dersler");
        krediler=bundle.getStringArrayList("krediler");
        notlar=bundle.getStringArrayList("notlar");



        btnEvet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                dersler.remove(position);
                krediler.remove(position);
                notlar.remove(position);

                içerikSil("Dersler");
                içerikSil("KrediSayıları");
                içerikSil("HarfNotları");

                dosyayaYaz("Dersler",dersler);
                dosyayaYaz("KrediSayıları",krediler);
                dosyayaYaz("HarfNotları",notlar);
                adapter1.notifyDataSetChanged();
                if (notlar.size()==0){
                    btnHesapla.setEnabled(false);
                    btnSil.setEnabled(false);
                }
            }
        });
        btnHayir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }
    public void dosyayaYaz(String dosyaAdı,ArrayList<String> veri) {
        String enter="\n";

        try {
            for (int i=0;i<veri.size();i++){
                FileOutputStream fileOutputStream = getActivity().openFileOutput(dosyaAdı+".txt", MODE_APPEND);
                fileOutputStream.write((veri.get(i)+enter).getBytes());
                fileOutputStream.close();
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
