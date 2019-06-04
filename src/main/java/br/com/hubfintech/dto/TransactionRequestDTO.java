package br.com.hubfintech.dto;

import lombok.Data;

@Data
public class TransactionRequestDTO {
    
    private String action;
    
    private String cardnumber;
            
    private String amount;
    
}
