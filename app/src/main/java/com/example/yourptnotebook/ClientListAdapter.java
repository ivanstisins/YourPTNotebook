package com.example.yourptnotebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Student> studentArrayList;

    public ClientListAdapter(Context context, ArrayList<Student> studentArrayList) {
        this.context = context;
        this.studentArrayList = studentArrayList;
    }

    @NonNull
    @Override
    public ClientListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.client_list_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientListAdapter.MyViewHolder holder, int position) {
        Student student = studentArrayList.get(position);
        holder.name.setText(student.fullName);
        holder.username.setText(student.username);
    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.clientName);
            username = itemView.findViewById(R.id.clientUsername);
        }
    }
}
