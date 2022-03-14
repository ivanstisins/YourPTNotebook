package com.example.yourptnotebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Student> studentArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Ptrainer ptrainer;

    public ClientListAdapter(Context context, ArrayList<Student> studentArrayList, Ptrainer ptrainer) {
        this.context = context;
        this.studentArrayList = studentArrayList;
        this.ptrainer = ptrainer;
    }

    @NonNull
    @Override
    public ClientListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.client_list_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (currentUser != null) {
            Student student = studentArrayList.get(position);
            holder.name.setText(student.fullName);
            holder.username.setText(student.username);
            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            ptrainer = document.toObject(Ptrainer.class);
//                            for (int i= 0; i< studentArrayList.size();i++) {
//                                for (int j = 0; j < ptrainer.students.size(); j++) {
//                                    if (studentArrayList.get(i).username.equals(ptrainer.students.get(j).username)) {
//                                        System.out.println("yes");
//                                        studentArrayList.remove(studentArrayList.get(i));
//                                    } else {
//                                        System.out.println("no");
//                                    }
//                                }
//                            }
                            //.removeAll(new HashSet(ptrainer.students));
                            System.out.println(ptrainer.students);
                            System.out.println(studentArrayList);

                            holder.addClientButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Log.d("demo", "user added "+student.getUsername());
                                    ptrainer.students.add(student);
                                    db.collection("ptrainer").document(currentUser.getUid())
                                            .set(ptrainer,SetOptions.merge());
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
        return studentArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView username;
        Button addClientButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.clientName);
            username = itemView.findViewById(R.id.clientUsername);
            addClientButton = itemView.findViewById(R.id.addclientButton);
        }
    }
}
