package com.example.yourptnotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateClass extends AppCompatActivity {
    EditText className, classDate;
    Spinner setWorkout;
    Button createClass;
    RadioGroup setType;
    Class aClass;
    RadioButton radioButton;
    Ptrainer ptrainer;
    ArrayList<Workout>workouts;
    ArrayList<Student> students;
    private Button myClasses;
    ArrayList<Student> classtudents = new ArrayList<>();
    private Button addClientToClass;
    TextView classClientList;
    boolean[] selectedClients;
    ArrayList<Integer> clientList = new ArrayList<>();
    String[] clientArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        className = findViewById(R.id.className);
        classDate = findViewById(R.id.classDate);
        setType = findViewById(R.id.class_type);
        createClass = findViewById(R.id.create_class);
        setWorkout = findViewById(R.id.select_workout);
        myClasses = findViewById(R.id.myClasses);
        addClientToClass = findViewById(R.id.add_client_to_class);
        classClientList = findViewById(R.id.list_clients_for_class);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ptrainer = document.toObject(Ptrainer.class);
                            workouts = ptrainer.workouts;
                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,workouts);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            setWorkout.setAdapter(adapter);
                            students = ptrainer.students;
                            clientArray = new String[students.size()];
                            for(int i = 0; i< students.size();i++){
                                clientArray[i] = students.get(i).fullName;
                            }
                            selectedClients = new boolean[clientArray.length];

                            addClientToClass.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateClass.this);
                                    builder.setTitle("Add Clients to Class");
                                    builder.setMultiChoiceItems(clientArray, selectedClients, new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                                            if(isChecked){
                                                clientList.add(which);
                                            }else{
                                                clientList.remove((Integer.valueOf(which)));
                                            }
                                        }
                                    });

                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            String clients = "";
                                            for(int i = 0; i < clientList.size(); i++){
                                                clients = clients + clientArray[clientList.get(i)];
                                                if(i !=clientList.size()-1){
                                                    clients = clients + ", ";
                                                }
                                            }
                                            classClientList.setText(clients);
                                        }
                                    });

                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            for(int  j = 0; j < selectedClients.length; j++){
                                                selectedClients[j] = false;
                                                clientList.clear();
                                                classClientList.setText("");
                                            }
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }
                            });

                            createClass.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String clients = classClientList.getText().toString();
                                    List<String> addedClients = new ArrayList<String>(Arrays.asList(clients.split(", ")));
                                    for (int i = 0; i < students.size(); i++) {
                                        if (addedClients.contains(students.get(i).fullName)) {
                                             classtudents.add(students.get(i));
                                        }
                                    }
                                    int radioId = setType.getCheckedRadioButtonId();
                                    radioButton = findViewById(radioId);
                                    Workout state = (Workout) setWorkout.getSelectedItem();
                                    aClass = new Class(className.getText().toString(), radioButton.getText().toString(),classDate.getText().toString(),ptrainer.getUsername(),state);
                                    aClass.setStudents(classtudents);
                                    ptrainer.classes.add(aClass);
                                    db.collection("Class").document(aClass.getName()).set(aClass);
                                    db.collection("ptrainer").document(currentUser.getUid())
                                            .set(ptrainer, SetOptions.merge());
                                }
                            });
                        }
                    }
                }
            });

            myClasses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateClass.this, ManageClasses.class);
                    startActivity(intent);
                }
            });
        }
    }
}