package com.edcg.simple.springboot.webapp.model;

public record Contact(
    Long id, 
    String nom, 
    String prenom, 
    String telephone, 
    String email
) {}
