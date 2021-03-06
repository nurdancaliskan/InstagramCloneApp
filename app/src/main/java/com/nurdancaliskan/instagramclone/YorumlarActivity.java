package com.nurdancaliskan.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

import Model.Kullanici;

public class YorumlarActivity extends AppCompatActivity {

    EditText edt_yorum_ekle;
    ImageView profil_resmi;
    TextView txt_gonder;

    String gonderiId;
    String gonderenId;

    FirebaseUser mevcutKullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorumlar);

        Toolbar toolbar =findViewById(R.id.toolbar_yorumlarActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yorumlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edt_yorum_ekle = findViewById(R.id.edt_yorumEkle_yorumlarActivity);
        profil_resmi = findViewById(R.id.profil_resmi_yorumlarActivity);
        txt_gonder = findViewById(R.id.txt_gonder_yorumlarActivity);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = new Intent();

        gonderiId = intent.getStringExtra("gonderiId");
        gonderenId = intent.getStringExtra("g??nderenId");

        Intent intent1 = getIntent();
        gonderiId = intent1.getStringExtra("gonderiId");
        gonderenId = intent1.getStringExtra("gonderenId");

        txt_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_yorum_ekle.getText().toString().equals(""))
                {
                    Toast.makeText(YorumlarActivity.this, "Bo?? yorum g??nderemezsiniz.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    yorumEkle();
                }
            }

        });

        resimAl();
        yorumlar??Oku();

    }

    private void yorumlar??Oku() {
    }

    private void yorumEkle() {
        DatabaseReference yorumlarYolu = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gonderiId);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("yorum",edt_yorum_ekle.getText().toString());
        hashMap.put("gonderen",mevcutKullanici.getUid());

        yorumlarYolu.push().setValue(hashMap);    //push verilerin ??st ??ste gelmemesi i??in

        //Yorum eklendi bildirimi metodu
        bildirimleriEkle();

        edt_yorum_ekle.setText("");

    }

    private void resimAl()
    {
        DatabaseReference resimAlmaYolu = FirebaseDatabase.getInstance().getReference("Kullan??c??lar").child(mevcutKullanici.getUid());

        resimAlmaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici = snapshot.getValue(Kullanici.class);
                Glide.with(getApplicationContext()).load(kullanici.getResimurl()).into(profil_resmi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bildirimleriEkle ()
    {
        DatabaseReference bildirimEklemeYolu = FirebaseDatabase.getInstance().getReference("Bildirimler")
                .child(gonderenId);

        HashMap<String,Object>hashMap = new HashMap<>();
        hashMap.put("kullaniciid",mevcutKullanici.getUid());
        hashMap.put("text","G??nderine Yorum Yapt??"+ edt_yorum_ekle.getText().toString());
        hashMap.put("gonderiid",gonderiId);
        hashMap.put("ispost",true);

        bildirimEklemeYolu.push().setValue(hashMap); // verilerin ??st ??ste binmemesi i??in.sonra verileri yola ekle
    }
}
