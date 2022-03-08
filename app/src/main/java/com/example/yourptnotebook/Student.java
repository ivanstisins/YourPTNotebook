package com.example.yourptnotebook;

import java.util.ArrayList;

public class Student {
    String username;
    String email;
    String fullName;
    String gender;
    String gymName;
    String password;
    String id;
    String age;
    String height;
    String weight;
    boolean isRegistered;
    ArrayList<Workout> workouts;
    ArrayList<Class> classes;


    public Student(String username, String email, String fullName, String gender, String gymName, String password, String id, String age, String height, String weight, boolean isRegistered) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.gymName = gymName;
        this.password = password;
        this.id = id;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.isRegistered = isRegistered;
        this.classes = new ArrayList<Class>();
        this.workouts = new ArrayList<Workout>();
    }

    public  Student(){

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getGymName() {
        return gymName;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getAge() {
        return age;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

//    public void AddClasses(Class aClass) {
//        this.classes.add(aClass);
//    }

    public void setClasses(ArrayList<Class> classes) {
        this.classes = classes;
    }

    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
