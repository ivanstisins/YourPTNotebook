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

public class CardWorkoutClassAdapter extends RecyclerView.Adapter<CardWorkoutClassAdapter.MyViewHolder>{

    Context context;
    ArrayList<Class> classArrayList;
    Ptrainer ptrainer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public CardWorkoutClassAdapter(Context context, ArrayList<Class> classArrayList, Ptrainer ptrainer) {
        this.context = context;
        this.classArrayList = classArrayList;
        this.ptrainer = ptrainer;;
    }

    @NonNull
    @Override
    public CardWorkoutClassAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_workout_class_list,parent,false);
        return new CardWorkoutClassAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardWorkoutClassAdapter.MyViewHolder holder, int position) {
        String clientString = "";
        Class aClass = classArrayList.get(position);
        holder.name.setText(aClass.name);
        holder.date.setText(aClass.classDate);
        holder.type.setText(aClass.type);
        for(int i = 0; i< aClass.students.size();i++){
            clientString += aClass.students.get(i)+" ";
        }

        holder.clients.setText(clientString);
        if(aClass.students.isEmpty()){
            holder.clients.setText("No Clients Assigned");
        }
        else if(aClass.students.get(0).equals("")){
            holder.clients.setText("No Clients Assigned");
        }
        else {
            holder.clients.setText(clientString);
        }

    }

    @Override
    public int getItemCount() {
        return classArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView date;
        TextView type;
        TextView clients;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardWorkoutClassName);
            date = itemView.findViewById(R.id.cardWorkoutClassDate);
            type = itemView.findViewById(R.id.cardWorkoutClassType);
            clients = itemView.findViewById(R.id.cardWorkoutClassClients);
        }
    }
}
