package com.example.yourptnotebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Exercise> exercises;

    public ExerciseListAdapter(Context context, ArrayList<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exercise_list,parent,false);
        return new ExerciseListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseListAdapter.MyViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.exercisen.setText(exercise.getName());
        holder.exercisese.setText(exercise.getSets());
        holder.exercisere.setText(exercise.getReps());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView exercisen;
        TextView exercisese;
        TextView exercisere;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            exercisen = itemView.findViewById(R.id.exerciseName);
            exercisese = itemView.findViewById(R.id.exerciseSets);
            exercisere = itemView.findViewById(R.id.exerciseReps);
        }
    }
}
