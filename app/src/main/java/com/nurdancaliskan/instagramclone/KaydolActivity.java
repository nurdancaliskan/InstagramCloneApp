package com.nurdancaliskan.instagramclone;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class KaydolActivity extends AppCompatActivity {

    EditText edt_kullaniciAdi, edt_Ad, edt_Soyad, edt_Email, edt_Sifre;

    Button btn_Kaydol;
    TextView txt_GirisSayfasinaGit;
    FirebaseAuth yetki;
    DatabaseReference yol;
    ProgressDialog pd;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydol);

        edt_kullaniciAdi = findViewById(R.id.edt_kullaniciAdi);
        edt_Ad = findViewById(R.id.edt_Ad);
        edt_Soyad = findViewById(R.id.edt_Soyad);
        edt_Email = findViewById(R.id.edt_Email);
        edt_Sifre = findViewById(R.id.edt_Sifre);

        btn_Kaydol=findViewById(R.id.btn_Kaydol_activity);
        txt_GirisSayfasinaGit=findViewById(R.id.txt_GirisSayfasina_git);
        yetki=FirebaseAuth.getInstance();

        txt_GirisSayfasinaGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KaydolActivity.this, GirisActivity.class));
            }
        });

        btn_Kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(KaydolActivity.this);
                pd.setMessage("Lütfen bekleyin..");
                pd.show();

                String str_kullaniciAdi = edt_kullaniciAdi.getText().toString();
                String str_Ad = edt_Ad.getText().toString();
                String str_Soyad = edt_Soyad.getText().toString();
                String str_Email = edt_Email.getText().toString();
                String str_Sifre = edt_Sifre.getText().toString();

                if (TextUtils.isEmpty(str_kullaniciAdi) || TextUtils.isEmpty(str_Ad) || TextUtils.isEmpty(str_Sifre) || TextUtils.isEmpty(str_Soyad)
                        || TextUtils.isEmpty(str_Email)) {
                    Toast.makeText(KaydolActivity.this, "Lütfen bütün alanları doldurun", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else if (str_Sifre.length() < 6) {
                    Toast.makeText(KaydolActivity.this, "Şifreniz minimum 6 karakter olmalıdır", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else {
                    //Yeni kullanıcı kaydetme kodları

                    kaydet(str_kullaniciAdi, str_Ad, str_Soyad, str_Email, str_Sifre);

                }
            }
        });
    }

    private void kaydet(String kullaniciadi, String ad, String soyad, String email, String sifre) {
        //Yeni kullanıcı kaydetme
        yetki.createUserWithEmailAndPassword(email, sifre).addOnCompleteListener(KaydolActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser firebaseKullanici = yetki.getCurrentUser();

                    String kullaniciId = firebaseKullanici.getUid();
                    yol = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(kullaniciId);
                    HashMap<String, Object> hashmap = new HashMap<>();   // Birden fazla veriyi aynı anda kullanmamızı sağlar.
                    hashmap.put("id", kullaniciId);
                    hashmap.put("kullaniciadi", kullaniciadi.toLowerCase());  //Küçük harfe çevirir.
                    hashmap.put("ad", ad);
                    hashmap.put("soyad", soyad);
                    hashmap.put("bio", "");
                    hashmap.put("resimurl", "https://firebasestorage.googleapis.com/v0/b/instagram-clone-a98de.appspot.com/o/resimurl.png?alt=media&token=83b1d5dc-3167-4568-ac3d-383f42ac03b1");

                    Task<Void> voidTask = yol.setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                Intent intent = new Intent(KaydolActivity.this, AnasayfaActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(KaydolActivity.this, "Bu mail veya şifre ile kayıt başarısız", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
