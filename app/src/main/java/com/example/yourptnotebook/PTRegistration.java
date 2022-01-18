package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class PTRegistration extends AppCompatActivity {
    private EditText et_name, et_username, et_age,et_gender,et_gymname, et_email, et_password;
    private String name,username,age,gender,gymname,email,password;
    private Button register;
    private DocumentReference db;
    private FirebaseAuth mAuth;

    public PTRegistration() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptregistration);
        et_name = findViewById(R.id.FullName);
        et_username = findViewById(R.id.PTUsername);
        et_age = findViewById(R.id.Age);
        et_gender = findViewById(R.id.PtGender);
        et_gymname = findViewById(R.id.GymName);
        et_email = findViewById(R.id.PTEmail);
        et_password = findViewById(R.id.PTPassword);
        register = findViewById(R.id.PTsignUp);
        name = username = age = gender =gymname = email = password = "";

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = String.valueOf(et_name.getText());
                username = String.valueOf(et_username.getText());
                age = String.valueOf(et_age.getText());
                gender = String.valueOf(et_gender.getText());
                gymname = String.valueOf(et_gymname.getText());
                email = String.valueOf(et_email.getText());
                password = String.valueOf(et_password.getText());
                if(!name.equals("") && !username.equals("") && !age.equals("") && !gender.equals("") && !gymname.equals("") && !email.equals("") && !password.equals("")){
                   registerPt(name,username,age,gender,gymname,email,password);
                }
            }
        });

    }
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }

    }


    private void registerPt(String name, String username, String age, String gender, String gymname, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> data = new HashMap<>();
                    data.put("Username", username);
                    data.put("FullName", name);
                    data.put("Age", age);
                    data.put("Gender", gender);
                    data.put("GymName", gymname);
                    data.put("Email", email);
                    data.put("Password", password);
                    data.put("id", mAuth.getCurrentUser().getUid());
                    db.collection("ptrainer").add(data);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PTRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void openLogIn(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void goToLogIn(View v) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}