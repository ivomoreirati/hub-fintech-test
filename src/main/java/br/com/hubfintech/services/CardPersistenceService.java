package br.com.hubfintech.services;

import br.com.hubfintech.entities.Card;
import br.com.hubfintech.entities.CardTransaction;

import java.util.LinkedList;

public class CardPersistenceService {
    
    CardService cardService;
    
    CardTransactionService cardTransactionService;

    static public LinkedList<Object> PROCESSING_QUEUE = new LinkedList<>();
    

    public CardPersistenceService(CardService cardDomain, CardTransactionService cardTransactionService){
        
        this.cardService = cardDomain;
        
        this.cardTransactionService = cardTransactionService;
    }

    static public void addToPersistence(Object obj){

        PROCESSING_QUEUE.add(obj);
    }

    public void processing() {
        
        while(true){

            if(PROCESSING_QUEUE.size() > 0){

                Object obj = PROCESSING_QUEUE.poll();

                if(obj != null){

                    while(!saveDB(obj)){

                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            System.out.println("Error wait save in DB.");
                        }
                    }
                }

            }else{

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    System.out.println("Error wait save in DB.");
                }
            }
        }
    }

    private boolean saveDB(Object obj){

        try{
            if(obj instanceof Card) cardService.saveCard((Card) obj);
            else if(obj instanceof CardTransaction) cardTransactionService.saveCardTransaction((CardTransaction) obj);
            
            return true;

        }catch(Exception e){}
        
        return false;
    }
}
