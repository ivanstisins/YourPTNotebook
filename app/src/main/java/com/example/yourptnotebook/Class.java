package com.example.yourptnotebook;

import java.util.ArrayList;
import java.util.Date;

public class Class {

    String name;
    String type;
    String classDate;
    ArrayList<String> students;
    Workout workout;
    String createdBy;

    public Class(String name, String type, String classDate,String createdBy,Workout workout) {
        this.name = name;
        this.type = type;
        this.classDate = classDate;
        this.students = new ArrayList<>();
        this.workout = workout;
        this.createdBy = createdBy;
    }

    public Class(){

    }

    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getClassDate() {
        return classDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ArrayList<String> getStudents() {
        return students;
    }

    public Workout getWorkout() {
        return workout;
    }

    @Override
    public String toString() {
        return name;
    }
}
