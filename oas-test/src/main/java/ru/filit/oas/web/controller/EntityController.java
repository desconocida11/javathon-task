package ru.filit.oas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.oas.dm.model.Client;
import ru.filit.oas.dm.model.Contact;
import ru.filit.oas.service.EntityService;

import java.util.List;

@RestController
public class EntityController {

    @Autowired
    private EntityService entityService;

    @PostMapping("/client/write")
    public ResponseEntity<String> writeClients() {
        entityService.writeClients();
        return ResponseEntity.ok("written");
    }

    @PostMapping("/client/read")
    public ResponseEntity<String> readClients() {
        List<Client> clients = entityService.readClients();
        return ResponseEntity.ok(clients.get(5).getLastname());
    }

    @PostMapping("/contact/write")
    public ResponseEntity<String> writeContact() {
        entityService.writeContacts();
        return ResponseEntity.ok("written");
    }

    @PostMapping("/contact/read")
    public ResponseEntity<String> readContact() {
        List<Contact> clients = entityService.readContacts();
        return ResponseEntity.ok(clients.get(5).getValue());
    }

}
