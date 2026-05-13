package com.example.rickmortyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rickmortyapp.R;
import com.example.rickmortyapp.modelo.Elemento;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ElementoAdapter extends RecyclerView.Adapter<ElementoAdapter.MyViewHolder> {
    private ArrayList<Elemento> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onFavoritoClick(Elemento elemento);
        void onItemClick(Elemento elemento); // Cambiado para pasar el objeto directamente
    }

    public ElementoAdapter(ArrayList<Elemento> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener; // ¡IMPORTANTE! Guardar el listener
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_elemento, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Elemento e = lista.get(position);
        holder.tv1.setText(e.getAtriString1()); // Nombre
        holder.tv2.setText(e.getAtriString2()); // Estado

        if (e.getAtriString3() != null && !e.getAtriString3().isEmpty()) {
            Picasso.get().load(e.getAtriString3()).into(holder.iv1);
        }

        holder.btnFavorito.setOnClickListener(v -> {
            if (listener != null) listener.onFavoritoClick(e);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(e);
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public void actualizarlista(List<Elemento> descargados) {
        this.lista.clear();
        this.lista.addAll(descargados);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View btnFavorito;
        TextView tv1, tv2;
        ImageView iv1;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tv1 = v.findViewById(R.id.tvString1);
            tv2 = v.findViewById(R.id.tvString2);
            iv1 = v.findViewById(R.id.tvString3); // Tu ImageView tiene este ID en el XML
            btnFavorito = v.findViewById(R.id.btnFavorito);
        }
    }
}