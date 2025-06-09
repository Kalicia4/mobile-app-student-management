package com.example.etudiant.model;

public class Student {

    private Long id;
    private String nom;
    private String prenom;
    private Double moyenne;

    // Constructeur vide (obligatoire pour Retrofit/Gson)
    public Student() {
    }


    // Constructeur sans id pour la création
    public Student(String nom, String prenom, Double moyenne) {
        this.nom = nom;
        this.prenom = prenom;
        this.moyenne = moyenne;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Double moyenne) {
        this.moyenne = moyenne;
    }
}
