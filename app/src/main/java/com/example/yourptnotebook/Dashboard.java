package com.example.yourptnotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity {
    private TextView username;
    private Button button_logout;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        button_logout = (Button) findViewById(R.id.button_logout);
        username=findViewById(R.id.username);

        HashMap<String, String> user = sessionManager.getUserDetails();
        String musername = user.get(sessionManager.USERNAME);

        username.setText(musername);

        button_logout.setOnClickListener((v) -> {sessionManager.logout();});
    }
}