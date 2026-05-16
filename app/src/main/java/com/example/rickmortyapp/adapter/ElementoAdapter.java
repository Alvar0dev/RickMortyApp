package com.example.rickmortyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rickmortyapp.R;
import com.example.rickmortyapp.bbdd.ElementoDAO;
import com.example.rickmortyapp.modelo.Elemento;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ElementoAdapter extends RecyclerView.Adapter<ElementoAdapter.MyViewHolder> {
    private ArrayList<Elemento> lista;
    private OnItemClickListener listener;
    private boolean esFavoritos; 
    private ElementoDAO dao;

    public interface OnItemClickListener {
        void onFavoritoClick(Elemento elemento);
        void onItemClick(Elemento elemento);
        default void onModificarClick(Elemento elemento) {}
        default void onBorrarClick(Elemento elemento) {}
    }

    public ElementoAdapter(ArrayList<Elemento> lista, OnItemClickListener listener, boolean esFavoritos) {
        this.lista = lista;
        this.listener = listener;
        this.esFavoritos = esFavoritos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (dao == null) {
            dao = new ElementoDAO(parent.getContext());
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_elemento, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Elemento e = lista.get(position);
        holder.tv1.setText(e.getAtriString1()); 
        holder.tv2.setText(e.getAtriString2()); 

        if (e.getAtriString3() != null && !e.getAtriString3().isEmpty()) {
            Picasso.get().load(e.getAtriString3()).placeholder(R.mipmap.ic_launcher).into(holder.iv1);
        }

        if (esFavoritos) {
            holder.btnMenu.setVisibility(View.VISIBLE);
            holder.btnFavorito.setVisibility(View.GONE);
        } else {
            holder.btnMenu.setVisibility(View.GONE);
            holder.btnFavorito.setVisibility(View.VISIBLE);
            if (dao.existePorApi(e.getAtriInt1())) {
                holder.btnFavorito.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                holder.btnFavorito.setImageResource(android.R.drawable.btn_star_big_off);
            }
        }

        holder.btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), holder.btnMenu);
            popup.getMenuInflater().inflate(R.menu.item_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_modificar) {
                    if (listener != null) listener.onModificarClick(e);
                    return true;
                } else if (item.getItemId() == R.id.menu_borrar) {
                    if (listener != null) listener.onBorrarClick(e);
                    return true;
                }
                return false;
            });
            popup.show();
        });

        holder.btnFavorito.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoritoClick(e);
                notifyItemChanged(holder.getBindingAdapterPosition());
            }
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageButton btnFavorito, btnMenu;
        public TextView tv1, tv2;
        public ImageView iv1;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tv1 = v.findViewById(R.id.tvString1);
            tv2 = v.findViewById(R.id.tvString2);
            iv1 = v.findViewById(R.id.tvString3);
            btnFavorito = v.findViewById(R.id.btnFavorito);
            btnMenu = v.findViewById(R.id.btnMenu); 
        }
    }
}