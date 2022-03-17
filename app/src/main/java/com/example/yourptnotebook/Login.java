package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    private EditText et_email, et_password;
    private String email, password;
    private Button login;
    private FirebaseAuth mAuth;

   public Login(){

   }
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            setContentView(R.layout.activity_login);
            et_email = findViewById(R.id.LogUserEmail);
            et_password = findViewById(R.id.LogPassword);
            login = findViewById(R.id.LogButton);
            mAuth = FirebaseAuth.getInstance();
            email = password = "";
            if(firebaseAuth.getCurrentUser()!=null){
                startActivity(new Intent(getApplicationContext(),Dashboard.class));
                finish();
            }
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email = et_email.getText().toString();
                    password = et_password.getText().toString();

                    if(TextUtils.isEmpty(email)){
                        et_email.setError("Email can not be empty!");
                        et_email.requestFocus();
                    }
                    else if(TextUtils.isEmpty(password)){
                        et_password.setError("Password can not be empty!");
                        et_password.requestFocus();
                    }
                    else{
                        loginUser(email,password);
                    }
                }
            });
        }

        private void loginUser(String email, String password) {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(Login.this, Dashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    public void goToReg(View v) {
        Intent intent = new Intent(this, PTorStudActivity.class);
        startActivity(intent);
    }

    public void goToMain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}