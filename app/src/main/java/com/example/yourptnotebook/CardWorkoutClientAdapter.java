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

public class CardWorkoutClientAdapter extends RecyclerView.Adapter<CardWorkoutClientAdapter.MyViewHolder> {

    Context context;
    ArrayList<Student> clientArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Ptrainer ptrainer;

    public CardWorkoutClientAdapter(Context context, ArrayList<Student> clientArrayList, Ptrainer ptrainer) {
        this.context = context;
        this.clientArrayList = clientArrayList;
        this.ptrainer = ptrainer;
    }

    @NonNull
    @Override
    public CardWorkoutClientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_workout_client_list,parent,false);
        return new CardWorkoutClientAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardWorkoutClientAdapter.MyViewHolder holder, int position) {
            Student client = clientArrayList.get(position);
            holder.name.setText(client.fullName);
    }

    @Override
    public int getItemCount() {
        return clientArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardWorkoutClientName);
        }
    }
}
