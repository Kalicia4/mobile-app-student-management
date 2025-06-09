package com.example.studentapi.model;

import jakarta.persistence.*; // Utilise jakarta.persistence si Spring Boot 3.x (sinon javax.persistence pour 2.x)
import lombok.Data;

@Entity
@Table(name = "students")
@Data // Lombok pour générer getters, setters, toString, equals, hashCode
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrément dans PostgreSQL
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private Double moyenne;
}
