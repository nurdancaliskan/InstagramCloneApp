package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nurdancaliskan.instagramclone.Gonderi;
import com.nurdancaliskan.instagramclone.R;

import java.util.List;

import Cerceve.GonderiDetayiFragment;

public class FotografAdapter extends RecyclerView.Adapter<FotografAdapter.ViewHolder> {
    public Context context;
    public List <Gonderi> mGonderiler;

    public FotografAdapter(Context context, List<Gonderi> mGonderiler) {
        this.context = context;
        this.mGonderiler = mGonderiler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fotograflar_ogesi, parent,false); //fotograflar_ogesi ni bağladık.
        return new FotografAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Gonderi modelinden nesne alıyoruz

        Gonderi gonderi = mGonderiler.get(position);

        Glide.with(context).load(gonderi.getGonderiResmi()).into(holder.gonderi_resmi);

        holder.gonderi_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",gonderi.getGonderiId());
                editor.apply();

                //Profil resmine tıkladığında gideceği yer

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new GonderiDetayiFragment()).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mGonderiler.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView gonderi_resmi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gonderi_resmi = itemView.findViewById(R.id.gonderi_resmi_fotograflar_ogesi);

        }
    }
}


