package ru.otus.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

public class DatabaseClientDao extends BaseDao implements ClientsDao {

    public DatabaseClientDao() {
        initializeTestData();
    }

    @Override
    public Optional<Client> findById(long id) {
        return execute(session -> session.find(Client.class, id));
    }

    @Override
    public List<Client> listClients() {
        var clients = execute(session -> session.createQuery("from Client").getResultList())
                .orElseGet(List::of);

        return clients;
    }

    @Override
    public Optional<Client> createClient(Client client) {
        return executeInTransaction(session -> {
            session.persist(client);
            return client;
        });
    }

    @Override
    public Optional<Client> addPhone(Long clientId, String number) {
        return executeWithClient(clientId, client -> {
            Phone phone = new Phone(null, number);
            phone.setClient(client);
            client.getPhones().add(phone);
            return client;
        });
    }

    @Override
    public Optional<Client> deletePhone(Long clientId, String number) {
        return executeWithClient(clientId, client -> {
            boolean removed =
                    client.getPhones().removeIf(phone -> phone.getNumber().equals(number));
            return removed ? client : null;
        });
    }

    @Override
    public Optional<Client> updateAddress(Long clientId, String address) {
        return executeWithClient(clientId, client -> {
            Address clientAddress = client.getAddress();
            if (clientAddress != null) {
                clientAddress.setAddress(address);
            } else {
                Address newAddress = new Address(null, address);
                newAddress.setClient(client);
                client.setAddress(newAddress);
            }
            return client;
        });
    }

    private Optional<Client> executeWithClient(Long clientId, Function<Client, Client> operation) {
        return executeInTransaction(session -> {
            Client client = session.find(Client.class, clientId);
            if (client == null) {
                return null;
            }
            Client result = operation.apply(client);
            if (result != null) {
                session.merge(result);
            }
            return result != null ? result : client;
        });
    }

    private void initializeTestData() {
        executeInTransaction(session -> {
            session.persist(new Client(
                    null,
                    "Vasya",
                    new Address(null, "AnyStreet"),
                    List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))));
            session.persist(
                    new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"))));
            session.persist(new Client(null, "Vasya", new Address(null, "AnyStreet"), null));
            session.persist(new Client(null, "Vasya", null, null));
            return null;
        });
    }
}
