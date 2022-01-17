package com.example.yourptnotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button regButton;
    private Button logButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regButton =(Button) findViewById(R.id.RegisterButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPTorStudActivity();
            }
        });

        logButton =(Button) findViewById(R.id.LoginButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogPage();
            }
        });
    }

    public void openPTorStudActivity(){
        Intent intent = new Intent(this, PTorStudActivity.class);
        startActivity(intent);
    }

    public void openLogPage(){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
}