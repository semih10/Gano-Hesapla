package com.semihbarut.gano;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity  {
    HepsiniSilFragment hepsiniSilFragment;
    SilFragment fragment;
    EditText etDersAdi;
    EditText etKrediSayisi;
    Button btnYaz;
    Button btnSil;
    Spinner spnHarfNotlari;
    ArrayList<String> dersler= new ArrayList<String>();
    ArrayList<String> krediler= new ArrayList<String>();
    ArrayList<String> notlar= new ArrayList<String>();
    ListView listView;
    TextView tvDeneme;
    ListviewAdapter adapter1;
    Button btnHesapla;
    TextView kayanYazı;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        kayanYazı=findViewById(R.id.tvKayanYazı);
        kayanYazı.setSelected(true);
        final String harfler[]={ "AA","BA","BB","CB","CC","DC","DD","FD","FF","Not"};
        spnHarfNotlari = findViewById(R.id.spnHarfler);
        etDersAdi = findViewById(R.id.input_field);
        etKrediSayisi =findViewById(R.id.etKrediSayısı);
        btnYaz = findViewById(R.id.write_button);
        listView=findViewById(R.id.listview);
        btnSil=findViewById(R.id.btnSil);
        tvDeneme=findViewById(R.id.tvDeneme);
        btnHesapla=findViewById(R.id.btnHesapla);
        kayanYazıyaYaz();
        final Animation soldanGelenText=AnimationUtils.loadAnimation(this,R.anim.soldan_gelen_text);
        soldanGelenText.setFillAfter(true);
        soldanGelenText.start();

        final Animation sagaGidenText=AnimationUtils.loadAnimation(this,R.anim.saga_giden_text);
        sagaGidenText.setFillAfter(true);
        sagaGidenText.start();

       final CustomSpinnerAdapter adapter=new CustomSpinnerAdapter(MainActivity.this, android.R.layout.simple_spinner_item, harfler,
                "Not");
        adapter.setDropDownViewResource(R.layout.spinner_item);

        spnHarfNotlari.setAdapter(adapter);
        spnHarfNotlari.setSelection(adapter.getCount()); //This line is must to show hint


        dosyadanOku("Dersler");
        dosyadanOku("KrediSayıları");
        dosyadanOku("HarfNotları");

        if (notlar.size()==0){
            btnSil.setEnabled(false);
            btnHesapla.setEnabled(false);
        }
        btnHesapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ortHesapla();
                tvDeneme.startAnimation(soldanGelenText);

                CountDownTimer countDownTimer=new CountDownTimer(3500,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        btnHesapla.setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        tvDeneme.startAnimation(sagaGidenText);
                        btnHesapla.setEnabled(true);
                    }
                }.start();
            }
        });

        btnYaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etDersAdi.getText().toString().isEmpty()|| etKrediSayisi.getText().toString().isEmpty()||
                        spnHarfNotlari.getSelectedItem().equals("Not")){
                    Toast.makeText(getApplicationContext(), "Boş geçilemez", Toast.LENGTH_SHORT).show();

                }else {
                    dersler.add(etDersAdi.getText().toString());
                    krediler.add(etKrediSayisi.getText().toString());
                    notlar.add(spnHarfNotlari.getSelectedItem().toString());

                    dosyayaYaz(etDersAdi,"Dersler");
                    dosyayaYaz(etKrediSayisi,"KrediSayıları");
                    dosyayaYaz(spnHarfNotlari,"HarfNotları");
                    btnHesapla.setEnabled(true);
                    btnSil.setEnabled(true);
                }

                adapter1.notifyDataSetChanged();

                spnHarfNotlari.setAdapter(adapter);//spinnerı sıfırla
                spnHarfNotlari.setSelection(adapter.getCount());//spinnerı sıfırla

                keyboardıGizle();


            }
        });



        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHepsiniSilDialog(v);

                Bundle bundle=new Bundle();
                bundle.putStringArrayList("dersler",dersler);
                bundle.putStringArrayList("krediler",krediler);
                bundle.putStringArrayList("notlar",notlar);

                hepsiniSilFragment.setArguments(bundle);
                hepsiniSilFragment.sendAdapter(adapter1);
                hepsiniSilFragment.sendHesaplaButton(btnHesapla);
                hepsiniSilFragment.sendSilButton(btnSil);



            }
        });


        adapter1=new ListviewAdapter(this,dersler,krediler,notlar,1);
        listView.setAdapter(adapter1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showSilDialog(view);

                Bundle bundle=new Bundle();
                bundle.putInt("position",position);
                bundle.putStringArrayList("dersler",dersler);
                bundle.putStringArrayList("krediler",krediler);
                bundle.putStringArrayList("notlar",notlar);
                fragment.sendAdapter(adapter1);
                fragment.sendHesaplaButton(btnHesapla);
                fragment.sendSilButton(btnSil);
                fragment.setArguments(bundle);
            }
        });

        if(etDersAdi.getText().toString().isEmpty()){
            btnYaz.setEnabled(false);
        }

        etDersAdi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnYaz.setEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnYaz.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                btnYaz.setEnabled(true);
            }
        });
    }

    private void kayanYazıyaYaz() {
        int random = new Random().nextInt(11);
        String sözler[]={"“Bir gün kalkacaksınız ve hep hayal ettiğiniz şeyleri yapmaya vakit kalmamış olacak. " +
                "Şimdi tam zamanı. Harekete geçin.” Paulo Coelho","“Arkamda bıraktığım köprüleri yıkarım ki, " +
                "ilerlemekten başka çarem kalmasın.” Fridtjof Nansen","“Senin almaya cesaret edemediğin riskleri " +
                "alanlar, senin yaşamak istediğin hayatı yaşarlar.” Sokrates","“Unutma, bir şeyin yapılamaz olduğunu" +
                " düşünerek uyursan, başkasının o şeyi yaparken çıkardığı gürültüyle uyanırsın.” Konfüçyüs","“Hiçbir şeyi" +
                " yargılama, mutlu olacaksın. Her şeyi affet, daha mutlu olacaksın. Her şeyi sev, en mutlu sen olacaksın.” Sri" +
                " Chinmoy Ghose  ","“Hiçbir şeyden asla vazgeçme; çünkü vazgeçenler yalnızca kaybedenlerdir.” Abraham Lincoln",
                "“Bazı yenilgilerin nedeni, insanların işi yarıda bıraktıklarında, başarıya ne kadar yakın olduklarını bilememeleridir.” Thomas" +
                " Edison","“Uçurtmalar rüzgar gücü ile değil, o güce karşı koydukları için yükselirler.” Winston Churchill","“Uçamıyorsan, koş; " +
                "koşamıyorsan, yürü. Eğer yürüyemiyorsan, sürün; ama hareket etmeye devam et. Geleceğe ilerlemeyi sürdür.” Martin Luther King"
                ,"“Yolunu değiştirmeden devam ettiğin sürece, ne kadar yavaş gittiğinin bir önemi yoktur.” Konfüçyüs","“Hayatınızın hikayesini" +
                " yazarken, kalemi başkasının tutmasına izin vermeyin.” Harley Davidson"};

        kayanYazı.setText(sözler[random]);
    }

    private void keyboardıGizle() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showSilDialog(View view){

        fragment=new SilFragment();
        fragment.show(getFragmentManager(),"My Delete Dialog");


    }
    public void showHepsiniSilDialog(View view){

        hepsiniSilFragment=new HepsiniSilFragment();
        hepsiniSilFragment.show(getFragmentManager(),"My Delete All Dialog");


    }
    private void ortHesapla() {
        double not=0.0;
        double sum=0.0;
        double toplamKrediSayısı=0.0;
        for(int i=0;i<notlar.size();i++){
            sum=sum+(Double.parseDouble(krediler.get(i))*harfNotDeğeri(notlar.get(i)));
            toplamKrediSayısı=toplamKrediSayısı+Double.parseDouble(krediler.get(i));
        }
        not=sum/toplamKrediSayısı;
        if (not<2.0){
            tvDeneme.setTextColor(Color.RED);
        }
        if (not>=2.0&&not<3.0){
            tvDeneme.setTextColor(Color.BLUE);
        }
        if (not>=3.0)
        {
            tvDeneme.setTextColor(Color.GREEN);
        }

        tvDeneme.setText(String.valueOf(String.format("%10.2f",not)));


    }

    private double harfNotDeğeri(String harfNotu) {
       double değer=0.0;

       switch (harfNotu){
           case "AA":
               değer=4.00;
               break;
           case "BA":
               değer=3.50;
               break;
           case "BB":
               değer=3.00;
               break;
           case "CB":
               değer=2.50;
               break;
           case "CC":
               değer=2.00;
               break;
           case "DC":
               değer=1.50;
               break;
           case "DD":
               değer=1.00;
               break;
           case "FD":
               değer=0.50;
               break;
           case "FF":
               değer=0.00;
               break;

       }

        return değer;
    }


    public void dosyayaYaz(EditText et,String dosyaAdı) {
        String textToSave = et.getText().toString()+"\n";

        try {
            FileOutputStream fileOutputStream = openFileOutput(dosyaAdı+".txt", MODE_APPEND);
            fileOutputStream.write(textToSave.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Eklendi", Toast.LENGTH_SHORT).show();


            et.setText("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void dosyayaYaz(Spinner spn,String dosyaAdı) {
        String textToSave = spn.getSelectedItem().toString()+"\n";

        try {
            FileOutputStream fileOutputStream = openFileOutput(dosyaAdı+".txt", MODE_APPEND);
            fileOutputStream.write(textToSave.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Eklendi", Toast.LENGTH_SHORT).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void dosyadanOku(String dosyaAdı) {
        try {
            FileInputStream fileInputStream = openFileInput(dosyaAdı+".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines + "\n");
                if (dosyaAdı.equals("Dersler")){
                    dersler.add(lines);
                }
                if (dosyaAdı.equals("KrediSayıları")){
                    krediler.add(lines);
                }
                if (dosyaAdı.equals("HarfNotları")){
                    notlar.add(lines);
                }
            }
            if (dersler.size()==0){
                btnHesapla.setEnabled(false);
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
            FileOutputStream fileOutputStream = openFileOutput(dosyaAdı + ".txt", MODE_PRIVATE);
            fileOutputStream.write(textToSave.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Silindi", Toast.LENGTH_SHORT).show();
            dersler.clear();
            krediler.clear();
            notlar.clear();
            adapter1.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
