package com.mendel.challenge.infrastructure.repository;

import com.mendel.challenge.infrastructure.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByType(String type);

    List<TransactionEntity> findByParentId(Long parentId);
}
