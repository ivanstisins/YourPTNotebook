package com.example.yourptnotebook;

import java.util.ArrayList;

public class Workout {

    ArrayList<Student> students;
    String name;
    ArrayList<Exercise> exercises = new ArrayList<>();

    public Workout(String name,ArrayList<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
        this.students = new ArrayList<Student>();
    }

    public Workout() {
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }



    public ArrayList<Exercise> getExercises(){
        return exercises;
    }

    @Override
    public String toString() {
        return name;
    }
}
