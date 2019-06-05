package br.com.processor.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.processor.entities.Card;

public interface CardRepository extends CrudRepository<Card, Long>{

    @Override
    List<Card> findAll();

    Card findCardByCardnumber(String cardNumber);
}
