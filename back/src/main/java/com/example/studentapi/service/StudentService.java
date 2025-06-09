package com.example.studentapi.service;

import com.example.studentapi.dto.MoyenneStats;
import com.example.studentapi.model.Student;
import com.example.studentapi.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Injection du repository via le constructeur
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Récupérer tous les étudiants
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Récupérer un étudiant par son id
    public ResponseEntity<Student> getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Ajouter un nouvel étudiant
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // Modifier un étudiant existant
    public ResponseEntity<Student> updateStudent(Long id, Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setNom(studentDetails.getNom());
            student.setPrenom(studentDetails.getPrenom());
            student.setMoyenne(studentDetails.getMoyenne());
            Student updatedStudent = studentRepository.save(student);
            return ResponseEntity.ok(updatedStudent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un étudiant
    public ResponseEntity<Void> deleteStudent(Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            studentRepository.delete(optionalStudent.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public MoyenneStats getStatistiquesMoyennes() {
        Double moyenneClasse = studentRepository.findMoyenneClasse();
        Double moyenneMin = studentRepository.findMoyenneMin();
        Double moyenneMax = studentRepository.findMoyenneMax();

        // Si aucun étudiant, retourne des zéros (évite les NullPointerException)
        return new MoyenneStats(
                roundToTwoDecimals(moyenneClasse),
                roundToTwoDecimals(moyenneMin),
                roundToTwoDecimals(moyenneMax)
        );
    }

    // Exemple pour arrondir à 2 chiffres après la virgule
    private double roundToTwoDecimals(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

}
