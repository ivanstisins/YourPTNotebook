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

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Workout> ptWorkoutArrayList;
    Ptrainer ptrainer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public ManageWorkoutAdapter(Context context, ArrayList<Workout> ptWorkoutArrayList,RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.ptWorkoutArrayList = ptWorkoutArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ManageWorkoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.workout_list,parent,false);
        return new MyViewHolder(view,recyclerViewInterface);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ManageWorkoutAdapter.MyViewHolder holder, int position) {
        if(currentUser != null){
            Workout workout = ptWorkoutArrayList.get(position);
            holder.name.setText(workout.name);

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

                        for(int s = 0; s < ptrainer.students.size(); s++){
                            for(int w =0; w<ptrainer.students.get(s).workouts.size();w++){
                                if(ptrainer.students.get(s).workouts.get(w).name.equals(workout.name)){
                                    ptrainer.students.get(s).workouts.remove(w);
                                }
                            }
                        }
                        for(int c = 0; c < ptrainer.classes.size();c++){
                            if(ptrainer.classes.get(c).workout == null){
                                continue;
                            }
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
                                db.collection("student").document(ptrainer.students.get(i).username).set(ptrainer.students.get(i),SetOptions.merge());
                            }
                        }
                        ptWorkoutArrayList.remove(workout);
                        notifyDataSetChanged();
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

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.woName);

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
