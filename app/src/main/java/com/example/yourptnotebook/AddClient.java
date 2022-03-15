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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashSet;

public class AddClient extends AppCompatActivity {
    private RecyclerView clientList;
    Ptrainer ptrainer;
    ArrayList<Student> studentArrayList;
    ClientListAdapter clientListAdapter;
    private Button myClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        clientList = findViewById(R.id.recyclerView);

        clientList.setHasFixedSize(true);
        clientList.setLayoutManager(new LinearLayoutManager(this));
        myClients = findViewById(R.id.myClients);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        studentArrayList = new ArrayList<Student>();
        ptrainer = new Ptrainer();
        clientListAdapter = new ClientListAdapter(AddClient.this, studentArrayList,ptrainer);
        clientList.setAdapter(clientListAdapter);

        if (currentUser != null) {

            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            //email.setText(document.getString("Username"));
                            ptrainer = document.toObject(Ptrainer.class);// wite database info to object

                        }
                    }
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

//                                    for (int i= 0; i< studentArrayList.size();i++) {
//                                        for (int j = 0; j < ptrainer.students.size(); j++) {
//                                            if(studentArrayList.isEmpty()){
//                                                continue;
//                                            }
//                                            if (studentArrayList.get(i).email.equals(ptrainer.students.get(j).email)) {
//                                                if(ptrainer.students.isEmpty()){
//                                                    continue;
//                                                }
//                                                System.out.println(studentArrayList.toString());;
//                                                System.out.println(studentArrayList.size());
//                                                studentArrayList.remove(i);
//                                            } else {
//                                                System.out.println("no");
//                                            }
//                                        }
//                                    }



                                }


                                clientListAdapter.notifyDataSetChanged();


                            }

                        }
                    });
                }
            });
//            db.collection("student").addSnapshotListener(new EventListener<QuerySnapshot>() {
//                @Override
//                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                    if(error != null){
//                        Log.e("firestore error", error.getMessage());
//                        return;
//                    }
//
//                    for(DocumentChange dc : value.getDocumentChanges()){
//                        if(dc.getType() == DocumentChange.Type.ADDED){
//                            studentArrayList.add(dc.getDocument().toObject(Student.class));
//                        }
//
//                        clientListAdapter.notifyDataSetChanged();
//
//
//                    }
//                }
//            });

            myClients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddClient.this, ManageClients.class);
                    startActivity(intent);
                }
            });

        }
    }
}