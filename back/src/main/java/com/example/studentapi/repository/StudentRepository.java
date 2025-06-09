package com.example.studentapi.repository;

import com.example.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT AVG(s.moyenne) FROM Student s")
    Double findMoyenneClasse();

    @Query("SELECT MIN(s.moyenne) FROM Student s")
    Double findMoyenneMin();

    @Query("SELECT MAX(s.moyenne) FROM Student s")
    Double findMoyenneMax();
}
