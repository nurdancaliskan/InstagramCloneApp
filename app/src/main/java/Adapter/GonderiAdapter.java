package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nurdancaliskan.instagramclone.Gonderi;
import com.nurdancaliskan.instagramclone.ProfilFragment;
import com.nurdancaliskan.instagramclone.R;
import com.nurdancaliskan.instagramclone.YorumlarActivity;

import java.util.HashMap;
import java.util.List;

import Cerceve.GonderiDetayiFragment;
import Model.Kullanici;

public class GonderiAdapter extends RecyclerView.Adapter<GonderiAdapter.ViewHolder>{

    public Context mContext;
    public List <Gonderi> mGonderi;

    private FirebaseUser mevcutFirebaseUser;
    private ViewGroup viewGroup;

    public GonderiAdapter(Context mContext, List<Gonderi> mGonderi) {
        this.mContext = mContext;
        this.mGonderi = mGonderi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.gonderi_ogesi,viewGroup, false);
        return new GonderiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Gonderi gonderi = mGonderi.get(position);
        Glide.with(mContext).load(gonderi.getGonderiResmi()).into(holder.gonderi_resmi);

        if(gonderi.getGonderiHakkinda().equals(""))
        {
            holder.txt_gonderiHakkinda.setVisibility(View.GONE);
        }

        else
        {
            holder.txt_gonderiHakkinda.setVisibility(View.VISIBLE);
            holder.txt_gonderiHakkinda.setText(gonderi.getGonderiHakkinda());
        }

        //methodları çağırma
        gonderenBilgileri(holder.profil_resmi,holder.txt_kullanici_adi,holder.txt_gonderen,gonderi.getGonderen());
        begenildi(gonderi.getGonderiId(), holder.begeni_resmi);
        begeniSayisi(holder.txt_begeni,gonderi.getGonderiId());
        yorumlarıAl(gonderi.getGonderiId(), holder.txt_yorumlar);
        kaydedildi(gonderi.getGonderiId(),holder.kaydetme_resmi);


        //Profil resmine tıklama olayı
        holder.profil_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",gonderi.getGonderen());
                editor.apply();

                //Profil resmine tıkladığında gideceği yer

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new ProfilFragment()).commit();

            }
        });
        //Kullanıcı adına tıklandığında yapılacak işin kodları
        holder.txt_kullanici_adi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",gonderi.getGonderen());
                editor.apply();

                //Profil resmine tıkladığında gideceği yer

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new ProfilFragment()).commit();

            }
        });
        //Gonderen kişiye tıklandığında
        holder.txt_gonderen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",gonderi.getGonderen());
                editor.apply();

                //Profil resmine tıkladığında gideceği yer

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new ProfilFragment()).commit();

            }
        });
        //Gonderi resmine tıklandığında
        holder.gonderi_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",gonderi.getGonderiId());
                editor.apply();

                //Profil resmine tıkladığında gideceği yer

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new GonderiDetayiFragment()).commit();

            }
        });
        //Kaydetme resmi tıklama olayı
        holder.kaydetme_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.kaydetme_resmi.getTag().equals("kaydet"))
                {
                   FirebaseDatabase.getInstance().getReference().child("Kaydedilenler")
                            .child(mevcutFirebaseUser.getUid()).child(gonderi.getGonderiId()).setValue(true);
                }

                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Kaydedilenler").child(mevcutFirebaseUser.getUid())
                            .child(gonderi.getGonderiId()).removeValue();
                }

            }
        });
        //Beğeni resmi tıklama olayı

        holder.begeni_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.begeni_resmi.getTag().equals("beğen"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gonderi.getGonderiId())
                            .child(mevcutFirebaseUser.getUid()).setValue(true);
                    bildirimleriEkle(gonderi.getGonderen(),gonderi.getGonderiId());
                }
                    else
                {
                        FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gonderi.getGonderiId())
                                .child(mevcutFirebaseUser.getUid()).removeValue();
                }

            }
        });
        //Yorum resmi tıklama olayı
        holder.yorum_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("gonderiId",gonderi.getGonderiId());
                intent.putExtra("gondereniId",gonderi.getGonderen());
                mContext.startActivity(intent);
            }
        });

        holder.txt_yorumlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("gonderiId",gonderi.getGonderiId());
                intent.putExtra("gondereniId",gonderi.getGonderen());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGonderi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profil_resmi, gonderi_resmi, begeni_resmi, yorum_resmi, kaydetme_resmi;

        public TextView txt_kullanici_adi, txt_begeni, txt_gonderen, txt_gonderiHakkinda, txt_yorumlar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.profil_resmi_Gonderi_Ogesi);
            gonderi_resmi = itemView.findViewById(R.id.gonderi_resmi_Gonderi_ogesi);
            begeni_resmi = itemView.findViewById(R.id.begeni_Gonderi_Ogesi);
            yorum_resmi = itemView.findViewById(R.id.yorum_Gonderi_Ogesi);
            kaydetme_resmi = itemView.findViewById(R.id.kaydetme_Ogesi);

            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_Gonderi_Ogesi);
            txt_begeni = itemView.findViewById(R.id.txt_begeniler_Gonderi_Ogesi);
            txt_gonderen = itemView.findViewById(R.id.txt_gonderen_Gonderi_Ogesi);
            txt_gonderiHakkinda = itemView.findViewById(R.id.txt_gonderiHakkinda_Gonderi_Ogesi);
            txt_yorumlar = itemView.findViewById(R.id.txt_yorum_Gonderi_Ogesi);
        }
    }

    private void yorumlarıAl(String gonderiId, TextView yorumlar)
    {
        DatabaseReference yorumlarıAlmaYolu = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gonderiId);

        yorumlarıAlmaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                yorumlar.setText(snapshot.getChildrenCount()+"yorumun hepsini gör..");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void begenildi(String gonderiId, ImageView imageView)
    {
        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference begeniVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler")
                .child(gonderiId);

        begeniVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(mevcutKullanici.getUid()).exists())
                {
                    imageView.setImageResource((R.drawable.ic_begenildi));
                    imageView.setTag("beğenildi");
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("beğen");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void begeniSayisi(TextView begeniler, String gonderiId)
    {
        DatabaseReference begeniSayisiVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler")
                .child(gonderiId);
        begeniSayisiVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                begeniler.setText(dataSnapshot.getChildrenCount()+"beğeni");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void gonderenBilgileri(ImageView profil_resmi,TextView kullaniciadi, TextView gonderen, String kullaniciId)
    {
        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(kullaniciId);

        veriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                Glide.with(mContext).load(kullanici.getResimurl()).into(profil_resmi);
                kullaniciadi.setText(kullanici.getKullaniciadi());
                gonderen.setText(kullanici.getKullaniciadi());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void kaydedildi (String gonderiId,ImageView imageView)
    {
        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference kaydetmeYolu = FirebaseDatabase.getInstance().getReference().child("Kaydedilenler")
                .child(mevcutKullanici.getUid());

        kaydetmeYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(gonderiId).exists())
                {
                    imageView.setImageResource(R.drawable.ic_kaydedildi);
                    imageView.setTag("kaydedildi");
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_kaydedildi_bos);
                    imageView.setTag("kaydet");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bildirimleriEkle (String kullaniciId,String gonderiId)
    {
        DatabaseReference bildirimEklemeYolu = FirebaseDatabase.getInstance().getReference("Bildirimler")
                .child(kullaniciId);

        HashMap<String,Object>hashMap = new HashMap<>();
        hashMap.put("kullaniciid",mevcutFirebaseUser.getUid());
        hashMap.put("text","Gönderini Beğendi");
        hashMap.put("gonderiid",gonderiId);
        hashMap.put("ispost",true);

        bildirimEklemeYolu.push().setValue(hashMap); // verilerin üst üste binmemesi için.sonra verileri yola ekle
    }



}

