package com.example.yourptnotebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CardClassClientsAdapter extends RecyclerView.Adapter<CardClassClientsAdapter.MyViewHolder>{

    Context context;
    ArrayList<String> clientArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Ptrainer ptrainer;

    public CardClassClientsAdapter(Context context, ArrayList<String> clientArrayList, Ptrainer ptrainer) {
        this.context = context;
        this.clientArrayList = clientArrayList;
        this.ptrainer = ptrainer;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_class_clients,parent,false);
        return new CardClassClientsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String client = clientArrayList.get(position);
        holder.name.setText(client);

    }

    @Override
    public int getItemCount() {
        return clientArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardClassClientName);
        }
    }
}
