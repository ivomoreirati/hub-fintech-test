package br.com.hubfintech.services;

import br.com.hubfintech.dto.CardDTO;
import br.com.hubfintech.dto.CardTransactionDTO;
import br.com.hubfintech.entities.Card;
import br.com.hubfintech.entities.CardTransaction;
import br.com.hubfintech.repositories.CardRepository;
import br.com.hubfintech.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardService {
    
     @Autowired
     CardRepository cardRepository;
     
    public Card saveCard(Card c) {
        
        return cardRepository.save(c);
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

    public List<CardDTO> getListCardDTO(List<Card> list){

        if((list != null) && (list.size() > 0)){

            List<CardDTO> result = new LinkedList<>();

            list.stream().forEach((c) -> {
                result.add(getCardDTO(c));
                                });

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
