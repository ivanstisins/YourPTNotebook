package com.example.yourptnotebook;

import android.annotation.SuppressLint;
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

public class CardClientWorkoutAdapter extends RecyclerView.Adapter<CardClientWorkoutAdapter.MyViewHolder> {

    Context context;
    ArrayList<Workout> workoutArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Ptrainer ptrainer;

    public CardClientWorkoutAdapter(Context context, ArrayList<Workout> workoutArrayList, Ptrainer ptrainer) {
        this.context = context;
        this.workoutArrayList = workoutArrayList;
        this.ptrainer = ptrainer;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_client_workout_list,parent,false);
        return new CardClientWorkoutAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Workout workout = workoutArrayList.get(position);
        String exercises ="";
        holder.workoutName.setText(workout.name);
        for(int i = 0; i< workout.exercises.size();i++){
            exercises += workout.exercises.get(i).toString();
            //holder.exercise.setText(workout.exercises.get(i).toString());
        }
        holder.exercise.setText(exercises);
    }

    @Override
    public int getItemCount() {
        return workoutArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView workoutName;
        TextView exercise;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.cardClientWoName);
            exercise = itemView.findViewById(R.id.cardClientWoExercise);


        }
    }
}
