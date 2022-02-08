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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class AddClient extends AppCompatActivity {
    private RecyclerView clientList;
    Ptrainer ptrainer;
    ArrayList<Student> studentArrayList;
    ClientListAdapter clientListAdapter;
    private ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        clientList = findViewById(R.id.recyclerView);
        clientList.setHasFixedSize(true);
        clientList.setLayoutManager(new LinearLayoutManager(this));
        backbutton = (ImageButton) findViewById(R.id.backbutton);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        studentArrayList = new ArrayList<Student>();
        clientListAdapter = new ClientListAdapter(AddClient.this, studentArrayList,ptrainer);
        clientList.setAdapter(clientListAdapter);

        if (currentUser != null) {
            db.collection("student").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error != null){
                        Log.e("firestore error", error.getMessage());
                        return;
                    }

                    for(DocumentChange dc : value.getDocumentChanges()){
                        if(dc.getType() == DocumentChange.Type.ADDED){
                            studentArrayList.add(dc.getDocument().toObject(Student.class));
                        }

                        clientListAdapter.notifyDataSetChanged();


                    }
                }
            });

            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddClient.this, Dashboard.class);
                    startActivity(intent);
                }
            });

        }
    }
}