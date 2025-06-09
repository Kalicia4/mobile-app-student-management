package com.example.etudiant

import android.os.Bundle
import android.util.Log
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

class AddEtudiantActivity : AppCompatActivity() {

    private lateinit var editTextNom: EditText
    private lateinit var editTextPrenom: EditText
    private lateinit var editTextMoyenne: EditText
    private lateinit var saveButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_etudiant)

        editTextNom = findViewById(R.id.editTextNom)
        editTextPrenom = findViewById(R.id.editTextPrenom)
        editTextMoyenne = findViewById(R.id.editTexMoyenne)
        saveButton = findViewById(R.id.saveButton)

        val retrofitService = RetrofitService().getRetrofit()
        val studentApi = retrofitService.create(StudentApi::class.java)

        saveButton.setOnClickListener {
            val nom = editTextNom.text.toString().trim()
            val prenom = editTextPrenom.text.toString().trim()
            val moyenneText = editTextMoyenne.text.toString().trim()

            if (nom.isEmpty() || prenom.isEmpty() || moyenneText.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val moyenne = moyenneText.toDoubleOrNull()
            if (moyenne == null) {
                Toast.makeText(this, "La moyenne doit être un nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val student = Student(nom, prenom, moyenne)

            studentApi.saveStudent(student).enqueue(object : Callback<Student> {
                override fun onResponse(call: Call<Student>, response: Response<Student>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddEtudiantActivity, "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show()
                        finish() // retourne à l'activité précédente
                    } else {
                        Log.e("API_ERROR", "Erreur : ${response.code()}")
                        Toast.makeText(this@AddEtudiantActivity, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Student>, t: Throwable) {
                    Log.e("API_FAILURE", "Échec : ${t.message}")
                    Toast.makeText(this@AddEtudiantActivity, "Échec de la requête", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
