package com.example.literatura.domain.model;

import jakarta.persistence.*;

@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer birthYear;
    private Integer deathYear;

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }

    public Integer getDeathYear() { return deathYear; }
    public void setDeathYear(Integer deathYear) { this.deathYear = deathYear; }

    @Override
    public String toString() {
        return "Autor: " + nombre +
                " | Nacimiento: " + (birthYear != null ? birthYear : "¿?") +
                " | Fallecimiento: " + (deathYear != null ? deathYear : "¿?");
    }
}
