package ru.otus.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.model.*;
import ru.otus.repository.AddressRepository;
import ru.otus.repository.ClientRepository;
import ru.otus.repository.PhoneRepository;

@RestController
@RequestMapping("/api/clients")
public class ClientsRestController {
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;

    @Autowired
    public ClientsRestController(
            ClientRepository clientRepository, AddressRepository addressRepository, PhoneRepository phoneRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
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

    @DeleteMapping("/{clientId}/phone/{phoneId}")
    public ResponseEntity<?> deletePhone(@PathVariable Long clientId, @PathVariable Long phoneId) {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            JsonErrorResponse error = new JsonErrorResponse("Invalid input", "Client not found");
            return ResponseEntity.status(404).body(error);
        }

        phoneRepository.deleteById(phoneId);
        return ResponseEntity.status(200).body(new JsonErrorResponse("Phone deleted", "Phone Deleted"));
    }

    @PostMapping("/{clientId}/phone")
    public ResponseEntity<?> addPhone(@PathVariable Long clientId, @RequestBody RequestAddPhone request) {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            JsonErrorResponse error = new JsonErrorResponse("Invalid input", "Client not found");
            return ResponseEntity.status(404).body(error);
        }

        if (request.getNumber() == null || request.getNumber().trim().isEmpty()) {
            return ResponseEntity.status(400)
                    .body(new JsonErrorResponse("Invalid input", "Phone number cannot be empty"));
        }

        Phone phone = new Phone(request.getNumber(), clientId);

        Phone savedPhone = phoneRepository.save(phone);
        return ResponseEntity.status(201).body(savedPhone);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClient(@PathVariable Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            JsonErrorResponse error = new JsonErrorResponse("Invalid input", "Client not found");
            return ResponseEntity.status(404).body(error);
        }

        clientRepository.deleteById(clientId);
        return ResponseEntity.status(201).body(new JsonErrorResponse("Client deleted", "Client Deleted"));
    }

    @PutMapping("/{clientId}/{field}")
    public ResponseEntity<?> editClient(
            @PathVariable Long clientId, @PathVariable String field, @RequestBody RequestEditClient request) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            JsonErrorResponse error = new JsonErrorResponse("Invalid input", "Client not found");
            return ResponseEntity.status(404).body(error);
        }

        Client client = clientOpt.get();

        if (field.equals("name")) {
            client.setName(request.getValue());
            clientRepository.save(client);
            return ResponseEntity.status(201).body(new JsonErrorResponse("Client name updated", "Client name updated"));
        }

        if (field.equals("address")) {
            Optional<Address> addressOpt =
                    addressRepository.findById(client.getAddress().getId());
            if (addressOpt.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(new JsonErrorResponse(
                                "Address not found",
                                "Address with ID: " + client.getAddress().getId() + " not found"));
            }
            Address address = addressOpt.get();
            address.setAddress(request.getValue());
            Address updatedAddress = addressRepository.save(address);
            return ResponseEntity.status(201).body(new JsonErrorResponse("Address updated", "Address updated"));
        }
        return ResponseEntity.status(400).body(new JsonErrorResponse("bad Request", "Bad request"));
    }
}
