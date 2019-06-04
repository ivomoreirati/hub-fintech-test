package br.com.hubfintech.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.hubfintech.entities.Card;

public interface CardRepository extends CrudRepository<Card, Long>{

    @Override
    List<Card> findAll();

    Card findCardByCardnumber(String cardNumber);
}
