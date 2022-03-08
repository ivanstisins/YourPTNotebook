package com.example.yourptnotebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class ManageClientAdapter extends RecyclerView.Adapter<ManageClientAdapter.MyViewHolder>{

    Context context;
    ArrayList<Student> ptStudentArrayList;
    Ptrainer ptrainer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Student student;

    public ManageClientAdapter(Context context, Ptrainer ptrainer, ArrayList<Student> ptStudentArrayList) {
        this.context = context;
        this.ptrainer = ptrainer;
        this.ptStudentArrayList = ptStudentArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manage_client_ist,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageClientAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (currentUser != null) {
          String classes = "";
            student = ptStudentArrayList.get(position);
            holder.name.setText(student.fullName);
            holder.username.setText(student.username);
            classes += student.classes;
            holder.clientClasses.setText(classes);
//            db.collection("student").document(student.getEmail())
//                                            .set(student, SetOptions.merge());
            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            ptrainer = document.toObject(Ptrainer.class);
                            holder.removeClientButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ptStudentArrayList.remove(student);
                                    ptrainer.students = ptStudentArrayList;
                                    db.collection("ptrainer").document(currentUser.getUid())
                                            .set(ptrainer, SetOptions.merge());
//                                    db.collection("student").document(currentUser.getUid())
//                                            .set(ptrainer, SetOptions.merge());
                                }
                            });
                        }
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return ptStudentArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView username;
        Button removeClientButton;
        TextView clientClasses;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.PtsclientName);
            username = itemView.findViewById(R.id.PtsclientUsername);
            removeClientButton = itemView.findViewById(R.id.RemoveclientButton);
            clientClasses = itemView.findViewById(R.id.studClasses);
        }
    }
}
