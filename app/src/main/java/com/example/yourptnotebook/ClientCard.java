package com.example.yourptnotebook;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ClientCard extends AppCompatActivity {
    TextView clientCardName, clientCardUsername, clientCardAge,clientCardWeight,clientCardHeight;
    Button removeClient;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    Ptrainer ptrainer;

    RecyclerView cardClientClassesList;
    CardClientClassAdapter cardClientClassAdapter;
    ArrayList<Class> classArrayList;

    RecyclerView cardClientWorkoutsList;
    CardClientWorkoutAdapter cardClientWorkoutAdapter;
    ArrayList<Workout> workoutArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_card);
        db = FirebaseFirestore.getInstance();

        String name = getIntent().getStringExtra("Name");
        String username = getIntent().getStringExtra("Username");
        String height = getIntent().getStringExtra("Height");
        String weight = getIntent().getStringExtra("Weight");
        String age = getIntent().getStringExtra("Age");

        clientCardName = findViewById(R.id.cardClientName);
        clientCardUsername = findViewById(R.id.cardClientUsername);
        clientCardHeight = findViewById(R.id.cardClientHeight);
        clientCardWeight = findViewById(R.id.cardClientWeight);
        clientCardAge = findViewById(R.id.cardClientAge);
        removeClient = findViewById(R.id.cardRemoveClient);
        cardClientClassesList = findViewById(R.id.cardClientClasses);
        cardClientWorkoutsList = findViewById(R.id.cardClientWorkouts);

        clientCardName.setText(name);
        clientCardUsername.setText(username);
        clientCardHeight.setText(height+"m");
        clientCardWeight.setText(weight+"kg");
        clientCardAge.setText(age);

        cardClientClassesList.setHasFixedSize(true);
        cardClientClassesList.setLayoutManager(new LinearLayoutManager(this));

        cardClientWorkoutsList.setHasFixedSize(true);
        cardClientWorkoutsList.setLayoutManager(new LinearLayoutManager(this));

        currentUser = fAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
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


                            for(Student s : ptrainer.getStudents()) {
                                if(s.username.equals(clientCardUsername.getText())) {
                                    for (Class c : s.getClasses()) {
                                        classArrayList.add(c);
                                    }
                                    for(Workout w: s.getWorkouts()) {
                                        workoutArrayList.add(w);
                                    }
                                }
                            }

                        }
                        cardClientClassAdapter.notifyDataSetChanged();
                        cardClientWorkoutAdapter.notifyDataSetChanged();


                    }

                    removeClient.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            for(int c = 0; c < ptrainer.classes.size();c++){
                                for (int s = 0; s< ptrainer.classes.get(c).students.size();s++){
                                    if(ptrainer.classes.get(c).students.get(s).equals(clientCardName.getText())){
                                        ptrainer.classes.get(c).students.remove(s);
                                        db.collection("Class").document(ptrainer.classes.get(c).name)
                                                .set(ptrainer.classes.get(c), SetOptions.merge());

                                    }
                                }
                            }
                            for(int i = 0; i < ptrainer.students.size();i++){

                                if(ptrainer.students.get(i).username.equals(clientCardUsername.getText())){
                                    ptrainer.students.get(i).workouts.clear();
                                    ptrainer.students.get(i).classes.clear();
                                    db.collection("student").document(ptrainer.students.get(i).username)
                                            .set(ptrainer.students.get(i), SetOptions.merge());
                                    ptrainer.students.remove(i);
                                }
                            }

                            db.collection("ptrainer").document(currentUser.getUid())
                                    .set(ptrainer, SetOptions.merge());
                            Intent intent = new Intent(ClientCard.this, ManageClients.class);
                            startActivity(intent);
                        }
                    });
                }
            });
            classArrayList = new ArrayList<>();
            cardClientClassAdapter = new CardClientClassAdapter(ClientCard.this, classArrayList, ptrainer);
            cardClientClassesList.setAdapter(cardClientClassAdapter);

            workoutArrayList = new ArrayList<>();
            cardClientWorkoutAdapter = new CardClientWorkoutAdapter(ClientCard.this, workoutArrayList,ptrainer);
            cardClientWorkoutsList.setAdapter(cardClientWorkoutAdapter);
        }


    }
}