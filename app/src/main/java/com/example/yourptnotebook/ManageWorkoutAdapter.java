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
        }

    }

    public void removeWorkout(int position){
        Workout workout = ptWorkoutArrayList.get(position);
        DocumentReference dr = db.collection("ptrainer").document(currentUser.getUid());
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        ptrainer = document.toObject(Ptrainer.class);
                        ptWorkoutArrayList.remove(workout);

                        for(int s = 0; s < ptrainer.students.size(); s++){
                            for(int w =0; w<ptrainer.students.get(s).workouts.size();w++){
                                if(ptrainer.students.get(s).workouts.get(w).name.equals(workout.name)){
                                    ptrainer.students.get(s).workouts.remove(w);
                                }
                            }
                        }
                        for(int c = 0; c < ptrainer.classes.size();c++){
                            if(ptrainer.classes.get(c).workout.name.equals(workout.name)){
                                ptrainer.classes.get(c).workout = null;
                                db.collection("Class").document(ptrainer.classes.get(c).name)
                                        .set(ptrainer.classes.get(c), SetOptions.merge());
                            }
                        }


                        ptrainer.workouts = ptWorkoutArrayList;
                        for (int i = 0; i < ptrainer.students.size(); i++) {
                            if(ptrainer.workouts.isEmpty()){
                                ptrainer.students.get(i).workouts.clear();
                                db.collection("student").document(ptrainer.students.get(i).email).set(ptrainer.students.get(i),SetOptions.merge());
                            }
                        }
                        db.collection("ptrainer").document(currentUser.getUid())
                                        .set(ptrainer, SetOptions.merge());
                        db.collection("Workouts").document(workout.getName())
                                .delete();


                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return ptWorkoutArrayList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView exercises;
        TextView clients;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.woName);
            exercises = itemView.findViewById(R.id.woExercises);
        }
    }

}
