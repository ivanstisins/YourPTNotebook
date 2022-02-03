package com.example.yourptnotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class create_workout extends AppCompatActivity implements CreateExercise.CreateExerciseInterface{
    TextView textView;
    RelativeLayout relativeLayout;
    //private TextView eName, eSets, eReps;
    public void openAddExercise(View view){
        CreateExercise createExercise = new CreateExercise();
        createExercise.show(getSupportFragmentManager(),"Add Exercise");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        textView = new TextView(create_workout.this);
        relativeLayout = (RelativeLayout) findViewById(R.id.mylayout);
        //eName = findViewById(R.id.ExerciseName);
        //eSets = findViewById(R.id.ExerciseSets);
        //eReps = findViewById(R.id.ExerciseReps);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            //System.out.println(eName.getText());
            //relativeLayout.addView(textView);
        }
    }

    @Override
    public void applyText(String strExerciseName, String exerciseSets, String exerciseReps) {
        textView.setText(strExerciseName+"\n"+exerciseSets+"\n"+exerciseReps);
        if (textView.getParent() != null) {
            ViewGroup parent = (ViewGroup) textView.getParent();
            parent.removeAllViews();
        }
        relativeLayout.addView(textView);
    }
}