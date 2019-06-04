package br.com.hubfintech.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CardDTO {
    
    private String cardnumber;
    
    private String availableAmount;
    
    private List<CardTransactionDTO> transactions = new ArrayList<>();
    
}
