package ru.otus.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

public class DatabaseClientDao implements ClientsDao {

    private StandardServiceRegistry serviceRegistry;
    private Metadata metadata;
    private SessionFactory sessionFactory;

    public DatabaseClientDao() {
        makeTestDependencies();

        applyCustomSqlStatementLogger(new SqlStatementLogger(true, false, false, 0) {
            @Override
            public void logStatement(String statement) {
                super.logStatement(statement);
            }
        });

        //        var client = new Client(
        //                null,
        //                "Vasya",
        //                new Address(null, "AnyStreet"),
        //                List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333")));

        try (var session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.persist(new Client(
                    null,
                    "Vasya",
                    new Address(null, "AnyStreet"),
                    List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))));
            session.persist(
                    new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"))));
            session.persist(new Client(null, "Vasya", new Address(null, "AnyStreet"), null));

            session.getTransaction().commit();

            session.clear();
        }
    }

    @Override
    public List<Client> listClients() {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM Client", Client.class).getResultList();
        }
    }

    @Override
    public Optional<Client> createClient(Client client) {
        return Optional.empty();
    }

    private void makeTestDependencies() {
        var cfg = new Configuration();

        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");

        cfg.setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        cfg.setProperty("hibernate.connection.username", "sa");
        cfg.setProperty("hibernate.connection.password", "");

        cfg.setProperty("hibernate.show_sql", "true");
        cfg.setProperty("hibernate.format_sql", "false");
        cfg.setProperty("hibernate.generate_statistics", "true");

        cfg.setProperty("hibernate.hbm2ddl.auto", "create");
        cfg.setProperty("hibernate.enable_lazy_load_no_trans", "false");

        serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties())
                .build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(Client.class);
        metadataSources.addAnnotatedClass(Phone.class);
        metadataSources.addAnnotatedClass(Address.class);
        metadata = metadataSources.getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    private void applyCustomSqlStatementLogger(SqlStatementLogger customSqlStatementLogger) {
        var jdbcServices = serviceRegistry.getService(JdbcServices.class);
        try {
            Field field = jdbcServices.getClass().getDeclaredField("sqlStatementLogger");
            field.setAccessible(true);
            field.set(jdbcServices, customSqlStatementLogger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
