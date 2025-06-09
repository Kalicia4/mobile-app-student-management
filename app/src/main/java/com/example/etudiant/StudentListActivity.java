package com.example.etudiant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etudiant.adapter.StudentAdapter;
import com.example.etudiant.model.MoyenneStats;
import com.example.etudiant.model.Student;
import com.example.etudiant.retrofit.RetrofitService;
import com.example.etudiant.retrofit.StudentApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvMoyenneClasse, tvMoyenneMin, tvMoyenneMax;
    private AppCompatButton btnVisualiser; // ✅ Ajouter la référence au bouton

    private StudentApi studentApi;
    private MoyenneStats currentStats; // ✅ Stocker les stats actuelles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Initialiser Retrofit
        RetrofitService retrofitService = new RetrofitService();
        studentApi = retrofitService.getRetrofit().create(StudentApi.class);

        // Initialiser RecyclerView
        recyclerView = findViewById(R.id.employeeList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // FloatingActionButton pour ajouter un étudiant
        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> {
            startActivity(new Intent(StudentListActivity.this, AddEtudiantActivity.class));
        });

        // ✅ Initialiser le bouton Visualiser
        btnVisualiser = findViewById(R.id.btnVisualiser);
        btnVisualiser.setOnClickListener(view -> {
            // Lancer l'activité du graphique avec les données des statistiques
            launchChartActivity();
        });

        // Initialiser les TextViews des statistiques
        tvMoyenneClasse = findViewById(R.id.tv_moyenne_classe);
        tvMoyenneMin = findViewById(R.id.tv_moyenne_min);
        tvMoyenneMax = findViewById(R.id.tv_moyenne_max);

        loadStudents();
        loadStats();
    }

    private void loadStudents() {
        studentApi.getAllStudents().enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateListView(response.body());
                } else {
                    Toast.makeText(StudentListActivity.this, "Erreur lors du chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(StudentListActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Méthode pour lancer le graphique avec les données
    private void launchChartActivity() {
        if (currentStats != null) {
            Intent intent = new Intent(StudentListActivity.this, ChartActivity.class);
            intent.putExtra("moyenneClasse", (float) currentStats.getMoyenneClasse());
            intent.putExtra("min", (float) currentStats.getMin());
            intent.putExtra("max", (float) currentStats.getMax());
            startActivity(intent);
        } else {
            // Si les stats ne sont pas encore chargées, les charger d'abord
            Toast.makeText(this, "Chargement des statistiques...", Toast.LENGTH_SHORT).show();
            studentApi.getMoyenneStats().enqueue(new Callback<MoyenneStats>() {
                @Override
                public void onResponse(Call<MoyenneStats> call, Response<MoyenneStats> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        currentStats = response.body();
                        launchChartActivity(); // Relancer après avoir récupéré les données
                    } else {
                        Toast.makeText(StudentListActivity.this, "Erreur lors du chargement des stats", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MoyenneStats> call, Throwable t) {
                    Toast.makeText(StudentListActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void populateListView(List<Student> studentList) {
        StudentAdapter studentAdapter = new StudentAdapter(studentList, studentApi);
        studentAdapter.setOnStudentDeletedListener(() -> {
            loadStudents();
            loadStats();
        });
        recyclerView.setAdapter(studentAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
        loadStats();
    }

    private void loadStats() {
        studentApi.getMoyenneStats().enqueue(new Callback<MoyenneStats>() {
            @Override
            public void onResponse(Call<MoyenneStats> call, Response<MoyenneStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MoyenneStats stats = response.body();
                    currentStats = stats; // ✅ Sauvegarder les stats

                    // Formater à 2 décimales
                    String moyenneClasse = String.format("%.2f", stats.getMoyenneClasse());
                    String min = String.format("%.2f", stats.getMin());
                    String max = String.format("%.2f", stats.getMax());

                    tvMoyenneClasse.setText("Classe: " + moyenneClasse);
                    tvMoyenneMin.setText("Min: " + min);
                    tvMoyenneMax.setText("Max: " + max);

                } else {
                    Toast.makeText(StudentListActivity.this, "Erreur lors du chargement des stats", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoyenneStats> call, Throwable t) {
                Toast.makeText(StudentListActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}