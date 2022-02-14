package com.example.yourptnotebook;

import java.util.ArrayList;

public class Workout {

    String name;
    ArrayList<Exercise> exercises = new ArrayList<>();

    public Workout(String name,ArrayList<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
    }

    public Workout() {
    }

    public ArrayList<Exercise> getExercises(){
        return exercises;
    }

}
