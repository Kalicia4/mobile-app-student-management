package com.example.etudiant.retrofit;

import com.example.etudiant.model.MoyenneStats;
import com.example.etudiant.model.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StudentApi {

    @GET("/api/students")
    Call<List<Student>> getAllStudents();

    @POST("/api/students")
    Call<Student> saveStudent(@Body Student student);

    @PUT("/api/students/{id}")
    Call<Void> updateStudent(@Path("id") Long id, @Body Student student);

    @DELETE("/api/students/{id}")
    Call<Void> deleteStudent(@Path("id") Long id);

    @GET("/api/students/moyennes")  // ou l’URL que tu as définie côté backend pour stats
    Call<MoyenneStats> getMoyenneStats();

}
