package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nurdancaliskan.instagramclone.Gonderi;
import com.nurdancaliskan.instagramclone.R;

import java.util.List;

import Cerceve.GonderiDetayiFragment;
import Model.Bildirim;
import Model.Kullanici;

public class BildirimAdapter extends RecyclerView.Adapter<BildirimAdapter.ViewHolder>{

   private Context mContext;
   private List<Bildirim> mBildirim;

    public BildirimAdapter(Context mContext, List<Bildirim> mBildirim) {
        this.mContext = mContext;
        this.mBildirim = mBildirim;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.bildirim_ogesi,parent,false);

        return new BildirimAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Bildirim bildirim = mBildirim.get(position);

        holder.txt_yorum.setText(bildirim.getText());
        kullaniciBilgisiAl(holder.profil_resmi,holder.txt_kullaniciadi,bildirim.getKullaniciId());

        if (bildirim.isIspost())
        {
            holder.gonderi_resmi.setVisibility(View.VISIBLE);
            gonderiresmiAl(holder.gonderi_resmi,bildirim.getGonderiId());
        }
        else
        {
            holder.gonderi_resmi.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bildirim.isIspost())
                {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("postid",bildirim.getGonderiId());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                            new GonderiDetayiFragment()).commit();

                }

                else
                {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("profileid",bildirim.getGonderiId());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                            new GonderiDetayiFragment()).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBildirim.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView profil_resmi,gonderi_resmi;
        public TextView txt_kullaniciadi,txt_yorum;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            profil_resmi = itemView.findViewById(R.id.profil_resmi_bildirim_ogesi);
            gonderi_resmi = itemView.findViewById(R.id.gonderi_resmi_bildirimOgesi);

            txt_kullaniciadi = itemView.findViewById(R.id.txt_kullanici_adı_bildirimOgesi);
            txt_yorum = itemView.findViewById(R.id.txt_yorum_BildirimOgesi);
        }
    }

    private void kullaniciBilgisiAl (ImageView imageView, TextView kullaniciadi, String gonderenid)
    {
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(String.valueOf(gonderenid));

        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                if (kullanici!=null){
                    Glide.with(mContext).load(kullanici.getResimurl()).into(imageView);
                    kullaniciadi.setText(kullanici.getKullaniciadi());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void gonderiresmiAl (ImageView imageView ,String gonderiId)
    {
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler").child(gonderiId);
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);
                Glide.with(mContext).load(gonderi.getGonderiResmi()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
