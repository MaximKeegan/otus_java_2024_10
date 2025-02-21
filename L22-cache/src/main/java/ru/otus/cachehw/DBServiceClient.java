package ru.otus.cachehw;

import java.util.List;
import java.util.Optional;
import ru.otus.cachehw.models.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
