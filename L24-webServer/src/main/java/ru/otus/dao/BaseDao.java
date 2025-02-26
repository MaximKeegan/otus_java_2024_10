package ru.otus.dao;

import java.util.Optional;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.*;

public abstract class BaseDao {
    private static final Logger logger = LoggerFactory.getLogger(BaseDao.class);
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = initializeSessionFactory(User.class, Client.class, Phone.class, Address.class);
    }

    protected BaseDao() {}

    protected <T> Optional<T> executeInTransaction(Function<Session, T> operation) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            T result = operation.apply(session);
            session.getTransaction().commit();
            if (result != null && session.contains(result)) {
                session.detach(result);
            }
            return Optional.ofNullable(result);
        } catch (Exception e) {
            logger.error("Error executing transaction: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    protected <T> Optional<T> execute(Function<Session, T> operation) {
        try (Session session = sessionFactory.openSession()) {
            T result = operation.apply(session);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            logger.error("Error executing: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    private static SessionFactory initializeSessionFactory(Class<?>... entityClasses) {
        Configuration cfg = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
                .setProperty("hibernate.connection.username", "sa")
                .setProperty("hibernate.connection.password", "")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "false")
                .setProperty("hibernate.generate_statistics", "true")
                .setProperty("hibernate.hbm2ddl.auto", "create")
                .setProperty("hibernate.enable_lazy_load_no_trans", "false");

        var registry = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties())
                .build();

        var sources = new MetadataSources(registry);
        for (Class<?> entityClass : entityClasses) {
            sources.addAnnotatedClass(entityClass);
        }

        var metadata = sources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }
}
