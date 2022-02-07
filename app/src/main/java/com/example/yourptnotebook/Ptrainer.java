package com.example.yourptnotebook;

public class Ptrainer {
    String username;
    String email;
    String fullName;
    String gender;
    String gymName;
    String password;
    String id;
    String age;

    public Ptrainer(String username, String email, String fullName,String age,String gender, String gymName, String password, String id) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.gymName = gymName;
        this.password = password;
        this.id = id;
        this.age =age;
    }

    public Ptrainer(){

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
}
