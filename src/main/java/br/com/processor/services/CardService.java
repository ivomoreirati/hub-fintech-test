package br.com.processor.services;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.processor.dto.CardDTO;
import br.com.processor.dto.CardTransactionDTO;
import br.com.processor.entities.Card;
import br.com.processor.entities.CardTransaction;
import br.com.processor.repositories.CardRepository;
import br.com.processor.util.Util;

/**
 * Class for manipulate cards . . .
 *
 * @author Ivo Moreira
 *
 */

@Service
public class CardService {
    
     @Autowired
     CardRepository cardRepository;

    public Card saveCard(Card c) {
        
        return cardRepository.save(c);
    }

    @Cacheable("card")
    public Card getCardByCardNumber(String cardnumber) {

        return cardRepository.findCardByCardnumber(cardnumber);
    }

    @Cacheable("cards")
    public List<Card> getAllCards(){
        return cardRepository.findAll();
    }

    public CardDTO getCardDTO(Card c){

        if(c != null){

            CardDTO dto = new CardDTO();
            dto.setCardnumber(c.getCardnumber());
            dto.setAvailableAmount(Util.convertBigDecimalToString(c.getAvailableAmount()));

            if((c.getTransactions() != null) && (c.getTransactions().size() > 0)){

                if(c.getTransactions().size() <= 10) dto.setTransactions(getListCardTransactionDTO(c.getTransactions()));
                else dto.setTransactions(getListCardTransactionDTO(c.getTransactions().subList(c.getTransactions().size() - 10,
                                                                   c.getTransactions().size())));
            }

            return dto;
        }

        return null;
    }

    public List<CardDTO> getListCardDTO(){
        List<CardDTO> result = new LinkedList<>();
        getAllCards().stream().forEach(card -> {
            result.add(getCardDTO(card));
        });

        if((result != null) && (result.size() > 0)){
            return result;
        }
        return null;
    }

    public CardTransactionDTO getCardTransactionDTO(CardTransaction t){

        if(t != null){

            CardTransactionDTO dto = new CardTransactionDTO();
            dto.setDate(Util.convertDate(t.getDate()));
            dto.setAmount(Util.convertBigDecimalToString(t.getAmount()));

            return dto;
        }

        return null;
    }

    public List<CardTransactionDTO> getListCardTransactionDTO(List<CardTransaction> list){

        if((list != null) && (list.size() > 0)){

            List<CardTransactionDTO> retorno = new LinkedList<>();

            list.stream().forEach((t) -> {
                                    retorno.add(getCardTransactionDTO(t));
                                });

            Collections.reverse(retorno);

            return retorno;
        }

        return null;
    }
}
