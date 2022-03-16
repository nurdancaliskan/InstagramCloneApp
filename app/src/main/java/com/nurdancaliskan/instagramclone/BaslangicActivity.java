package com.nurdancaliskan.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class BaslangicActivity extends AppCompatActivity {

    Button btn_baslangicGiris;
    Button btn_baslangicKaydol;

    FirebaseUser baslangicKullanici;

    @Override
    protected void onStart() {
        super.onStart();

        baslangicKullanici = FirebaseAuth.getInstance().getCurrentUser();

        //Eğer kullanıcı veri tababanında varsa direkt anasayfaya gönder

        if (baslangicKullanici != null)
        {
            startActivity(new Intent(BaslangicActivity.this,AnasayfaActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baslangic);

        btn_baslangicGiris = findViewById(R.id.btn_giris);
        btn_baslangicKaydol = findViewById(R.id.btn_kaydol);

        btn_baslangicGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(BaslangicActivity.this,GirisActivity.class));
            }
        });

        btn_baslangicKaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BaslangicActivity.this,KaydolActivity.class));
            }
        });

    }
}