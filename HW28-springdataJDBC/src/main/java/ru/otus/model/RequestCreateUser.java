package ru.otus.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestCreateUser {
    String name;
    String login;
    String password;
}
