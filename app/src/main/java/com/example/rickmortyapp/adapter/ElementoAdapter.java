package com.example.rickmortyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rickmortyapp.MainActivity;
import com.example.rickmortyapp.R;
import com.example.rickmortyapp.modelo.Elemento;
import com.squareup.picasso.Picasso;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ElementoAdapter extends
        RecyclerView.Adapter<ElementoAdapter.MyViewHolder> {
    private ArrayList<Elemento> lista;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onMeGustaClick(Elemento elemento);
        void onFavoritoClick(Elemento elemento);
        void onItemClick(View v);
    }
    public ElementoAdapter(ArrayList<Elemento> lista, MainActivity mainActivity) { this.lista = lista; }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_elemento,
                parent, false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Elemento e = lista.get(position);
        holder.tv1.setText(e.getAtriString1());
        holder.tv2.setText(e.getAtriString2());
        if (e.getAtriString3() != null && !e.getAtriString3().isEmpty()) {
            Picasso.get().load(e.getAtriString3())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher_round)
                    .into(holder.iv1);
        }
        holder.btnFavorito.setOnClickListener(v -> {
            if (listener != null) listener.onFavoritoClick(e);
        });
        holder.itemView.setOnClickListener(v -> listener.onItemClick(v));
    }
    @Override
    public int getItemCount() { return lista.size(); }

    public void actualizarlista(List<Elemento> descargados) {
        this.lista = new ArrayList<>(descargados);
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View btnFavorito;
        TextView tv1, tv3, tv2;
        ImageView iv1;
        public MyViewHolder(@NonNull View v) {
            super(v);

            tv1 = v.findViewById(R.id.tvString1);
            tv2 = v.findViewById(R.id.tvString2);
            iv1 = v.findViewById(R.id.tvString3);


        }
    }
}