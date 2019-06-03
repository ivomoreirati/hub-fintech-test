package br.com.hubfintech.services;

import br.com.hubfintech.constants.TransactionResultCode;
import br.com.hubfintech.constants.TransactionType;
import br.com.hubfintech.entities.Card;
import br.com.hubfintech.entities.CardTransaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CardCacheService {
    
    static final private Map<String, Card> CACHE = new ConcurrentHashMap<>();
    
    static private long TRANSACTIONS_COUNT = 1;
    
    static final private LinkedList<String> LIST_CURRENT_CARDS_TRANSACTIONS = new LinkedList<>();
    
    static public List<Card> getAllCards() {

        List<Card> cards = new LinkedList<>();
        
        CACHE.keySet().stream().forEach((k) -> {
            cards.add(CACHE.get(k));
        });
        
        return cards;
    }
    
    static public Card getCard(String cardNumber){
        
        if((cardNumber != null) && (cardNumber.length() > 0) && CACHE.containsKey(cardNumber))
            return CACHE.get(cardNumber);
        
        return null;
    }
    
    static public void putCard(Card card){
        
        if((card != null) && (card.getCardnumber() != null) && (card.getCardnumber().length() > 0))
            CACHE.put(card.getCardnumber(), card);
    }
    
    synchronized static public boolean addCardListCurrentCardsTransactions(String cardNumber){
    
        if(!LIST_CURRENT_CARDS_TRANSACTIONS.contains(cardNumber)){
         
            LIST_CURRENT_CARDS_TRANSACTIONS.add(cardNumber);
            
            return true;
        }
        
        return false;
    }
    
    synchronized static public boolean removeCardListCurrentCardsTransactions(String cardNumber){
    
        if(LIST_CURRENT_CARDS_TRANSACTIONS.contains(cardNumber)){
            
            LIST_CURRENT_CARDS_TRANSACTIONS.remove(cardNumber);
         
            return true;
        }
        
        return false;
    }
    
    synchronized static public Card saveTransaction(Card card, TransactionType transactionType, TransactionResultCode result_code, BigDecimal transactionAmount, BigDecimal cardAvaliableAmount){
        
        card.setAvailableAmount(cardAvaliableAmount);
        
        CardTransaction transaction = new CardTransaction();
        
        transaction.setId(TRANSACTIONS_COUNT);
        TRANSACTIONS_COUNT++;
        
        transaction.setCardnumber(card.getCardnumber());
        transaction.setTransactionType(transactionType);
        transaction.setResultCode(result_code);
        transaction.setAmount(transactionAmount);
        transaction.setDate(new Date());
        
        card.getTransactions().add(transaction);
        
        putCard(card);
        
        CardPersistenceService.addToPersistence(card);
        
        removeCardListCurrentCardsTransactions(card.getCardnumber());
        
        return card;
    }
    
    synchronized static public Long saveInvalidTransaction(String cardNumber, TransactionType transactionType, TransactionResultCode resultCode, BigDecimal transactionAmount){
        
        CardTransaction transaction = new CardTransaction();
        
        Long id = TRANSACTIONS_COUNT;
        TRANSACTIONS_COUNT++;
        
        transaction.setId(id);
        transaction.setCardnumber(cardNumber);
        transaction.setAmount(transactionAmount);
        transaction.setTransactionType(transactionType);
        transaction.setResultCode(resultCode);
        transaction.setDate(new Date());
        
        CardPersistenceService.addToPersistence(transaction);
        
        return id;
    }
}
