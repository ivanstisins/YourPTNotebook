package com.example.yourptnotebook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateExercise extends AppCompatDialogFragment {
    EditText editExerciseName, editSets, editReps;

    CreateExerciseInterface createExerciseInterface;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_addexercise, null);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        builder.setView(view).setTitle("Add Exercise").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String steExerciseName = editExerciseName.getText().toString();
                String stSets = editSets.getText().toString();
                String stReps = editReps.getText().toString();
                createExerciseInterface.applyText(steExerciseName,stSets,stReps);
            }
        });


        editExerciseName = view.findViewById(R.id.exercisename);
        editSets = view.findViewById(R.id.setSets);
        editReps = view.findViewById(R.id.setReps);


        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        createExerciseInterface = (CreateExerciseInterface) context;
    }

    public interface CreateExerciseInterface{
        void applyText(String strExerciseName, String exerciseSets, String exerciseReps);
    }
}
