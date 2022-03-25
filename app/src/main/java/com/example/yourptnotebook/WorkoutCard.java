package com.example.yourptnotebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class WorkoutCard extends AppCompatActivity {
    TextView workoutCardName;
    FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    Ptrainer ptrainer;
    Button removeWorkout;

    RecyclerView cardWorkoutExerciseList;
    CardWorkoutExercisesAdapter cardWorkoutExercisesAdapter;
    ArrayList<Exercise> exerciseArrayList;


    RecyclerView cardWorkoutClientList;
    CardWorkoutClientAdapter cardWorkoutClientAdapter;
    ArrayList<Student> clientArrayList;

    RecyclerView cardWorkoutClassList;
    CardWorkoutClassAdapter cardWorkoutClassAdapter;
    ArrayList<Class> classArrayList;

    ViewFlipper viewFlipper;

    Button viewExercise;
    Button viewClass;
    Button viewClients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_card);
        db = FirebaseFirestore.getInstance();

        String name = getIntent().getStringExtra("Name");

        workoutCardName = findViewById(R.id.cardWorkoutName);
        removeWorkout = findViewById(R.id.workoutCardRemove);
        cardWorkoutExerciseList = findViewById(R.id.workoutCardExercises);
        cardWorkoutClientList = findViewById(R.id.workoutCardClients);
        cardWorkoutClassList = findViewById(R.id.workoutCardClasses);
        viewFlipper = findViewById(R.id.workout_view_flip);
        viewExercise = findViewById(R.id.viewExercises);
        viewClass = findViewById(R.id.viewWorkoutClassses);
        viewClients = findViewById(R.id.viewWorkoutClients);

        workoutCardName.setText(name);

        cardWorkoutExerciseList.setHasFixedSize(true);
        cardWorkoutExerciseList.setLayoutManager(new LinearLayoutManager(this));

        cardWorkoutClientList.setHasFixedSize(true);
        cardWorkoutClientList.setLayoutManager(new LinearLayoutManager(this));

        cardWorkoutClassList.setHasFixedSize(true);
        cardWorkoutClassList.setLayoutManager(new LinearLayoutManager(this));

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null){

            db.collection("ptrainer").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error != null){
                        Log.e("firestore error", error.getMessage());
                        return;
                    }

                    for(DocumentChange dc : value.getDocumentChanges()){
                        ptrainer = dc.getDocument().toObject(Ptrainer.class);

                        if(dc.getType() == DocumentChange.Type.ADDED){


                            for(Workout w : ptrainer.workouts){
                                if(w.name.equals(workoutCardName.getText())) {
                                    for (Exercise e : w.exercises) {
                                        exerciseArrayList.add(e);
                                        cardWorkoutExercisesAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            for(Student s : ptrainer.students){
                                for(Workout w: s.workouts){
                                    if(w.name.equals(workoutCardName.getText())){
                                        clientArrayList.add(s);
                                        cardWorkoutClientAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            for(Class c: ptrainer.classes){
                                    if(c.workout.name.equals(workoutCardName.getText())){
                                        classArrayList.add(c);
                                        cardWorkoutClassAdapter.notifyDataSetChanged();
                                    }
                                }
                        }



                    }

                    removeWorkout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for(int s = 0; s < ptrainer.students.size(); s++){
                                for(int w =0; w<ptrainer.students.get(s).workouts.size();w++){
                                    if(ptrainer.students.get(s).workouts.get(w).name.equals(workoutCardName.getText())){
                                        ptrainer.students.get(s).workouts.remove(w);
                                        db.collection("student").document(ptrainer.students.get(s).username)
                                                .set(ptrainer.students.get(s), SetOptions.merge());

                                    }
                                }
                            }

                            for(int c = 0; c < ptrainer.classes.size();c++){
                                if(ptrainer.classes.get(c).workout == null){
                                    continue;
                                }
                                if(ptrainer.classes.get(c).workout.name.equals(workoutCardName.getText())){
                                    ptrainer.classes.get(c).workout = null;
                                    db.collection("Class").document(ptrainer.classes.get(c).name)
                                            .set(ptrainer.classes.get(c), SetOptions.merge());
                                }
                            }

                            for(int i = 0; i < ptrainer.workouts.size();i++){

                                if(ptrainer.workouts.get(i).name.equals(workoutCardName.getText())){
                                    ptrainer.workouts.remove(i);
                                }
                            }
                            db.collection("ptrainer").document(currentUser.getUid())
                                    .set(ptrainer, SetOptions.merge());
                            db.collection("Workouts").document((String) workoutCardName.getText())
                                    .delete();

                            Intent intent = new Intent(WorkoutCard.this, ManageWorkouts.class);
                            startActivity(intent);
                        }
                    });
                }
            });
            exerciseArrayList = new ArrayList<>();
            cardWorkoutExercisesAdapter = new CardWorkoutExercisesAdapter(WorkoutCard.this,exerciseArrayList,ptrainer);
            cardWorkoutExerciseList.setAdapter(cardWorkoutExercisesAdapter);

            clientArrayList = new ArrayList<>();
            cardWorkoutClientAdapter = new CardWorkoutClientAdapter(WorkoutCard.this,clientArrayList,ptrainer);
            cardWorkoutClientList.setAdapter(cardWorkoutClientAdapter);

            classArrayList = new ArrayList<>();
            cardWorkoutClassAdapter = new CardWorkoutClassAdapter(WorkoutCard.this,classArrayList,ptrainer);
            cardWorkoutClassList.setAdapter(cardWorkoutClassAdapter);

            viewExercise.setTextColor(getResources().getColor(R.color.second_color));


        }
    }

    public void seeExercises(View view){
        viewFlipper.setDisplayedChild(0);
        changeButtonColor(view);
    }


    public void seeWorkoutsClasses(View view){
        viewFlipper.setDisplayedChild(1);
        changeButtonColor(view);
    }

    public void seeClients(View view){
        viewFlipper.setDisplayedChild(2);
        changeButtonColor(view);
    }

    public void changeButtonColor(View view){
        if(viewFlipper.getDisplayedChild()==0){
            viewExercise.setTextColor(getResources().getColor(R.color.second_color));
            viewClass.setTextColor(getResources().getColor(R.color.blue_100));
            viewClients.setTextColor(getResources().getColor(R.color.blue_100));
        }

        else if(viewFlipper.getDisplayedChild()==1){
            viewExercise.setTextColor(getResources().getColor(R.color.blue_100));
            viewClass.setTextColor(getResources().getColor(R.color.second_color));
            viewClients.setTextColor(getResources().getColor(R.color.blue_100));
        }
        else {
            viewExercise.setTextColor(getResources().getColor(R.color.blue_100));
            viewClass.setTextColor(getResources().getColor(R.color.blue_100));
            viewClients.setTextColor(getResources().getColor(R.color.second_color));
        }
    }
}