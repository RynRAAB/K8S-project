package com.edcg.simple.springboot.webapp.controller;

import com.edcg.simple.springboot.webapp.model.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@Scope("request")
public class FrontendController {
    
    @Value("${backend.url:http://localhost:8080/api/contacts}")
    private String backendUrl;
    
    private final RestTemplate restTemplate = new RestTemplate(); // Direct sans injection
    
    // Liste tous les contacts - Page principale
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        try {
            Contact[] contacts = restTemplate.getForObject(backendUrl, Contact[].class);
            model.addAttribute("contacts", contacts != null ? Arrays.asList(contacts) : List.of());
            model.addAttribute("message", "✅ Backend connecté");
        } catch (Exception e) {
            model.addAttribute("contacts", List.of());
            model.addAttribute("error", "Backend indisponible: " + e.getMessage());
        }
        return "index";
    }
    
    // Ajouter contact
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addContact(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String telephone,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {
        
        try {
            Contact newContact = new Contact(null, nom, prenom, telephone, email);
            restTemplate.postForObject(backendUrl, newContact, Void.class);
            redirectAttributes.addFlashAttribute("message", "Contact ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur ajout: " + e.getMessage());
            return "redirect:/";
        }
        return "redirect:http://frontend.local:8080/";
    }
    
    // Supprimer contact
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteContact(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            restTemplate.delete(backendUrl + "/" + id);
            redirectAttributes.addFlashAttribute("message", "Contact supprimé !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur suppression: " + e.getMessage());
        }
        return "redirect:http://frontend.local:8080/";
    }
    
    // Page modification - Formulaire pré-rempli
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editForm(@PathVariable Long id, Model model) {
        try {
            Contact contact = restTemplate.getForObject(backendUrl + "/" + id, Contact.class);
            model.addAttribute("contact", contact);
            model.addAttribute("action", "update");
        } catch (Exception e) {
            model.addAttribute("error", "Contact non trouvé: " + e.getMessage());
            return "redirect:http://frontend.local:8080/";
        }
        return "edit";
    }
    
    // Modifier contact
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String updateContact(
            @PathVariable Long id,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String telephone,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {
        
        try {
            Contact updatedContact = new Contact(id, nom, prenom, telephone, email);
            restTemplate.put(backendUrl + "/" + id, updatedContact);
            redirectAttributes.addFlashAttribute("message", "Contact modifié avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur modification: " + e.getMessage());
        }
        return "redirect:http://frontend.local:8080/";
    }
}
