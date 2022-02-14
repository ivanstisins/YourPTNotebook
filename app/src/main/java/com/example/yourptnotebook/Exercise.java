package com.example.yourptnotebook;

public class Exercise {
    String name;
    String sets;
    String reps;
    public Exercise(String name, String sets, String reps) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
    }

    public  Exercise(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getName() {
        return name;
    }

    public String getSets() {
        return sets;
    }

    public String getReps() {
        return reps;
    }
}
