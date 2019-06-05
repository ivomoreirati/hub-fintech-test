package br.com.processor.entities;

import br.com.processor.constants.TransactionResultCode;
import br.com.processor.constants.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
public class CardTransaction implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; 
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    private String cardnumber;
    
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    
    @Enumerated(EnumType.STRING)
    private TransactionResultCode resultCode;
    
    private BigDecimal amount;

}
