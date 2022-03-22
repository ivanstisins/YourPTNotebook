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

public class CardClientClassAdapter extends RecyclerView.Adapter<CardClientClassAdapter.MyViewHolder> {

    Context context;
    ArrayList<Class> classArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Ptrainer ptrainer;

    public CardClientClassAdapter(Context context,ArrayList<Class> classArrayList, Ptrainer ptrainer) {
        this.context = context;
        this.ptrainer = ptrainer;
        this.classArrayList = classArrayList;
    }

    @NonNull
    @Override
    public CardClientClassAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_client_class_list,parent,false);
        return new CardClientClassAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CardClientClassAdapter.MyViewHolder holder, int position) {
        Class aClass = classArrayList.get(position);
        holder.name.setText(aClass.name);
        holder.date.setText(aClass.classDate);
        holder.type.setText(aClass.type);
        if(aClass.workout == null){
            holder.workout.setText("No Workouts Assigned");
        }
        else {
            holder.workout.setText(aClass.workout.name);
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
        TextView workout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardClientClassName);
            date = itemView.findViewById(R.id.cardClientClassDate);
            type = itemView.findViewById(R.id.cardClientClassType);
            workout = itemView.findViewById(R.id.cardClientClassWorkout);
        }
    }
}
