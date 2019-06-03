package br.com.hubfintech.config;

import br.com.hubfintech.entities.Card;
import br.com.hubfintech.services.CardCacheService;
import br.com.hubfintech.services.CardPersistenceService;
import br.com.hubfintech.services.CardService;
import br.com.hubfintech.services.CardTransactionService;
import br.com.hubfintech.tcp.TCPServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.concurrent.Executors;

@Configuration
public class TcpConfig {
    
    @Value("${tcp.transaction.port}")
    private int TCP_PORT;
    
    @Autowired
    CardService cardService;
    
    @Autowired
    CardTransactionService cardTransactionService;
    
    @Bean
    InitializingBean init() {

        return () -> {
            
            Card c1 = new Card();
            c1.setCardnumber("1234567890123456");
            c1.setAvailableAmount(new BigDecimal("1000.00"));
            cardService.saveCard(c1);
            CardCacheService.putCard(c1);
            
            Card c2 = new Card();
            c2.setCardnumber("4485617978182589");
            c2.setAvailableAmount(new BigDecimal("1000.00"));
            cardService.saveCard(c2);
            CardCacheService.putCard(c2);
            
            System.out.println("Initial Data");
            
            for(Card c : CardCacheService.getAllCards())
                System.out.println(c);

            Executors.newSingleThreadExecutor().execute(() -> {
                try{
                    TCPServer.createServerTCP(TCP_PORT, cardTransactionService);

                }catch(Exception e){
                    System.err.println("Error create TCP MultiThread. " + e.getMessage());
                }
            });

            Executors.newSingleThreadExecutor().execute(() -> {
                try{
                    new CardPersistenceService(cardService, cardTransactionService).processing();

                }catch(Exception e){
                    System.err.println("Error create server TCP MultiThread. " + e.getMessage());
                }
            });
        };
    }
}
