package ru.otus.dao;

import java.util.Optional;
import ru.otus.model.User;

public class DatabaseUserDao extends BaseDao implements UserDao {

    public DatabaseUserDao() {
        initializeTestData();
    }

    @Override
    public Optional<User> findById(long id) {
        return execute(session -> session.find(User.class, id));
    }

    @Override
    public Optional<User> findRandomUser() {
        return execute(session -> session.createQuery("FROM User ORDER BY RAND()", User.class)
                .setMaxResults(1)
                .uniqueResult());
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return execute(session -> session.createQuery("FROM User WHERE login = :login", User.class)
                .setParameter("login", login)
                .uniqueResult());
    }

    public Optional<User> saveUser(User user) {
        return executeInTransaction(session -> {
            session.persist(user);
            return user;
        });
    }

    private void initializeTestData() {
        executeInTransaction(session -> {
            session.persist(new User("Admin", "admin", "password"));
            return null;
        });
    }
}
