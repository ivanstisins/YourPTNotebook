package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {
    private TextView username, name;
    private ImageButton addClient, createClass, createWorkout;
    BottomNavigationView bottomNavigationView;
    CircleImageView profilePic;
    FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileref = storageReference.child("users/"+fAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        profilePic = findViewById(R.id.profilePicture);
        addClient =(ImageButton) findViewById(R.id.addClient);
        createClass = (ImageButton) findViewById(R.id.createClass);
        createWorkout = (ImageButton) findViewById(R.id.createWorkOut);

        bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.manage_client:
                        startActivity(new Intent(Dashboard.this, ManageClients.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.manage_classes:
                        startActivity(new Intent(Dashboard.this, ManageClasses.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.manage_workout:
                        startActivity(new Intent(Dashboard.this, ManageWorkouts.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Dashboard.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0,0);
                        return true;

                }


                return false;
            }
        });
        currentUser = fAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            //email.setText(document.getString("Username"));
                            Ptrainer ptrainer = document.toObject(Ptrainer.class);
                            username.setText(ptrainer.getUsername());
                            name.setText(ptrainer.getFullName());

                        }
                    }
                }
            });
            addClient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Dashboard.this, AddClient.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            });
            createWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Dashboard.this, create_workout.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            });


            createClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Dashboard.this, CreateClass.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            });

            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(openGallery,1000);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri profileUri = data.getData();
                //profilePic.setImageURI(profileUri);

                uploadProfilePicture(profileUri);
            }
        }

    }

    private void uploadProfilePicture(Uri imageUri) {
        StorageReference fileReferecne = storageReference.child("users/"+fAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        fileReferecne.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReferecne.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePic);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Dashboard.this,"Image Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

}