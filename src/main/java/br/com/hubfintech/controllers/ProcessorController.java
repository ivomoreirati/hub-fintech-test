package br.com.hubfintech.controllers;

import br.com.hubfintech.dto.CardDTO;
import br.com.hubfintech.exceptions.ProcessorBadRequestException;
import br.com.hubfintech.services.CardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProcessorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorController.class);
    
    @Autowired
    CardService cardService;
    
    @GetMapping(value="/v1/cards")
    @ApiOperation(value = "List cards", response = String.class)
    public ResponseEntity<List<CardDTO>> getAllCards(){
        
        List<CardDTO> cards = cardService.getListCardDTO();
        
        if(cards.isEmpty()){
            throw new ProcessorBadRequestException("Not exists cards in database!");
        }else{
            return ResponseEntity.ok(cards);
        }
    }
    
    @GetMapping(value="/v1/card/{cardnumber}")
    @ApiOperation(value = "get card by number ", response = String.class)
    public ResponseEntity<CardDTO> getCardByCardNumber(@ApiParam(value = "Card Number", required = true) @PathVariable("cardnumber") String cardNumber){
        
        CardDTO card = cardService.getCardDTO(cardService.getCardByCardNumber(cardNumber));

        if(card == null){
            throw new ProcessorBadRequestException("Not exist card by number in database!");
        }else{
            return ResponseEntity.ok(card);
        }
    }
}
