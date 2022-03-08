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
            Class aClass = ptClassArrayList.get(position);
            holder.name.setText(aClass.name);
            holder.date.setText(aClass.classDate);
            holder.type.setText(aClass.type);
            holder.workout.setText(aClass.workout.name);
            holder.clients.setText(aClass.students.toString()+"\n");
            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            ptrainer = document.toObject(Ptrainer.class);
                            holder.removeClassButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ptClassArrayList.remove(aClass);
                                    ptrainer.classes = ptClassArrayList;

                                    db.collection("ptrainer").document(currentUser.getUid())
                                            .set(ptrainer, SetOptions.merge());


                                    db.collection("Class").document(aClass.getName())
                                            .delete();
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
        return ptClassArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView date;
        TextView type;
        TextView workout;
        TextView clients;
        Button removeClassButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ptClassName);
            date = itemView.findViewById(R.id.ptClassDate);
            type = itemView.findViewById(R.id.ptClassType);
            workout = itemView.findViewById(R.id.ptClassWorkout);
            removeClassButton = itemView.findViewById(R.id.RemoveClassButton);
            clients = itemView.findViewById(R.id.ptClassClients);
        }
    }
}
