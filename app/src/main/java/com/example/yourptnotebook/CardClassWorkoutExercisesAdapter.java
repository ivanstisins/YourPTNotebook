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

public class CardClassWorkoutExercisesAdapter extends RecyclerView.Adapter<CardClassWorkoutExercisesAdapter.MyViewHolder>{
    Context context;
    ArrayList<Exercise> exerciseArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Ptrainer ptrainer;

    public CardClassWorkoutExercisesAdapter(Context context, ArrayList<Exercise> exerciseArrayList, Ptrainer ptrainer) {
        this.context = context;
        this.exerciseArrayList = exerciseArrayList;
        this.ptrainer = ptrainer;
    }
    @NonNull
    @Override
    public CardClassWorkoutExercisesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_class_workout_exercises,parent,false);
        return new CardClassWorkoutExercisesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardClassWorkoutExercisesAdapter.MyViewHolder holder, int position) {
        Exercise exercise = exerciseArrayList.get(position);
        holder.name.setText(exercise.getName());
        holder.sets.setText(exercise.getSets());
        holder.reps.setText(exercise.getReps());

    }

    @Override
    public int getItemCount() {
        return exerciseArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView reps;
        TextView sets;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardClassWorkoutExerciseName);
            sets = itemView.findViewById(R.id.cardClassWorkoutExerciseSets);
            reps = itemView.findViewById(R.id.cardClassWorkoutExerciseReps);
        }
    }


}
