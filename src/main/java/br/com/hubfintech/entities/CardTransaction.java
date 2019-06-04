package br.com.hubfintech.entities;

import br.com.hubfintech.constants.TransactionResultCode;
import br.com.hubfintech.constants.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
