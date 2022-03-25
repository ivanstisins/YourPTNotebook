package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ManageClients extends AppCompatActivity implements RecyclerViewInterface {
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
            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                         ptrainer = document.toObject(Ptrainer.class);

                        for(Student s : ptrainer.getStudents()){
                                ptStudentArrayList.add(s);
                            }

                        manageClientAdapter.notifyDataSetChanged();

                    }
                }
            });
            ptStudentArrayList = new ArrayList<>();
            manageClientAdapter = new ManageClientAdapter(ManageClients.this,ptrainer,ptStudentArrayList,this);
            manageClientsList.setAdapter(manageClientAdapter);
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    manageClientAdapter.removeClient(viewHolder.getAdapterPosition());
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addBackgroundColor(ContextCompat.getColor(ManageClients.this, R.color.red))
                            .addActionIcon(R.drawable.ic_delete)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }).attachToRecyclerView(manageClientsList);

            addClient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ManageClients.this, AddClient.class);
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ManageClients.this, ClientCard.class);
        intent.putExtra("Name",ptStudentArrayList.get(position).fullName);
        intent.putExtra("Username",ptStudentArrayList.get(position).username);
        intent.putExtra("Height",ptStudentArrayList.get(position).height);
        intent.putExtra("Weight",ptStudentArrayList.get(position).weight);
        intent.putExtra("Age",ptStudentArrayList.get(position).age);
        startActivity(intent);
    }
}