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
@Table("phones")
public class Phone {
    @Id
    @Column("id")
    private Long id;

    @Column("number")
    private String number;

    @Column("client_id")
    private Long clientId; // Внешний ключ на Client

    public Phone(String number) {
        this.number = number;
    }

    public Phone(String number, Long clientId) {
        this.number = number;
        this.clientId = clientId;
    }
}
