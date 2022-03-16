package Cerceve;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nurdancaliskan.instagramclone.Gonderi;
import com.nurdancaliskan.instagramclone.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.GonderiAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GonderiDetayiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GonderiDetayiFragment extends Fragment {

    private RecyclerView recyclerView;
    private GonderiAdapter gonderiAdapter;
    private List<Gonderi> gonderiListesi;

    String gonderiId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GonderiDetayiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GonderiDetayiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GonderiDetayiFragment newInstance(String param1, String param2) {
        GonderiDetayiFragment fragment = new GonderiDetayiFragment();
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
        View view = inflater.inflate(R.layout.fragment_gonderi_detayi, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        gonderiId = preferences.getString("postid","none");

        recyclerView = view.findViewById(R.id.recyler_view_gonderiDetayi);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayouManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayouManager);

        gonderiListesi = new ArrayList<>();
        gonderiAdapter = new GonderiAdapter(getContext(),gonderiListesi);
        recyclerView.setAdapter(gonderiAdapter);

        gonderiOku();

        return view;
    }

    private void gonderiOku()
    {
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler")
                .child(gonderiId);
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                gonderiListesi.clear();
                Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);
                gonderiListesi.add(gonderi);

                gonderiAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}