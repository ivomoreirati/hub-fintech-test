package br.com.hubfintech.repositories;

import br.com.hubfintech.entities.CardTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardTransactionRepository  extends CrudRepository<CardTransaction, Long> {

    @Override
    List<CardTransaction> findAll();

}
