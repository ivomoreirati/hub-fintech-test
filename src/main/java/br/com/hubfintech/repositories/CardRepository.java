package br.com.hubfintech.repositories;

import br.com.hubfintech.entities.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, Long>{

    @Override
    List<Card> findAll();

    Card findCardByCardnumber(String cardNumber);
}
