package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

public class CreateClass extends AppCompatActivity {
    EditText className, classDate;
    Spinner setWorkout;
    Button createClass;
    RadioGroup setType;
    Class aClass;
    RadioButton radioButton;
    Ptrainer ptrainer;
    ArrayList<Workout>workouts;
    ArrayList<Student> students;
    private Button myClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        className = findViewById(R.id.className);
        classDate = findViewById(R.id.classDate);
        setType = findViewById(R.id.class_type);
        createClass = findViewById(R.id.create_class);
        setWorkout = findViewById(R.id.select_workout);
        myClasses = findViewById(R.id.myClasses);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ptrainer = document.toObject(Ptrainer.class);
                            workouts = ptrainer.workouts;
                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,workouts);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            setWorkout.setAdapter(adapter);

                            createClass.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int radioId = setType.getCheckedRadioButtonId();
                                    radioButton = findViewById(radioId);
                                    Workout state = (Workout) setWorkout.getSelectedItem();
                                    aClass = new Class(className.getText().toString(), radioButton.getText().toString(),classDate.getText().toString(),ptrainer.getUsername(),state);
                                    ptrainer.classes.add(aClass);
                                    db.collection("Class").document(aClass.getName()).set(aClass);
                                    db.collection("ptrainer").document(currentUser.getUid())
                                            .set(ptrainer, SetOptions.merge());
                                }
                            });
                        }
                    }
                }
            });

            myClasses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateClass.this, ManageClasses.class);
                    startActivity(intent);
                }
            });
        }
    }
}