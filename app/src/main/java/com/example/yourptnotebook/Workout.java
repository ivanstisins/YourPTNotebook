package com.example.yourptnotebook;

import java.util.ArrayList;

public class Workout {

    String name;
    ArrayList<Exercise> exercises = new ArrayList<>();

    public Workout(String name) {
        this.name = name;
    }

    public void addExercise(Exercise e){
        exercises.add(e);
    }

    public ArrayList<Exercise> getExercises(){
        return exercises;
    }

}
