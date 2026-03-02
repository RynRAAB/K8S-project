package com.rayaneraab.service1.controller;

import com.rayaneraab.service1.model.Contact;
import com.rayaneraab.service1.repository.ContactRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    
    private final ContactRepository repository;
    
    public ContactController(ContactRepository repository) {
        this.repository = repository;
    }
    
    // GET /api/contacts → Liste TOUS
    @GetMapping
    public List<Contact> findAll() {
        return repository.findAll();
    }
    
    // GET /api/contacts/{id} → Un seul
    @GetMapping("/{id}")
    public ResponseEntity<Contact> findById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Contact create(@RequestBody Contact contact) {
        contact.setId(null);  // ID auto-généré
        return repository.save(contact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> update(@PathVariable Long id, @RequestBody Contact contactDetails) {
        return repository.findById(id).map(contact -> {
            contact.setNom(contactDetails.getNom());
            contact.setPrenom(contactDetails.getPrenom());
            contact.setTelephone(contactDetails.getTelephone());
            contact.setEmail(contactDetails.getEmail());
            return ResponseEntity.ok(repository.save(contact));
        }).orElse(ResponseEntity.notFound().build());
    }
        
    // DELETE /api/contacts/{id} → Supprimer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
