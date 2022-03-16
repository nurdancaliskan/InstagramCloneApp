package com.nurdancaliskan.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SeceneklerActivity extends AppCompatActivity {

    TextView txt_cikisyap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secenekler);

        txt_cikisyap = findViewById(R.id.txt_secenekler_cıkısyap);

        //toolbar ayarlamaları
        Toolbar toolbar = findViewById(R.id.toolbar_seceneklerActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Seçenekler");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_cikisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SeceneklerActivity.this,BaslangicActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

    }
}