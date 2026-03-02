package com.rayaneraab.service1.repository;

import com.rayaneraab.service1.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    // Hérite automatiquement : findAll(), save(), findById(), delete()
}

