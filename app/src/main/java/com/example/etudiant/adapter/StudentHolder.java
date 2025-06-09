package com.example.etudiant.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etudiant.R;

public class StudentHolder extends RecyclerView.ViewHolder {

    TextView name, moyenne, observation;
    ImageView updateButton, deleteButton;

    public StudentHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.studentName);
        moyenne = itemView.findViewById(R.id.studentMoyenne);
        observation = itemView.findViewById(R.id.studentObservation);
        updateButton = itemView.findViewById(R.id.updateButton);
        deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}
