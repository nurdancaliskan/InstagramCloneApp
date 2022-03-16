package Cerceve;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
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

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView ;
    private GonderiAdapter gonderiAdapter;
    private List<Gonderi> gonderiListesi;

    private List<String> takipListesi;

    ProgressBar progressBar;

    public HomeFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInsanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyler_view_HomeFragment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        gonderiListesi = new ArrayList<>();
        gonderiAdapter = new GonderiAdapter(getContext(),gonderiListesi);

        recyclerView.setAdapter(gonderiAdapter);

        progressBar = view.findViewById(R.id.progress_homeFragment);

        takipKontrolu();

        return view;

    }

    private void takipKontrolu ()
    {

        takipListesi = new ArrayList<>();

        DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference("Takip")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("takipEdilenler");

        takipYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                takipListesi.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    takipListesi.add(snapshot.getKey());
                }

                gonderileriOku();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gonderileriOku ()
    {
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               gonderiListesi.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);
                    for(String id : takipListesi)
                    {
                        if (gonderi.getGonderen().equals(id))
                        {
                            gonderiListesi.add(gonderi);
                        }
                    }
                }

                gonderiAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}