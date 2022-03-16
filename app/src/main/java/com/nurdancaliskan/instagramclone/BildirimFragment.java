package com.nurdancaliskan.instagramclone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Adapter.BildirimAdapter;
import Model.Bildirim;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BildirimFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BildirimFragment extends Fragment {

    private RecyclerView recyclerView;
    private BildirimAdapter bildirimAdapter;
    private List<Bildirim> bildirimList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BildirimFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BildirimFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BildirimFragment newInstance(String param1, String param2) {
        BildirimFragment fragment = new BildirimFragment();
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
        View view = inflater.inflate(R.layout.fragment_bildirim, container, false);

        recyclerView = view.findViewById(R.id.recyler_view_bildirimCercevesi);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        bildirimList = new ArrayList<>();
        bildirimAdapter = new BildirimAdapter(getContext(),bildirimList);
        recyclerView.setAdapter(bildirimAdapter);

        bildirimleriOku();

        return view ;
    }

    private void bildirimleriOku() {

        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference bildirimYolu = FirebaseDatabase.getInstance().getReference("Bildirimler")
                .child(mevcutKullanici.getUid());
        bildirimYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bildirimList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Bildirim bildirim = dataSnapshot.getValue(Bildirim.class);
                    bildirimList.add(bildirim);

                }

                Collections.reverse(bildirimList);
                bildirimAdapter.notifyDataSetChanged();;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}