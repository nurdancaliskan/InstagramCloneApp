package com.nurdancaliskan.instagramclone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Adapter.FotografAdapter;
import Model.Kullanici;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {

    ImageView resimSeçenekler, profil_resmi;
    TextView txt_gonderiler, txt_takipciler, txt_takipEdilenler, txt_Ad, txt_bio, txt_kullaniciAdi;
    Button btn_profili_Duzenle;
    ImageButton imagebtn_Fotograflarım, imagebtn_kaydedilen_fotograflar;

    private List <String> kaydettiklerim;

    //Kaydettiğim gönderi
    private List<Gonderi> gonderiList_kaydettiklerim;
    RecyclerView recyclerViewKaydettiklerim;
    FotografAdapter fotografAdapterKaydettiklerim;


    //Fotoğrafları profilde görme
    RecyclerView recyclerViewFotograflar;
    FotografAdapter fotografAdapter;
    List<Gonderi> gonderiList;


    FirebaseUser mevcutKullanici;
    String profilId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profilId = prefs.getString("profileid", "none");

        resimSeçenekler = view.findViewById(R.id.resimSecenekler_profilCercevesi);
        profil_resmi = view.findViewById(R.id.profil_resmi_profilCercevesi);

        txt_gonderiler = view.findViewById(R.id.txt_gonder_profilCercevesi);
        txt_takipciler = view.findViewById(R.id.txt_takipciler_profilCercevesi);
        txt_takipEdilenler = view.findViewById(R.id.txt_takipEdilenler_profilCercevesi);
        txt_bio = view.findViewById(R.id.txt_profilBio_cercevesi);
        txt_Ad = view.findViewById(R.id.txt_profilAd_cercevesi);
        txt_kullaniciAdi = view.findViewById(R.id.txt_kullaniciadi_profilCerceve);


        btn_profili_Duzenle = view.findViewById(R.id.btn_profiliDuzenle_profilCercevesi);

        imagebtn_Fotograflarım = view.findViewById(R.id.imagebtn_fotograflarım_profilCercevesi);
        imagebtn_kaydedilen_fotograflar = view.findViewById(R.id.imagebtn_kaydedilenFotograflar_profilCercevesi);

        //Fotoğrafları profilde görme

        recyclerViewFotograflar = view.findViewById(R.id.recyler_view_profilCercevesi);
        recyclerViewFotograflar.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerViewFotograflar.setLayoutManager(linearLayoutManager);
        kaydettiklerim = new ArrayList<>();
        gonderiList = new ArrayList<>();
        gonderiList_kaydettiklerim = new ArrayList<>();
        fotografAdapter = new FotografAdapter(getContext(),gonderiList);
        recyclerViewFotograflar.setAdapter(fotografAdapter);

        //Kaydettiğim gönderi
        recyclerViewKaydettiklerim = view.findViewById(R.id.recyler_kaydet_view_profilCercevesi);
        recyclerViewKaydettiklerim.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_Kaydettiklerim = new GridLayoutManager(getContext(),3);
        recyclerViewKaydettiklerim.setLayoutManager(linearLayoutManager_Kaydettiklerim);
        fotografAdapterKaydettiklerim = new FotografAdapter(getContext(),gonderiList_kaydettiklerim);
        recyclerViewKaydettiklerim.setAdapter(fotografAdapterKaydettiklerim);

        recyclerViewFotograflar.setVisibility(View.VISIBLE);
        recyclerViewKaydettiklerim.setVisibility(View.GONE);

        //Metotları çağır
        kullaniciBilgisi();
        takipcileriAl();
        gonderiSayisiAl();
        fotograflarim();
        kaydettiklerim();


        if (profilId.equals(mevcutKullanici.getUid())) {
            btn_profili_Duzenle.setText("Profili Düzenle");
            //profili düzenlemeye gitsin
        }

        else
        {
            takipKontrolu();
            ;
            imagebtn_kaydedilen_fotograflar.setVisibility(View.GONE);
        }

        btn_profili_Duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn = btn_profili_Duzenle.getText().toString();

                if (btn.equals("Profili Düzenle")) {
                    startActivity(new Intent(getContext(),ProfilDuzenleActivity.class));
                }
                else if (btn.equals("takip et")) {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(mevcutKullanici.getUid())
                            .child("takipEdilenler").child(profilId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                            .child("takipciler").child(mevcutKullanici.getUid()).setValue(true);

                   bildirimleriEkle();
                }
                else if (btn.equals("takip ediliyor")) {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(mevcutKullanici.getUid())
                            .child("takipEdilenler").child(profilId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                            .child("takipciler").child(mevcutKullanici.getUid()).removeValue();
                }
            }
        });

         //Seçenekler sayfasına git
            resimSeçenekler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),SeceneklerActivity.class);
                    startActivity(intent);
                }
            });




        imagebtn_Fotograflarım.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewFotograflar.setVisibility(View.VISIBLE);
                recyclerViewKaydettiklerim.setVisibility(View.GONE);
            }
        });

        imagebtn_kaydedilen_fotograflar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewFotograflar.setVisibility(View.GONE);
                recyclerViewKaydettiklerim.setVisibility(View.VISIBLE);
            }
        });

        return view;

    }

    private void kullaniciBilgisi() {
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(profilId);

        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (getContext() == null) {
                    return;
                }

                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                if(kullanici != null){
                    Glide.with(getContext()).load(kullanici.getResimurl()).into(profil_resmi);
                    txt_kullaniciAdi.setText(kullanici.getKullaniciadi());
                    txt_Ad.setText(kullanici.getAd());
                    txt_bio.setText(kullanici.getBio());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void takipKontrolu() {
        DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(mevcutKullanici.getUid()).child("takipEdilenler");
        takipYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(profilId).exists()) {
                    btn_profili_Duzenle.setText("takip ediliyor");
                } else {
                    btn_profili_Duzenle.setText("takip et");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void takipcileriAl() {
        //Takipçi sayısını alır
        DatabaseReference takipciYolu = FirebaseDatabase.getInstance().getReference()
                .child("Takip").child(profilId).child("takipçiler");

        takipciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txt_takipciler.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Takip edilen sayısını alır
        DatabaseReference takipciEdilenYolu = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(profilId).child("takipEdilenler");

        takipciEdilenYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txt_takipEdilenler.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gonderiSayisiAl() {
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);

                    if (gonderi.getGonderen().equals(profilId)) {
                        i++;
                    }
                }
                txt_gonderiler.setText("" + i);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    private void fotograflarim () {

        DatabaseReference fotografYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
        fotografYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gonderiList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                   Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);
                   if (gonderi.getGonderen().equals(profilId))
                   {
                       gonderiList.add(gonderi);
                   }
                }
                Collections.reverse(gonderiList);
                fotografAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void kaydettiklerim ()
    {

        DatabaseReference kaydettiklerimYolu = FirebaseDatabase.getInstance().getReference("Kaydedilenler")
                .child(mevcutKullanici.getUid());

        kaydettiklerimYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    kaydettiklerim.add(snapshot.getKey());
                }

                kaydettiklerimiOku();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void kaydettiklerimiOku()
    {
        DatabaseReference gonderidenOku = FirebaseDatabase.getInstance().getReference("Gonderiler");
        gonderidenOku.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gonderiList_kaydettiklerim.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);

                    for (String id : kaydettiklerim)
                    {
                        if (gonderi.getGonderiId().equals(id))
                        {
                            gonderiList_kaydettiklerim.add(gonderi);
                        }
                    }
                    fotografAdapterKaydettiklerim.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void bildirimleriEkle ()
    {
        DatabaseReference bildirimEklemeYolu = FirebaseDatabase.getInstance().getReference("Bildirimler")
                .child(profilId);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("kullaniciid",mevcutKullanici.getUid());
        hashMap.put("text","Seni Takip Etmeye Başladı");
        hashMap.put("gonderiid","");
        hashMap.put("ispost",false);

        bildirimEklemeYolu.push().setValue(hashMap); // verilerin üst üste binmemesi için.sonra verileri yola ekle
    }
}