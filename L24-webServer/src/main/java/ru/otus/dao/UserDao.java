package ru.otus.dao;

import jakarta.transaction.SystemException;
import java.util.Optional;
import ru.otus.model.User;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);

    Optional<User> saveUser(User user) throws SystemException;
}
