package com.nurdancaliskan.instagramclone;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.KullaniciAdapter;
import Model.Kullanici;

public class AramaFragment extends Fragment {
    private RecyclerView recyclerView;
    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullanicilar;

    EditText arama_bar;

    public AramaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arama, container, false);

        recyclerView = view.findViewById(R.id.recyler_view_arama);
        recyclerView.setHasFixedSize(true);
        mKullanicilar = new ArrayList<>();
        kullaniciAdapter = new KullaniciAdapter(getContext(), mKullanicilar);

        recyclerView.setAdapter(kullaniciAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        arama_bar = view.findViewById(R.id.edt_arama_bar);


        kullanicilariOku();

        arama_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                kullaniciAra(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;

    }

    private void kullaniciAra(String s) // arama yapt??raca????m kodlar
    {
        Query sorgu = FirebaseDatabase.getInstance().getReference("Kullanc??lar").orderByChild("kullaniciadi")
                .startAt(s)
                .endAt(s + "\uf8ff");

        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mKullanicilar.clear();
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    Kullanici kullanici = datasnapshot.getValue(Kullanici.class);
                    mKullanicilar.add(kullanici);
                }

                kullaniciAdapter.notifyDataSetChanged(); // veriler her g??ncellendi??inde liste de g??ncellensin
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void kullanicilariOku() {
        DatabaseReference kullanicilarYolu = FirebaseDatabase.getInstance().getReference("Kullan??c??lar");

        kullanicilarYolu.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (arama_bar.getText().toString().equals("")) {
                    mKullanicilar.clear();
                    for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                        Kullanici kullanici = datasnapshot.getValue(Kullanici.class);
                        mKullanicilar.add(kullanici);

                    }
                    kullaniciAdapter.notifyDataSetChanged(); //her t??rl?? de??i??ikli??in hemen yazmas?? i??in kullanad??k.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
