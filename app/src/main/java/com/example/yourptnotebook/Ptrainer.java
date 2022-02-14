package com.example.yourptnotebook;

import java.util.ArrayList;

public class Ptrainer {
    String username;
    String email;
    String fullName;
    String gender;
    String gymName;
    String password;
    String id;
    String age;
    ArrayList<Student> students;
    ArrayList<Workout> workouts;



    public Ptrainer(String username, String email, String fullName, String age, String gender, String gymName, String password, String id) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.gymName = gymName;
        this.password = password;
        this.id = id;
        this.age =age;
        this.students = new ArrayList<Student>();
        this.workouts = new ArrayList<Workout>();
    }

    public Ptrainer(){

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

    public  String getAge(){return age;}

    public String getGymName() {
        return gymName;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public void addStudent(Student s){
        students.add(s);
    }

    public ArrayList<Student> getStudents(){
        return students;
    }

    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

}
