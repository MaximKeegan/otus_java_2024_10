package ru.otus;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.model.User;
import ru.otus.repository.AddressRepository;
import ru.otus.repository.ClientRepository;
import ru.otus.repository.PhoneRepository;
import ru.otus.repository.UserRepository;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
@SpringBootApplication
public class WebServerSimpleDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {

        SpringApplication.run(WebServerSimpleDemo.class, args);
    }

    @Bean
    CommandLineRunner init(
            UserRepository userRepository,
            ClientRepository clientRepository,
            AddressRepository addressRepository,
            PhoneRepository phoneRepository) {
        return args -> {
            Address address = new Address("Main St");
            Client client = new Client("max");
            client.setAddress(address);
            client.addPhone(new Phone("123-456-7890"));
            client.addPhone(new Phone("098-765-4321"));
            clientRepository.save(client);
            addressRepository.save(address);

            userRepository.save(new User("user", "user", "user"));
            userRepository.save(new User("admin", "admin", "admin"));
        };
    }
}
