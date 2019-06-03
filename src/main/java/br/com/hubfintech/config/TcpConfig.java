package br.com.hubfintech.config;

import br.com.hubfintech.entities.Card;
import br.com.hubfintech.services.CardService;
import br.com.hubfintech.services.CardTransactionService;
import br.com.hubfintech.tcp.TCPServer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class TcpConfig {
    
    @Value("${tcp.transaction.port}")
    private int TCP_PORT;
    
    @Autowired
    CardService cardService;
    
    @Autowired
    CardTransactionService cardTransactionService;
    
    @Bean
    InitializingBean inicialize() {

        return () -> {
            
            Card c1 = new Card();
            c1.setCardnumber("1234567890123456");
            c1.setAvailableAmount(new BigDecimal("1000.00"));
            cardService.saveCard(c1);

            Card c2 = new Card();
            c2.setCardnumber("4485617978182589");
            c2.setAvailableAmount(new BigDecimal("1000.00"));
            cardService.saveCard(c2);

            log.info("Initial Data");

            cardService.getAllCards().forEach(card -> {
                log.info(card.toString());
            });

            Executors.newSingleThreadExecutor().execute(() -> {
                try{
                    TCPServer.createServerTCP(TCP_PORT, cardTransactionService);

                }catch(Exception e){
                    log.error("Error create TCP MultiThread. " + e.getMessage());
                }
            });

            Executors.newSingleThreadExecutor().execute(() -> {
                try{
                    new CardStoreService(cardService, cardTransactionService).processing();

                }catch(Exception e){
                    log.error("Error create server TCP MultiThread. " + e.getMessage());
                }
            });
        };
    }
}
