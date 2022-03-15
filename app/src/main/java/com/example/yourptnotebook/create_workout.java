package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    //Spinner setClients;
    Button addClients;
    EditText setExerciseName, workoutName;
    Exercise exercise;
    ArrayList<Exercise> exercises;
    ExerciseListAdapter exerciseListAdapter;
    private RecyclerView exerciseList;
    Button createWorkout;
    Workout workout;
    Button myWorkouts;
    Ptrainer ptrainer;
    ArrayList<Student> students;
    ArrayList<Student> workoutstudents = new ArrayList<>();
    TextView addedClients;
    boolean[] selectedClients;
    ArrayList<Integer> clientList = new ArrayList<>();
    String[] clientArray;


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
        myWorkouts = findViewById(R.id.myWorkouts);
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
        //setClients = findViewById(R.id.add_client_to_workout_spinner);
        addClients = findViewById(R.id.add_client_to_workout_button);
        addedClients = findViewById(R.id.list_clients_for_workout);



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
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    exerciseListAdapter.removeExercice(viewHolder.getAdapterPosition());
                }
            }).attachToRecyclerView(exerciseList);

            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ptrainer = document.toObject(Ptrainer.class);
                            students = ptrainer.students;
                            clientArray = new String[students.size()];
                            for(int i = 0; i< students.size();i++){
                                clientArray[i] = students.get(i).fullName;
                            }
                            selectedClients = new boolean[clientArray.length];
//                            ArrayAdapter adapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,students);
//                            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            setClients.setAdapter(adapter1);
                            addClients.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(create_workout.this);
                                    builder.setTitle("Add Clients to Workout");
                                    builder.setMultiChoiceItems(clientArray, selectedClients, new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                                            if(isChecked){
                                                clientList.add(which);
                                            }else{
                                                clientList.remove((Integer.valueOf(which)));
                                            }
                                        }
                                    });

                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            String clients = "";
                                            for(int i = 0; i < clientList.size(); i++){
                                                clients = clients + clientArray[clientList.get(i)];
                                                if(i !=clientList.size()-1){
                                                    clients = clients + ", ";
                                                }
                                            }
                                            addedClients.setText(clients);
                                        }
                                    });

                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            for(int  j = 0; j < selectedClients.length; j++){
                                                selectedClients[j] = false;
                                                clientList.clear();
                                                addedClients.setText("No Clients Added");
                                            }
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }
//
                            });

                            createWorkout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String clients = addedClients.getText().toString();
                                    List<String> addedClients = new ArrayList<String>(Arrays.asList(clients.split(", ")));

                                    workout = new Workout(workoutName.getText().toString(), exercises);
                                    ptrainer.workouts.add(workout);
                                    for (int i = 0; i < students.size(); i++) {
                                        if (addedClients.contains(students.get(i).fullName)) {
                                            ptrainer.students.get(i).workouts.add(workout);
                                        }
                                    }
                                    db.collection("ptrainer").document(currentUser.getUid())
                                            .set(ptrainer, SetOptions.merge());
                                }
                            });

                        }
                    }
                }
            });

            myWorkouts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("button is pressed");
                    Intent intent = new Intent(create_workout.this, ManageWorkouts.class);
                    startActivity(intent);
                }
            });


        }
    }

}