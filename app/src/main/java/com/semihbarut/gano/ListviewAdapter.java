package com.semihbarut.gano;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListviewAdapter extends ArrayAdapter<String>  {
    final Context context;
    final List<String> dersName;
    final List<String> credit;
    final List<String> harfScore;
    final int image;


    public ListviewAdapter(Context context, List<String> dersAdı, List<String> kredi, List<String> harfNotu, int img) {
        super(context, R.layout.tek_satir_item,dersAdı);
        this.context = context;
        this.dersName = dersAdı;
        this.credit = kredi;
        this.harfScore = harfNotu;
        this.image=img;

    }
    class ViewYerTutucu {

        TextView dersAdı;
        TextView kredi;
        TextView harfNotu;
        ImageView img;
        TextView indeks;
        ViewYerTutucu(View v) {
            dersAdı = (TextView) v.findViewById(R.id.tvDersAdı);
            kredi = (TextView) v.findViewById(R.id.tvKrediSayısı);
            harfNotu = (TextView) v.findViewById(R.id.tvHarfNotu);
            img = (ImageView) v.findViewById(R.id.imgYuz);
            indeks = (TextView) v.findViewById(R.id.tvIndeks);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // return super.getView(position, convertView, parent);

        View tek_satir = convertView;
        ViewYerTutucu tutucu=null;

        if (tek_satir == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            tek_satir = inflater.inflate(R.layout.tek_satir_item, parent, false);
            tutucu=new ViewYerTutucu(tek_satir);
            tek_satir.setTag(tutucu);

        }
        else{
            tutucu= (ViewYerTutucu) tek_satir.getTag();

        }
        if(dersName.get(position).length()>10&&dersName.get(position).length()<=22){
            tutucu.dersAdı.setTextSize(12);
        }
        else if(dersName.get(position).length()>23){
            tutucu.dersAdı.setTextSize(7);
        }
        else{
            tutucu.dersAdı.setTextSize(18);
        }

        tutucu.dersAdı.setText(dersName.get(position));
        tutucu.kredi.setText(credit.get(position)+" Kredi");
        tutucu.harfNotu.setText(harfScore.get(position));
        if(harfScore.get(position).equals("AA")){
            tutucu.img.setImageResource(R.drawable.ic_action_emo_cool);
        }
        if(harfScore.get(position).equals("BA")){
            tutucu.img.setImageResource(R.drawable.ic_action_emo_wink);
        }
        if(harfScore.get(position).equals("BB")){
            tutucu.img.setImageResource(R.drawable.ic_action_emo_wink);
        }
        if(harfScore.get(position).equals("CB")){
            tutucu.img.setImageResource(R.drawable.ic_action_emo_basic);
        }
        if(harfScore.get(position).equals("CC")){
            tutucu.img.setImageResource(R.drawable.ic_action_emo_basic);
        }
        if(harfScore.get(position).equals("DC")){
            tutucu.img.setImageResource(R.drawable.ic_action_emo_shame);
        }
        if(harfScore.get(position).equals("DD")){
            tutucu.img.setImageResource(R.drawable.ic_action_emo_shame);
        }
        if(harfScore.get(position).equals("FD")){
            tutucu.img.setImageResource(R.drawable.ic_action_emo_cry);
        }
        if(harfScore.get(position).equals("FF")){
            tutucu.img.setImageResource(R.drawable.ic_action_emo_cry);
        }

        tutucu.indeks.setText((position+1)+" -");
        return tek_satir;
    }

}