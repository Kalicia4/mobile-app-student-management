package com.example.studentapi.controller;

import com.example.studentapi.dto.MoyenneStats;
import com.example.studentapi.model.Student;
import com.example.studentapi.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students") // L'URL de base de l'API pour les étudiants
public class StudentController {

    private final StudentService studentService;

    // Injection du service via le constructeur
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Récupérer tous les étudiants
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Récupérer un étudiant par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    // Ajouter un nouvel étudiant
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    // Mettre à jour un étudiant existant
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    // Supprimer un étudiant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/moyennes")
    public ResponseEntity<MoyenneStats> getMoyenneStats() {
        MoyenneStats stats = studentService.getStatistiquesMoyennes();
        return ResponseEntity.ok(stats);
    }
}
