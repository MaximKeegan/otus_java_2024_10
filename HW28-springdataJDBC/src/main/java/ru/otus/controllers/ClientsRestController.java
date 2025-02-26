package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.model.*;
import ru.otus.repository.AddressRepository;
import ru.otus.repository.ClientRepository;

@RestController
@RequestMapping("/api/clients")
public class ClientsRestController {
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public ClientsRestController(ClientRepository clientRepository, AddressRepository addressRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody RequestCreateClient request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            JsonErrorResponse error = new JsonErrorResponse("Invalid input", "Client name cannot be empty");
            return ResponseEntity.status(400).body(error); // 400 Bad Request
        }

        Address address = new Address(request.getAddress());
        Client client = new Client(request.getName());
        client.setAddress(address);
        client.addPhone(new Phone(request.getNumber()));
        var savedClient = clientRepository.save(client);
        addressRepository.save(address);

        return ResponseEntity.status(201).body(savedClient);
    }
}
