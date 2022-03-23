
package com.example.yourptnotebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class ClassCard extends AppCompatActivity {

    TextView classCardName,classCardDate, classCardType, classCardWorkout;
    Button removeClass;
    Button viewWorkout;
    Button viewClients;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    Ptrainer ptrainer;

    RecyclerView cardClassWorkoutExerciseList;
    CardClassWorkoutExercisesAdapter cardClassWorkoutExercisesAdapter;
    ArrayList<Exercise> exerciseArrayList;

    RecyclerView cardClassClientList;
    CardClassClientsAdapter cardClassClientsAdapter;
    ArrayList<String> clientArrayList;

    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_card);
        db = FirebaseFirestore.getInstance();

        String name = getIntent().getStringExtra("Name");
        String date = getIntent().getStringExtra("Date");
        String type = getIntent().getStringExtra("Type");
        String workoutName = getIntent().getStringExtra("Workout");

        classCardName = findViewById(R.id.cardClassName);
        classCardDate = findViewById(R.id.cardClassDate);
        classCardType = findViewById(R.id.cardClassType);
        classCardWorkout = findViewById(R.id.cardClassWorkoutName);
        removeClass = findViewById(R.id.classCardRemove);
        cardClassWorkoutExerciseList = findViewById(R.id.cardClassWorkoutExercises);
        cardClassClientList=findViewById(R.id.cardClassClients);
        viewFlipper = findViewById(R.id.class_card_view_flipper);
        viewWorkout = findViewById(R.id.cardClassViewWorkout);
        viewClients= findViewById(R.id.cardClassViewClients);

        classCardName.setText(name);
        classCardDate.setText(date);
        classCardType.setText(type);
        classCardWorkout.setText(workoutName);

        cardClassWorkoutExerciseList.setHasFixedSize(true);
        cardClassWorkoutExerciseList.setLayoutManager(new LinearLayoutManager(this));

        cardClassClientList.setHasFixedSize(true);
        cardClassClientList.setLayoutManager(new LinearLayoutManager(this));

        currentUser = fAuth.getInstance().getCurrentUser();

        db.collection("ptrainer").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for(DocumentChange dc : value.getDocumentChanges()) {
                    ptrainer = dc.getDocument().toObject(Ptrainer.class);

                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        for (Class c : ptrainer.classes) {
                            if (c.name.equals(classCardName.getText())) {
                                for (Exercise e : c.workout.exercises) {
                                        exerciseArrayList.add(e);
                                }
                            }
                        }
                        for(Class c : ptrainer.classes){
                            if(c.name.equals(classCardName.getText())){
                                for(String s: c.students){
                                    clientArrayList.add(s);
                                }
                            }
                        }


                    }
                    cardClassWorkoutExercisesAdapter.notifyDataSetChanged();
                    cardClassClientsAdapter.notifyDataSetChanged();
                }

                removeClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        for(int s = 0; s < ptrainer.students.size(); s++){
                            for(int c =0; c<ptrainer.students.get(s).classes.size();c++){
                                if(ptrainer.students.get(s).classes.get(c).name.equals(classCardName.getText())){
                                    ptrainer.students.get(s).classes.remove(c);
                                    db.collection("student").document(ptrainer.students.get(s).username)
                                            .set(ptrainer.students.get(s), SetOptions.merge());

                                }
                            }
                        }

                        for(int i = 0; i < ptrainer.classes.size();i++){

                            if(ptrainer.classes.get(i).name.equals(classCardName.getText())){
                                ptrainer.classes.remove(i);
                            }
                        }

                        db.collection("ptrainer").document(currentUser.getUid())
                                .set(ptrainer, SetOptions.merge());
                        db.collection("Class").document((String) classCardName.getText())
                                .delete();

                        Intent intent = new Intent(ClassCard.this, ManageClasses.class);
                        startActivity(intent);
                    }
                });
            }
        });
        exerciseArrayList = new ArrayList<>();
        cardClassWorkoutExercisesAdapter = new CardClassWorkoutExercisesAdapter(ClassCard.this,exerciseArrayList,ptrainer);
        cardClassWorkoutExerciseList.setAdapter(cardClassWorkoutExercisesAdapter);

        clientArrayList = new ArrayList<>();
        cardClassClientsAdapter = new CardClassClientsAdapter(ClassCard.this,clientArrayList,ptrainer);
        cardClassClientList.setAdapter(cardClassClientsAdapter);


        viewWorkout.setTextColor(getResources().getColor(R.color.second_color));

    }

    public void seeClassWorkout(View view){
        viewFlipper.setDisplayedChild(0);
        changeButtonColor(view);
    }
    public void seeClassClients(View view){
        viewFlipper.setDisplayedChild(1);
        changeButtonColor(view);
    }

    public void changeButtonColor(View view){
        if(viewFlipper.getDisplayedChild()==0){
            viewWorkout.setTextColor(getResources().getColor(R.color.second_color));
            viewClients.setTextColor(getResources().getColor(R.color.blue_100));
        }
        else {
            viewWorkout.setTextColor(getResources().getColor(R.color.blue_100));
            viewClients.setTextColor(getResources().getColor(R.color.second_color));
        }
    }
}