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
@Table("addresses")
public class Address {
    @Id
    @Column("id")
    private Long id;

    @Column("address")
    private String address;

    public Address(String address) {
        this.address = address;
    }
}
