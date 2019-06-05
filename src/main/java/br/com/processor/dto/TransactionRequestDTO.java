package br.com.processor.dto;

import lombok.Data;

@Data
public class TransactionRequestDTO {
    
    private String action;
    
    private String cardnumber;
            
    private String amount;
    
}
