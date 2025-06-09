package com.example.etudiant

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.etudiant.model.Student
import com.example.etudiant.retrofit.RetrofitService
import com.example.etudiant.retrofit.StudentApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class UpdateStudentActivity : AppCompatActivity() {

    private lateinit var editNom: EditText
    private lateinit var editPrenom: EditText
    private lateinit var editMoyenne: EditText
    private lateinit var updateButton: ImageView

    private var studentId by Delegates.notNull<Long>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_student)

        editNom = findViewById(R.id.updateEditTextNom)
        editPrenom = findViewById(R.id.updateEditTextPrenom)
        editMoyenne = findViewById(R.id.updateEditTextMoyenne)
        updateButton = findViewById(R.id.updateButton)

        // Récupère les données envoyées via Intent
        studentId = intent.getLongExtra("student_id", -1L)
        if (studentId == -1L) {
            Toast.makeText(this, "ID de l'étudiant introuvable", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val nom = intent.getStringExtra("nom")
        val prenom = intent.getStringExtra("prenom")
        val moyenne = intent.getDoubleExtra("moyenne", 0.0)

        editNom.setText(nom)
        editPrenom.setText(prenom)
        editMoyenne.setText(moyenne.toString())

        updateButton.setOnClickListener {
            val nom = editNom.text.toString().trim()
            val prenom = editPrenom.text.toString().trim()
            val moyenne = editMoyenne.text.toString().toDoubleOrNull()

            if (nom.isEmpty() || prenom.isEmpty() || moyenne == null) {
                Toast.makeText(this, "Veuillez remplir correctement tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedStudent = Student(
                nom,
                prenom,
                moyenne
            )

            val retrofitService = RetrofitService()
            val studentApi = retrofitService.retrofit.create(StudentApi::class.java)

            studentApi.updateStudent(studentId, updatedStudent).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@UpdateStudentActivity, "Étudiant modifié", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@UpdateStudentActivity, "Erreur : ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@UpdateStudentActivity, "Erreur réseau : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}
