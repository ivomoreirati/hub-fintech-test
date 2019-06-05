package br.com.processor.repositories;

import br.com.processor.entities.CardTransaction;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardTransactionRepository  extends CrudRepository<CardTransaction, Long> {

    @Override
    List<CardTransaction> findAll();

}
