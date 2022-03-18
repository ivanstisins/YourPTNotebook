package com.example.yourptnotebook;

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

public class ManageClassAdapter extends RecyclerView.Adapter<ManageClassAdapter.MyViewHolder>{

    Context context;
    ArrayList<Class> ptClassArrayList;
    Ptrainer ptrainer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public ManageClassAdapter(Context context, ArrayList<Class> ptClassArrayList, Ptrainer ptrainer) {
        this.context = context;
        this.ptClassArrayList = ptClassArrayList;
        this.ptrainer = ptrainer;
    }

    @NonNull
    @Override
    public ManageClassAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manage_class_list,parent,false);
        return new ManageClassAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageClassAdapter.MyViewHolder holder, int position) {
        if (currentUser != null) {
            String clientString = "";
            Class aClass = ptClassArrayList.get(position);
            holder.name.setText(aClass.name);
            holder.date.setText(aClass.classDate);
            holder.type.setText(aClass.type);
            clientString += aClass.students;
            if(aClass.workout == null){
                holder.workout.setText("No Workouts Assigned");
            }
            else {
                holder.workout.setText(aClass.workout.name);
            }
            if(aClass.students.get(0).equals("")){
                holder.clients.setText("No Clients Assigned");
            }
            else {
                holder.clients.setText(clientString);
            }
        }
    }

    public void removeClass(int position){
        Class aClass = ptClassArrayList.get(position);
        DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        ptrainer = document.toObject(Ptrainer.class);


                        ptClassArrayList.remove(aClass);
                        for(int s = 0; s < ptrainer.students.size(); s++){
                            for(int c =0; c<ptrainer.students.get(s).classes.size();c++){
                                if(ptrainer.students.get(s).classes.get(c).name.equals(aClass.name)){
                                    ptrainer.students.get(s).classes.remove(c);
                                }
                            }
                        }
                        ptrainer.classes = ptClassArrayList;
                        for (int i = 0; i < ptrainer.students.size(); i++) {
                            if(ptrainer.classes.isEmpty()){
                                ptrainer.students.get(i).classes.clear();
                            }
                        }

                        db.collection("ptrainer").document(currentUser.getUid())
                                        .set(ptrainer, SetOptions.merge());
//
                        db.collection("Class").document(aClass.getName())
                                        .delete();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ptClassArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView date;
        TextView type;
        TextView workout;
        TextView clients;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ptClassName);
            date = itemView.findViewById(R.id.ptClassDate);
            type = itemView.findViewById(R.id.ptClassType);
            workout = itemView.findViewById(R.id.ptClassWorkout);
            clients = itemView.findViewById(R.id.ptClassClients);
        }
    }
}
