package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    private Button createWorkout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workouts);
        bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.manage_workout);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(ManageWorkouts.this, Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.manage_client:
                        startActivity(new Intent(ManageWorkouts.this, ManageClients.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.manage_classes:
                        startActivity(new Intent(ManageWorkouts.this, ManageClasses.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ManageWorkouts.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
        createWorkout = findViewById(R.id.create_workout_button);
        manageWorkoutList = findViewById(R.id.recyclerViewManageWorkouts);
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

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    manageWorkoutAdapter.removeClass(viewHolder.getAdapterPosition());

                }
            }).attachToRecyclerView(manageWorkoutList);


        }

        createWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("button is pressed");
                Intent intent = new Intent(ManageWorkouts.this, create_workout.class);
                startActivity(intent);
            }
        });
    }
}