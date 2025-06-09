package com.example.studentapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoyenneStats {
    private Double moyenneClasse;
    private Double moyenneMin;
    private Double moyenneMax;
}