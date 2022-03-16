package com.nurdancaliskan.instagramclone;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class GonderiActivity extends AppCompatActivity {

    Uri resimUri;//uri ile resmi galeriden seçeriz
    String myUri = "";
    Uri gonderiResimUri;

    StorageTask<?> yuklemeGorevi;
    StorageReference resimYukleYolu;

    ImageView image_Kapat, image_Eklendi;
    TextView txt_Gonder;
    EditText edt_Gonderi_Hakkinda;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonderi);

        image_Kapat = findViewById(R.id.close_Gonderi);
        image_Eklendi = findViewById(R.id.eklenen_Resim_Gonderi);

        txt_Gonder = findViewById(R.id.txt_Gonder);
        edt_Gonderi_Hakkinda = findViewById(R.id.edt_Gonderi_Hakkinda);

        resimYukleYolu = FirebaseStorage.getInstance().getReference("gonderiler");

        //Kapat'a tıkladığımda gönderi sayfasını kapatıp bizi anasayfaya gönderecek kodlar

        image_Kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GonderiActivity.this, AnasayfaActivity.class));
                finish();
            }
        });

        //Gönder'e bastığımda gidecek kodlar

        txt_Gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimYukle();  //tıkladığında bu methodu çağıracak
            }
        });

        //Resmi kırpma özelliği

        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(GonderiActivity.this);

        /*if (!getIntent().getExtras().getString("Result").equals("Cropped")){

        }else
            gonderiResimUri = getIntent().getExtras().getParcelable("Uri");
            image_Eklendi.setImageURI(gonderiResimUri);
            resimUri = gonderiResimUri;*/
    }

    private String dosyaUzantisiAl(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void resimYukle() {

        ProgressDialog progressDialog = new ProgressDialog(this);//dönüp lütfen bekleyin diyen şey
        progressDialog.setMessage("Gönderiliyor..");
        progressDialog.show();
        //Resim yükleme kodları

        if (resimUri != null) //Resim Uri Boş değilse tüm aşağıdakileri yapacak.
        {
            StorageReference dosyaYolu = resimYukleYolu.child(System.currentTimeMillis()
                    +"."+dosyaUzantisiAl(resimUri));

            yuklemeGorevi = dosyaYolu.putFile(resimUri);
            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException(); //hata açsın görev başarısızsa
                    }
                    return dosyaYolu.getDownloadUrl();
                }
            }).addOnCompleteListener(new  OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri indirmeUri = task.getResult();
                        myUri = indirmeUri.toString();

                        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
                        String gonderiId = veriYolu.push().getKey();

                        HashMap<String,Object> hashMap = new HashMap<>();//çoklu göndeririyi veritabanına göndermek için listeye benzer bir yapıdır

                        hashMap.put("gonderiId",gonderiId);
                        hashMap.put("gonderiResmi",myUri);
                        hashMap.put("gonderiHakkinda",edt_Gonderi_Hakkinda.getText().toString());
                        hashMap.put("gonderen", FirebaseAuth.getInstance().getCurrentUser().getUid()); // o an kullanan kullanıcının id sini alıp gönderir.
                        veriYolu.child(gonderiId).setValue(hashMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(GonderiActivity.this,AnasayfaActivity.class));
                        finish(); //geri tuşuna bastığımda bir daha geri gelmesin

                    }
                    else
                    {
                        Toast.makeText(GonderiActivity.this, "Gönderme Başarısız!",Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GonderiActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
        }
        //Resim Uri Boşsa tüm aşağıdakileri yapacak.
        else
        {
            Toast.makeText(this,"Seçilen resim yok!",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Galeriden resim seçmek için gereken kodlar

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            resimUri = result.getUri();
            image_Eklendi.setImageURI(resimUri);
            Intent intent = new Intent(GonderiActivity.this, GonderiActivity.class);
            intent.putExtra("Uri",resimUri);
            intent.putExtra("Result","Cropped");
            //startActivity(intent);
            //finish();
        } else {
            Toast.makeText(this, "Resim Seçilemedi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GonderiActivity.this, AnasayfaActivity.class));
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}