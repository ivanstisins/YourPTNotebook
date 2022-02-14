package com.example.yourptnotebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageWorkouts extends AppCompatActivity {
    private RecyclerView manageWorkoutList;
    Ptrainer ptrainer;
    ArrayList<Workout> ptWorkoutArrayList;
    ManageWorkoutAdapter manageWorkoutAdapter;
    private ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workouts);
        backbutton = findViewById(R.id.backbutton);
        manageWorkoutList = findViewById(R.id.manageWorkoutRecyclerView);
        manageWorkoutList.setHasFixedSize(true);
        manageWorkoutList.setLayoutManager(new LinearLayoutManager(this));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            db.collection("ptrainer").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.e("firestore error", error.getMessage());
                        return;
                    }

                    for (DocumentChange dc : value.getDocumentChanges()) {
                        ptrainer = dc.getDocument().toObject(Ptrainer.class);

                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            for (Workout w : ptrainer.getWorkouts()) {
                                ptWorkoutArrayList.add(w);
                            }
                        }
                        manageWorkoutAdapter.notifyDataSetChanged();


                    }
                }
            });
            ptWorkoutArrayList = new ArrayList<>();
            manageWorkoutAdapter = new ManageWorkoutAdapter(ManageWorkouts.this,ptWorkoutArrayList);
            manageWorkoutList.setAdapter(manageWorkoutAdapter);


        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("button is pressed");
                Intent intent = new Intent(ManageWorkouts.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }
}