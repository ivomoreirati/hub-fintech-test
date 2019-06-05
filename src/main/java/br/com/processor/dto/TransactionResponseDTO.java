package br.com.processor.dto;

import lombok.Data;

@Data
public class TransactionResponseDTO {
    
    private String action;
    
    private String code;
            
    private String authorization_code;
    
}
