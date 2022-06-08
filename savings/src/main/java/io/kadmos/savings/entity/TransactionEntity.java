package io.kadmos.savings.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @Getter
    @Setter
    private AccountEntity account;

    @Column(name = "amount")
    @Setter
    @Getter
    private BigDecimal amount;



}
