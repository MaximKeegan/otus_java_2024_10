package ru.otus.cachehw;

import java.util.*;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.cachehw.models.Client;
import ru.otus.cachehw.repository.DataTemplateHibernate;
import ru.otus.cachehw.repository.HibernateUtils;
import ru.otus.cachehw.sessionmanager.TransactionManagerHibernate;

public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        @SuppressWarnings("java:S1604")
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put("1", 1);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);

        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///

        var hwCache = new MyCache<Long, Client>();

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, hwCache);
        var client = dbServiceClient.saveClient(new Client("dbServiceFirst"));

        logger.info("clientInserted:{}", client);
        var clientSelected = dbServiceClient
                .getClientNoCache(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        logger.info("clientSelectedNoCache:{}", clientSelected);
        var clientSelectedCache = dbServiceClient
                .getClient(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        logger.info("clientSecondSelectedCache:{}", clientSelectedCache);
    }
}
