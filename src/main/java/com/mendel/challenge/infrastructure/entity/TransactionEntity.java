package com.mendel.challenge.infrastructure.entity;

import com.mendel.challenge.domain.model.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "TRANSACTION")
@Getter
@Setter
public class TransactionEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6005053388287590724L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double amount;

    @Column
    private String type;

    @Column(name = "PARENT_ID", nullable = true)
    private Long parentId;

    public static TransactionEntity fromDomain(Transaction domain) {
        TransactionEntity entity = new TransactionEntity();
        entity.id = domain.id();
        entity.amount = domain.amount();
        entity.type = domain.type();
        entity.parentId = domain.parentId();
        return entity;
    }

}
