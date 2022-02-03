package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity {
    private TextView email;
    private Button button_logout;
    private ImageButton addClient,manageClient, createClass, manageClass, createWorkout, manageWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        button_logout = (Button) findViewById(R.id.button_logout);
        email = findViewById(R.id.email);
        addClient =(ImageButton) findViewById(R.id.addClient);
        manageClient =(ImageButton) findViewById(R.id.manageClient);
        createClass = (ImageButton) findViewById(R.id.createClass);
        manageClass = (ImageButton) findViewById(R.id.ManageClasses);
        createWorkout = (ImageButton) findViewById(R.id.createWorkOut);
        manageWorkout = (ImageButton) findViewById(R.id.manageWorkout);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            //email.setText(document.getString("Username"));
                            Ptrainer ptrainer = document.toObject(Ptrainer.class);
                            email.setText(ptrainer.getUsername());

                        }
                    }
                }
            });
            button_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Dashboard.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            });
            addClient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Dashboard.this, AddClient.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            });
            createWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Dashboard.this, create_workout.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            });
        }
    }

}