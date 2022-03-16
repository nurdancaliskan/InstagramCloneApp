package com.nurdancaliskan.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import Model.Kullanici;

public class ProfilDuzenleActivity extends AppCompatActivity {

    ImageView resim_kapatma, resim_profil;
    TextView txt_kaydet, txt_fotograf_degistir;
    MaterialEditText mEdt_ad, mEdt_kullaniciadi, mEdt_biyografi;

    FirebaseUser mevcutKullanici;
    private StorageTask yuklemeGorevi; // depolama görevi ayarladık
    private Uri mResimUri; //galeriden seçmek için ayarladık
    StorageReference depolamaYolu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_duzenle);

        resim_kapatma = findViewById(R.id.kapat_resmi_profilDuzenleActivity);
        resim_profil = findViewById(R.id.profil_resmi_profilDuzenleActivity);

        txt_kaydet = findViewById(R.id.txt_kaydet_profilDuzenleActivity);
        txt_fotograf_degistir = findViewById(R.id.txt_fotograf_degistir);

        mEdt_ad = findViewById(R.id.material_edt_text_profiliDuzenleActivity);
        mEdt_kullaniciadi = findViewById(R.id.material_edt_text_KullaniciAdi_profiliDuzenleActivity);
        mEdt_biyografi = findViewById(R.id.material_edt_text_Biyografi_profiliDuzenleActivity);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser(); //veritabanında o an işlem yapan kullanıcıyı tanısın ve onun profil resmini değiştirsin
        depolamaYolu = FirebaseStorage.getInstance().getReference("yuklemeler");

        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar")
                .child(mevcutKullanici.getUid());

        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                mEdt_ad.setText(kullanici.getAd());
                mEdt_kullaniciadi.setText(kullanici.getKullaniciadi()); //Veritabanında mevcut olan kullanıcıyı arayüze çekiyor.
                mEdt_biyografi.setText(kullanici.getBio());

                Glide.with(getApplicationContext()).load(kullanici.getResimurl()).into(resim_profil);  //resmi çekmek için bir kütüphanedir

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Kapat'a tıklama kodu
        resim_kapatma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });

        //Profil resmine tıklandığında
        txt_fotograf_degistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfilDuzenleActivity.this);

            }
        });

        //Kaydetmeye tıklandığında
        txt_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profiliGuncelle(mEdt_ad.getText().toString(),mEdt_kullaniciadi.getText().toString(),mEdt_biyografi.getText().toString());
                Toast.makeText(ProfilDuzenleActivity.this," Değişiklikler Kaydedildi",Toast.LENGTH_SHORT).show();
                finish();
            }

        });

    }

    private void profiliGuncelle(String ad, String kullaniciAdi, String biyografi)
    {
        DatabaseReference guncellemeYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar")
                .child(mevcutKullanici.getUid()); //Veritabanındaki kullanıcıları güncelleyecek

        HashMap<String,Object> kullanıcıGuncelleHashmap = new HashMap<>(); //Studio'dan Firebase'e veriyi aktarma

        kullanıcıGuncelleHashmap.put("ad",ad);
        kullanıcıGuncelleHashmap.put("kullaniciadi",kullaniciAdi);
        kullanıcıGuncelleHashmap.put("bio",biyografi);

        guncellemeYolu.updateChildren(kullanıcıGuncelleHashmap); //kullanıcıGuncelleHashmap i bulsun yukarıdakilerle güncellesin
    }

    private String dosyaUzantisiAl (Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap =MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void resimYukle()
    {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Yükleniyor..");
        pd.show();

        if(mResimUri != null)
        {
            StorageReference dosyaYolu = depolamaYolu.child(System.currentTimeMillis()
                    +"."+ dosyaUzantisiAl(mResimUri));

            yuklemeGorevi = dosyaYolu.putFile(mResimUri);
            yuklemeGorevi.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful())
                {
                    throw task.getException();
                }
                return dosyaYolu.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri indirmeUrisi = task.getResult();//görevin sonucunu al
                        String benimUrim = indirmeUrisi.toString();

                        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar")
                                .child(mevcutKullanici.getUid());

                        HashMap<String,Object> resimHashmap = new HashMap<>();
                        resimHashmap.put("resimurl","" + benimUrim);

                        kullaniciYolu.updateChildren(resimHashmap);
                        pd.dismiss();
                }
                    else
                    {
                        Toast.makeText(ProfilDuzenleActivity.this,"Yükleme Başarısız",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() { //başarısız ise
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ProfilDuzenleActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this,"Resim Seçilemedi",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // activity sonuçlandığında yapılacak işin kodları
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mResimUri = result.getUri();

            resimYukle();
        }

        else
        {
            Toast.makeText(this,"Bir şeyler yanlış gitti!",Toast.LENGTH_SHORT).show();

        }
    }
}