package ru.otus.cachehw;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.models.Client;
import ru.otus.cachehw.repository.DataTemplate;
import ru.otus.cachehw.sessionmanager.TransactionManager;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final MyCache<Long, Client> clientCache = new MyCache<>();

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                clientCache.put(savedClient.getId(), savedClient);
                // очистить всех клиентов
                return savedClient;
            } else {
                var savedClient = clientDataTemplate.update(session, clientCloned);
                log.info("updated client: {}", savedClient);
                clientCache.put(savedClient.getId(), savedClient);
                return savedClient;
            }
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = clientCache.get(id);
        if (cachedClient != null) {
            log.info("client found in cache: {}", cachedClient);
            return Optional.of(cachedClient);
        }

        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    public Optional<Client> getClientNoCache(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
