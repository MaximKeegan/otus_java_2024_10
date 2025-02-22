package ru.otus.dao;

import java.util.List;
import java.util.Optional;
import ru.otus.model.Client;

public interface ClientsDao {
    List<Client> listClients();

    Optional<Client> createClient(Client client);
}
