package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class create_workout extends AppCompatActivity /*implements CreateExercise.CreateExerciseInterface*/{
    TextView textView;
    Button addExercise;
    Spinner setSets;
    Spinner setReps;
    EditText setExerciseName, workoutName;
    Exercise exercise;
    ArrayList<Exercise> exercises;
    ExerciseListAdapter exerciseListAdapter;
    private RecyclerView exerciseList;
    Button createWorkout;
    Workout workout;
    ImageButton backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        exerciseList = findViewById(R.id.ExerciseList);
        exerciseList.setHasFixedSize(true);
        exerciseList.setLayoutManager(new LinearLayoutManager(this));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        textView = new TextView(create_workout.this);
        setExerciseName = findViewById(R.id.setExerciseName);
        addExercise = findViewById(R.id.addExercise);
        backbutton = findViewById(R.id.backbutton);
        createWorkout = findViewById(R.id.createWorkout);
        workoutName = findViewById(R.id.WorkoutName);
        exercises = new ArrayList<>();
        List<Integer> sets = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        List<Integer> reps = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
        setSets = findViewById(R.id.setSetsSpinner);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,sets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSets.setAdapter(adapter);
        setReps = findViewById(R.id.setRepsSpinner);
        ArrayAdapter adapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,reps);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setReps.setAdapter(adapter1);



        if(currentUser != null){
            addExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String strsetExerciseName = setExerciseName.getText().toString();
                    String strSets = setSets.getSelectedItem().toString();
                    String strReps = setReps.getSelectedItem().toString();
                    exercise = new Exercise();
                    exercise.setName(strsetExerciseName);
                    exercise.setSets(strSets);
                    exercise.setReps(strReps);
                    exercises.add(exercise);
                    exerciseListAdapter.notifyDataSetChanged();

                }
            });
            exercises = new ArrayList<>();
            exerciseListAdapter = new ExerciseListAdapter(create_workout.this,exercises);
            exerciseList.setAdapter(exerciseListAdapter);

            createWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    workout = new Workout(workoutName.getText().toString(), exercises);
                    System.out.println(workout.getName());

                    DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    //email.setText(document.getString("Username"));
                                    Ptrainer ptrainer = document.toObject(Ptrainer.class);
                                    ptrainer.workouts.add(workout);
                                    db.collection("ptrainer").document(currentUser.getUid())
                                            .set(ptrainer, SetOptions.merge());
                                }
                            }
                        }
                    });
                }
            });

            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("button is pressed");
                    Intent intent = new Intent(create_workout.this, Dashboard.class);
                    startActivity(intent);
                }
            });


        }
    }

}