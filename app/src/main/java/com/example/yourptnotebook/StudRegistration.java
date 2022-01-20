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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StudRegistration extends AppCompatActivity {
    private EditText et_name, et_username, et_age,et_height,et_weight,et_gender,et_gymname, et_email, et_password;
    //private String URL = "http://10.0.2.2:80/YOurPTNoteBook/Stud_register.php";
    private String name,username,age,height,weight,gender,gymname,email,password;
    private Button register;
    private DocumentReference db;
    private FirebaseAuth mAuth;

    public StudRegistration(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_registration);
        et_name = findViewById(R.id.StudName);
        et_username = findViewById(R.id.StudUsername);
        et_age = findViewById(R.id.StudAge);
        et_height = findViewById(R.id.Height);
        et_weight = findViewById(R.id.Weight);
        et_gender = findViewById(R.id.StudGender);
        et_gymname = findViewById(R.id.StudGymName);
        et_email = findViewById(R.id.StudEmail);
        et_password = findViewById(R.id.StudPassword);
        register = findViewById(R.id.StudSignUp);
        name = username = age = gender=height = weight =gymname = email = password = "";

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = String.valueOf(et_name.getText());
                username = String.valueOf(et_username.getText());
                age = String.valueOf(et_age.getText());
                height= String.valueOf(et_height.getText());
                weight = String.valueOf(et_weight.getText());
                gender = String.valueOf(et_gender.getText());
                gymname = String.valueOf(et_gymname.getText());
                email = String.valueOf(et_email.getText());
                password = String.valueOf(et_password.getText());
                if(!name.equals("") && !username.equals("") && !age.equals("")&& !height.equals("")&& !weight.equals("") && !gender.equals("") && !gymname.equals("") && !email.equals("") && !password.equals("")){
                    Studregister(name,username,age,height,weight,gender,gymname,email,password);
                }
            }
        });
    }

    public void Studregister(String name, String username, String age,String height,String weight, String gender, String gymname, String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> data = new HashMap<>();
                    data.put("Username", username);
                    data.put("FullName", name);
                    data.put("Age", age);
                    data.put("Height", height);
                    data.put("Weight",weight);
                    data.put("Gender", gender);
                    data.put("GymName", gymname);
                    data.put("Email", email);
                    data.put("Password", password);
                    data.put("id", mAuth.getCurrentUser().getUid());
                    db.collection("student").document(mAuth.getCurrentUser().getUid()).set(data);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Intent intent = new Intent(StudRegistration.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void goToLogIn(View v) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}