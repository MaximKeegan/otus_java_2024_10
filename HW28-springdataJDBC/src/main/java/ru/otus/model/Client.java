package ru.otus.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("clients")
public class Client {
    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones = new HashSet<>();

    public Client(String name) {
        this.name = name;
    }

    public void addPhone(Phone phone) {
        this.phones.add(phone);
    }
}
