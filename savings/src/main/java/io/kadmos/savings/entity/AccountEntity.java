package io.kadmos.savings.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    @Column(name = "account_number")
    @Getter
    @Setter
    private String accountNumber;

    @OneToMany(mappedBy = "account")
    @Getter
    @Setter
    private List<TransactionEntity> transaction;

}
