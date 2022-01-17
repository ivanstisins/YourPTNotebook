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

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    private EditText et_username, et_password;
    private String username, password;
    private Button login;
    //private FirebaseAuth mAuth;
    SessionManager sessionManager;
    private String URL = "http://10.0.2.2:80/YOurPTNoteBook/LogIn.php";

   public Login(){

   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getApplicationContext());
        setContentView(R.layout.activity_login);
        et_username = findViewById(R.id.LogUserName);
        et_password = findViewById(R.id.LogPassword);
        login = findViewById(R.id.LogButton);
       // mAuth = FirebaseAuth.getInstance();
        username = password = "";
    /*    login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                if(!username.equals("") && !password.equals("")){
                    loginUser(username,password);
                }
            }
        });
    }

    private void loginUser(String username, String password) {
       mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
*/
    }



        public void login(View v) {
            username = String.valueOf(et_username.getText());
            password = String.valueOf(et_password.getText());
            if(!username.equals("") && !password.equals("")){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        if (response.equals("Login Successful")) {
                            sessionManager.createSession(username);
                            Intent intent = new Intent(Login.this, Dashboard.class);
                            startActivity(intent);
                            finish();

                        } else if (response.equals("Username or Password is wrong")) {
                            Toast.makeText(Login.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> data = new HashMap<>();
                        data.put("Username", username);
                        data.put("Password", password);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
            else{
                Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            }
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