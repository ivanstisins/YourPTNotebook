package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageClients extends AppCompatActivity {
    private RecyclerView manageClientsList;
    Ptrainer ptrainer;
    ArrayList<Student> ptStudentArrayList;
    ManageClientAdapter manageClientAdapter;
    private Button addClient;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_clients);
        manageClientsList = findViewById(R.id.recyclerViewManageClients);
        bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.manage_client);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(ManageClients.this, Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.manage_classes:
                        startActivity(new Intent(ManageClients.this, ManageClasses.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.manage_workout:
                        startActivity(new Intent(ManageClients.this, ManageWorkouts.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ManageClients.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
        manageClientsList.setHasFixedSize(true);
        manageClientsList.setLayoutManager(new LinearLayoutManager(this));
        addClient =findViewById(R.id.add_client_button);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
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
                            for(Student s : ptrainer.getStudents()){
                                ptStudentArrayList.add(s);
                            }
                        }
                        manageClientAdapter.notifyDataSetChanged();


                    }
                }
            });
            ptStudentArrayList = new ArrayList<>();
            manageClientAdapter = new ManageClientAdapter(ManageClients.this,ptrainer,ptStudentArrayList);
            manageClientsList.setAdapter(manageClientAdapter);

            addClient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ManageClients.this, AddClient.class);
                    startActivity(intent);
                }
            });

        }
    }
}