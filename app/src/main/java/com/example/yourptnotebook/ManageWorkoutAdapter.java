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

public class ManageWorkoutAdapter extends RecyclerView.Adapter<ManageWorkoutAdapter.MyViewHolder> {

    Context context;
    ArrayList<Workout> ptWorkoutArrayList;
    Ptrainer ptrainer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public ManageWorkoutAdapter(Context context, ArrayList<Workout> ptWorkoutArrayList) {
        this.context = context;
        this.ptWorkoutArrayList = ptWorkoutArrayList;
    }

    @NonNull
    @Override
    public ManageWorkoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.workout_list,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ManageWorkoutAdapter.MyViewHolder holder, int position) {
        if(currentUser != null){
            Workout workout = ptWorkoutArrayList.get(position);
            holder.name.setText(workout.name);
            holder.exercises.setText(workout.exercises.toString());
            holder.clients.setText(workout.students.toString()+"\n");
            DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            ptrainer = document.toObject(Ptrainer.class);
                            holder.removeWorkoutButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ptWorkoutArrayList.remove(workout);
                                    ptrainer.workouts = ptWorkoutArrayList;
                                    db.collection("ptrainer").document(currentUser.getUid())
                                            .set(ptrainer, SetOptions.merge());
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
        return ptWorkoutArrayList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView exercises;
        TextView clients;
        Button removeWorkoutButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.woName);
            clients = itemView.findViewById(R.id.woClients);
            exercises = itemView.findViewById(R.id.woExercises);
            removeWorkoutButton = itemView.findViewById(R.id.RemoveWorkoutButton);
        }
    }

}
