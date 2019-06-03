package br.com.hubfintech.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class CardDTO {
    
    private String cardnumber;
    
    private String availableAmount;
    
    private List<CardTransactionDTO> transactions = new LinkedList<>();
    
}
