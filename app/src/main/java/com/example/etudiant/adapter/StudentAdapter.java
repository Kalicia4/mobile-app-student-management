package com.example.etudiant.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etudiant.R;
import com.example.etudiant.UpdateStudentActivity;
import com.example.etudiant.model.Student;
import com.example.etudiant.retrofit.StudentApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAdapter extends RecyclerView.Adapter<StudentHolder> {

    private List<Student> studentList;
    private StudentApi studentApi;
    private OnStudentDeletedListener listener;

    public interface OnStudentDeletedListener {
        void onStudentDeleted();
    }

    public void setOnStudentDeletedListener(OnStudentDeletedListener listener) {
        this.listener = listener;
    }

    public StudentAdapter(List<Student> studentList, StudentApi studentApi) {
        this.studentList = studentList;
        this.studentApi = studentApi;
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new StudentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentHolder holder, int position) {
        Student student = studentList.get(position);
        Context context = holder.itemView.getContext();

        holder.name.setText(student.getNom() + " " + student.getPrenom());
        holder.moyenne.setText("Moyenne : " + String.format("%.2f", student.getMoyenne()));

        if (student.getMoyenne() >= 10) {
            holder.observation.setText("Admis");
            holder.observation.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        } else if (student.getMoyenne() >= 5) {
            holder.observation.setText("Redoublant");
            holder.observation.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
        } else {
            holder.observation.setText("Exclus");
            holder.observation.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }

        holder.updateButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateStudentActivity.class);
            intent.putExtra("student_id", student.getId());
            intent.putExtra("nom", student.getNom());
            intent.putExtra("prenom", student.getPrenom());
            intent.putExtra("moyenne", student.getMoyenne());
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Supprimer l'étudiant " + student.getNom() + " " + student.getPrenom() + " ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        studentApi.deleteStudent(student.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
                                    if (listener != null) listener.onStudentDeleted();
                                } else {
                                    Toast.makeText(context, "Échec de la suppression", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Erreur réseau", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}

