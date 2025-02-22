package ru.otus.dao;

import java.util.List;
import java.util.Optional;
import ru.otus.model.Client;

public interface ClientsDao {

    Optional<Client> findById(long id);

    List<Client> listClients();

    Optional<Client> createClient(Client client);

    Optional<Client> addPhone(Long clientId, String number);

    Optional<Client> deletePhone(Long clientId, String number);

    Optional<Client> updateAddress(Long clientId, String address);
}
