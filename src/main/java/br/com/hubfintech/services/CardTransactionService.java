package br.com.hubfintech.services;

import br.com.hubfintech.constants.TransactionResultCode;
import br.com.hubfintech.constants.TransactionType;
import br.com.hubfintech.dto.TransactionRequestDTO;
import br.com.hubfintech.dto.TransactionResponseDTO;
import br.com.hubfintech.entities.Card;
import br.com.hubfintech.entities.CardTransaction;
import br.com.hubfintech.repositories.CardRepository;
import br.com.hubfintech.repositories.CardTransactionRepository;
import br.com.hubfintech.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

public class CardTransactionService {

    @Autowired
    CardTransactionRepository cardTransactionRepository;

    @Autowired
    CardRepository cardRepository;

    public TransactionResponseDTO processRequestTransaction(TransactionRequestDTO request){

        if(request != null){
            
            TransactionResponseDTO response = new TransactionResponseDTO();
            response.setAction(request.getAction());
            
            TransactionType tt = getTransactionTypeByValue(request.getAction());
            
            BigDecimal req_ammount = Util.convertStringToBigDecimal(request.getAmount());
            
            if((tt == null) || (req_ammount == null))
                return createTransactionResponseDTO(tt, TransactionResultCode.PROCESSING_ERROR,
                                                           this.saveTransaction(request.getCardnumber(), tt, TransactionResultCode.PROCESSING_ERROR, req_ammount));
                
            if(req_ammount.compareTo(BigDecimal.ZERO) <= 0)
                return createTransactionResponseDTO(tt, TransactionResultCode.PROCESSING_ERROR,
                                                           this.saveTransaction(request.getCardnumber(), tt, TransactionResultCode.PROCESSING_ERROR, req_ammount));
            

            Card card = cardRepository.findCardByCardnumber(request.getCardnumber());

            if(card != null){

                switch(tt){

                    case WITHDRAW:{

                        BigDecimal avaliable_amount = card.getAvailableAmount().subtract(req_ammount);

                        if(avaliable_amount.compareTo(BigDecimal.ZERO) >= 0){

                            TransactionResultCode tc = TransactionResultCode.APPROVED;
                            response.setCode(tc.getCode());

                            card = this.saveTransaction(card, tt, tc, req_ammount, avaliable_amount);

                            response.setAuthorization_code(Util.converteLongAuthorizationCode(getIdLastCardTransaction(card)));

                        }else{
                            return createTransactionResponseDTO(tt, TransactionResultCode.INSUFFICIENT_FUNDS,
                                                                       this.saveTransaction(request.getCardnumber(), tt, TransactionResultCode.INSUFFICIENT_FUNDS, req_ammount));
                        }

                        break;
                    }

                    default:{
                        return createTransactionResponseDTO(tt, TransactionResultCode.PROCESSING_ERROR,
                                                                   this.saveTransaction(request.getCardnumber(), tt, TransactionResultCode.PROCESSING_ERROR, req_ammount));
                    }
                }

                return response;

            }else{
                return createTransactionResponseDTO(tt, TransactionResultCode.INVALID_ACCOUNT,
                        this.saveTransaction(request.getCardnumber(), tt, TransactionResultCode.INVALID_ACCOUNT, req_ammount));
            }

        }
        return createTransactionResponseDTO(null, TransactionResultCode.PROCESSING_ERROR,
                this.saveTransaction(request.getCardnumber(), null, TransactionResultCode.PROCESSING_ERROR, null));
    }
    
    public TransactionType getTransactionTypeByValue(String value){
        
        for(TransactionType tt : TransactionType.values()){
            
            if(tt.getType().equals(value))
                return tt;
        }
        
        return null;
    }
    
    public CardTransaction getLastCardTransaction(Card card){
        
        if((card != null) && (card.getTransactions() != null) && (card.getTransactions().size() > 0))
            return card.getTransactions().get(card.getTransactions().size() - 1);
     
        return null;
    }
    
    public Long getIdLastCardTransaction(Card card){
        
        CardTransaction t = getLastCardTransaction(card);
        
        if(t != null)
            return t.getId();
        
        return null;
    }
    
    public Card saveTransaction(Card card, TransactionType transactionType, TransactionResultCode result_code, BigDecimal transactionAmount, BigDecimal cardAvaliableAmount){

        CardTransaction transaction = new CardTransaction();

        transaction.setCardnumber(card.getCardnumber());
        transaction.setTransactionType(transactionType);
        transaction.setResultCode(result_code);
        transaction.setAmount(transactionAmount);
        transaction.setDate(new Date());

        card.getTransactions().add(transaction);

        cardTransactionRepository.save(transaction);

        if(cardAvaliableAmount != null){
            card.setAvailableAmount(cardAvaliableAmount);
            cardRepository.save(card);
        }

        return card;
    }

    public Long saveTransaction(String cardNumber, TransactionType transactionType, TransactionResultCode resultCode, BigDecimal transactionAmount){

        CardTransaction transaction = new CardTransaction();

        transaction.setCardnumber(cardNumber);
        transaction.setAmount(transactionAmount);
        transaction.setTransactionType(transactionType);
        transaction.setResultCode(resultCode);
        transaction.setDate(new Date());

        Long id = cardTransactionRepository.save(transaction).getId();

        return id;
    }
    
    static public TransactionResponseDTO createTransactionResponseDTO(TransactionType tt, TransactionResultCode trc, Long ac){
    
        TransactionResponseDTO response_dto = new TransactionResponseDTO();
        
        if(tt != null)
            response_dto.setAction(tt.getType());
        
        response_dto.setCode(trc.getCode());
        
        if(ac != null)
            response_dto.setAuthorization_code(Util.converteLongAuthorizationCode(ac));
     
        return response_dto;
    }
}
