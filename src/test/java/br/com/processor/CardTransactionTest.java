package br.com.processor;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.processor.constants.TransactionResultCode;
import br.com.processor.dto.CardDTO;
import br.com.processor.dto.TransactionRequestDTO;
import br.com.processor.dto.TransactionResponseDTO;
import br.com.processor.entities.Card;
import br.com.processor.entities.CardTransaction;
import br.com.processor.repositories.CardRepository;
import br.com.processor.repositories.CardTransactionRepository;
import br.com.processor.services.CardTransactionService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CardTransactionTest
{
	@Autowired
	private CardTransactionService service;

	@MockBean
	private CardTransactionRepository cardTransactionRepository;

	@MockBean
	private CardRepository cardRepository;

	private TransactionRequestDTO transactionRequestDTO;

	private TransactionRequestDTO transactionRequestInvalidDTO;

	private Card card;

	private CardDTO cardDTO;

	@Autowired
	private ObjectMapper mapper;

	public <T> T getObjectFromFile(String path, Class<T> objectClass) throws IOException
	{
		File file = new ClassPathResource(path).getFile();
		return mapper.readValue(file, objectClass);
	}

	@Before
	public void setUp() throws Exception {
		transactionRequestDTO = getObjectFromFile("json/TransactionRequest.json", TransactionRequestDTO.class);
		transactionRequestInvalidDTO = getObjectFromFile("json/TransactionRequestInvalid.json", TransactionRequestDTO.class);
		card = getObjectFromFile("json/Card.json", Card.class);
		cardDTO = getObjectFromFile("json/CardDTO.json", CardDTO.class);
	}

	@Test
	public void processRequestTransactionSucessTest() {
		doReturn(card).when(cardRepository).findCardByCardnumber(Mockito.anyString());
		TransactionResponseDTO transactionResponseDTO = this.service.processRequestTransaction(transactionRequestDTO);
		assertEquals(transactionResponseDTO.getCode(), TransactionResultCode.APPROVED.getCode());
	}

	@Test
	public void processRequestTransactionNoSucessTest() {
		doReturn(new CardTransaction()).when(cardTransactionRepository).save(Mockito.any());
		TransactionResponseDTO transactionResponseDTO = this.service.processRequestTransaction(transactionRequestInvalidDTO);
		assertEquals(transactionResponseDTO.getCode(), TransactionResultCode.PROCESSING_ERROR.getCode());
	}
}
