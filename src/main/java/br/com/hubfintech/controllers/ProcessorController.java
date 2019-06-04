package br.com.hubfintech.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.hubfintech.dto.CardDTO;
import br.com.hubfintech.entities.Card;
import br.com.hubfintech.exceptions.ProcessorBadRequestException;
import br.com.hubfintech.services.CardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class ProcessorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorController.class);
    
    @Autowired
    CardService cardService;
    
    @GetMapping(value="/v1/cards")
    @ApiOperation(value = "View cards", response = CardDTO.class, responseContainer="List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "View cards sucessfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal Server Error, check the Body Error Response")

    })
    public ResponseEntity<List<CardDTO>> getCards(){
        
        List<CardDTO> cards = cardService.getListCardDTO();
        
        if(cards.isEmpty()){
            throw new ProcessorBadRequestException("Not exists cards in database!");
        }else{
            return ResponseEntity.ok(cards);
        }
    }
    
    @GetMapping(value="/v1/card/{cardnumber}")
    @ApiOperation(value = "get card by number", response = CardDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "View card sucessfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal Server Error, check the Body Error Response")

    })

    public ResponseEntity<CardDTO> getCardByCardNumber(@ApiParam(value = "Card Number", required = true) @PathVariable("cardnumber") String cardNumber){

        Card card = cardService.getCardByCardNumber(cardNumber);
        CardDTO cardDTO = cardService.getCardDTO(card);

        if(card == null){
            throw new ProcessorBadRequestException("Not exist card by number in database!");
        }else{
            return ResponseEntity.ok(cardDTO);
        }
    }
}
