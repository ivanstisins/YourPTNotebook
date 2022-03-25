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
    private final RecyclerViewInterface recyclerViewInterface;
    ArrayList<Student> ptStudentArrayList;
    Ptrainer ptrainer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Student student;

    public ManageClientAdapter(Context context, Ptrainer ptrainer, ArrayList<Student> ptStudentArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.ptrainer = ptrainer;
        this.ptStudentArrayList = ptStudentArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manage_client_ist,parent,false);
        return new MyViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageClientAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (currentUser != null) {
            student = ptStudentArrayList.get(position);
            holder.name.setText(student.fullName);
            holder.username.setText(student.username);

        }
    }


    public void removeClient(int position){
        student = ptStudentArrayList.get(position);
        DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        ptrainer = document.toObject(Ptrainer.class);
                                ptStudentArrayList.remove(student);
                                notifyDataSetChanged();
                                for(int i = 0; i < ptrainer.classes.size();i++){
                                 if(ptrainer.students.isEmpty()){
                                     ptrainer.classes.get(i).students.clear();
                                 }
                                }
                                for(int c = 0; c < ptrainer.classes.size();c++){
                                    for (int s = 0; s< ptrainer.classes.get(c).students.size();s++){
                                        if(ptrainer.classes.get(c).students.get(s).equals(student.fullName)){
                                            ptrainer.classes.get(c).students.remove(s);
                                            db.collection("Class").document(ptrainer.classes.get(c).name)
                                                    .set(ptrainer.classes.get(c), SetOptions.merge());

                                        }
                                    }
                                }
                                ptrainer.students = ptStudentArrayList;
                                db.collection("ptrainer").document(currentUser.getUid())
                                        .set(ptrainer, SetOptions.merge());
                                student.classes.clear();
                                student.workouts.clear();
                              db.collection("student").document(student.username)
                                  .set(student, SetOptions.merge());
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return ptStudentArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView username;

        public MyViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.PtsclientName);
            username = itemView.findViewById(R.id.PtsclientUsername);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}
