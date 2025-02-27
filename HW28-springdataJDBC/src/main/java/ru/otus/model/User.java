package ru.otus.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("users")
public class User {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("login")
    private String login;

    @Column("password")
    private String password;

    public User(String name, String login, String password) {
        //            this.id = null;
        this.name = name;
        this.login = login;
        this.password = password;
    }
    //
    //    public User(long id, String name, String login, String password) {
    //        this.id = id;
    //        this.name = name;
    //        this.login = login;
    //        this.password = password;
    //    }
}
