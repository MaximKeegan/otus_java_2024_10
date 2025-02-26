package ru.otus.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public <E> Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;

        if (address != null) {
            this.address = address.clone();
            this.address.setClient(this);
        } else {
            this.address = null;
        }

        if (phones != null) {
            this.phones = new ArrayList<>();
            for (Phone phone : phones) {
                Phone phoneCopy = phone.clone();
                phoneCopy.setClient(this);
                this.phones.add(phoneCopy);
            }
        } else {
            this.phones = null;
        }
    }

    public String listPhones() {
        return phones.stream().map(Phone::getNumber).collect(Collectors.joining(", "));
    }

    public String listAddress() {
        if (address != null) {
            return address.getAddress();
        }
        return "";
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        return new Client(this.id, this.name, this.address, this.phones);
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + ", phones='" + phones + '\'' + ", address='"
                + address + '\'' + '}';
    }
}
