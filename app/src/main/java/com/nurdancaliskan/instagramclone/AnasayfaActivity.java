package com.nurdancaliskan.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import Cerceve.HomeFragment;

public class AnasayfaActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment seciliCerceve = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new HomeFragment()).commit(); // açıldığında ilk çerçeve home çerçevesi olacak

    }
        //hangi id'ye tıkladığında ne yapacak aşağıdaki anasayfadaki seçeneklerden
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId())  //menüdeki öğelerin id sini aldım
                    {
                        case R.id.nav_home:
                            //Ana çerçeveyi çağırır

                            seciliCerceve = new HomeFragment();

                            break;

                        case R.id.nav_arama:
                            //Arama çerçevesini çağırır

                            seciliCerceve= new AramaFragment();

                            break;

                        case R.id.nav_ekle:
                            // Boş çerçeve olacak Gonderi activitye gitsin
                            Intent intent = new Intent(AnasayfaActivity.this, GonderiActivity.class);
                            intent.putExtra("Result","homepage");
                            startActivity(intent);
                            break;

                        case R.id.nav_kalp:
                            //Bildirim çerçevesini çağırır

                            seciliCerceve = new BildirimFragment();

                            break;

                        case R.id.nav_profil:

                            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid()); //mevcut kullanıcının her kullanıcıya verdiği Id
                            editor.apply();

                            //Profil çerçevesini çağırır
                            seciliCerceve = new ProfilFragment();

                            break;
                    }

                    if (seciliCerceve != null)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,seciliCerceve).commit();
                    }

                    return true;

                }
            };
}