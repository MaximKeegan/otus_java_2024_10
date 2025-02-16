package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    //    @SequenceGenerator(name = "address_gen", sequenceName = "address_seq", initialValue = 1, allocationSize = 1)
    //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    //    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    //    @OneToOne(mappedBy = "address")
    //    private Client client;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", unique = true)
    private Client client;

    public Address(Long id, String address) {
        this.id = id;
        this.address = address;
    }

    public Address(String address) {
        this.id = null;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" + "id=" + id + ", address='" + address + '\'' + '}';
    }
}
