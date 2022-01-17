package com.example.yourptnotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PTorStudActivity extends AppCompatActivity {

    private Button ptButton;
    private Button studButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptor_stud);

        ptButton = (Button)findViewById(R.id.PTRegister);
        ptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenPTReg();
            }
        });

        studButton = (Button) findViewById(R.id.studRegister);
        studButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenStudReg();
            }
        });
    }

    public void OpenPTReg(){
        Intent intent= new Intent(this, PTRegistration.class);
        startActivity(intent);
    }

    public void OpenStudReg(){
        Intent intent = new Intent(this,StudRegistration.class);
        startActivity(intent);
    }
}