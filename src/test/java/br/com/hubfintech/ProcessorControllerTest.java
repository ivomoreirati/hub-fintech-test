package br.com.hubfintech;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hubfintech.controllers.ProcessorController;
import br.com.hubfintech.dto.CardDTO;
import br.com.hubfintech.entities.Card;
import br.com.hubfintech.services.CardService;
import lombok.extern.slf4j.Slf4j;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class ProcessorControllerTest
{

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	CardService cardService;

	@Autowired
	@InjectMocks
	private ProcessorController controller;

	private static final String ENDPOINT_CARDS = "/v1/cards";

	private static final String ENDPOINT_GET_BY_CARD_NUMBER = "/v1/card";

	private static final String CARD_NUMBER = "1234567890123456";

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	public String createPayload(String path) {
		File file = null;
		String payload = null;
		try {
			file = new ClassPathResource(path).getFile();
			payload = new String(Files.readAllBytes(Paths.get(file.getPath())));
		} catch (Exception e) {
			log.error("Error loading file - error:{} ", e.getMessage());
			throw new RuntimeException(e);
		}
		return payload;
	}

	public <T> T getObjectFromFile(String path, Class<T> objectClass) throws IOException
	{
		File file = new ClassPathResource(path).getFile();
		return mapper.readValue(file, objectClass);
	}

	@Test
	public void getAll() throws Exception {
		String uri = ENDPOINT_CARDS;

		List<CardDTO> cards = getObjectFromFile("json/Cards.json", ArrayList.class);

		when(cardService.getListCardDTO()).thenReturn(cards);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(content().string(createPayload("json/Cards.json"))).andReturn();
	}

	@Test
	public void getByCardByNumber() throws Exception {
		String uri = ENDPOINT_GET_BY_CARD_NUMBER + "/"+CARD_NUMBER;

		CardDTO cardDTO = getObjectFromFile("json/CardDTO.json", CardDTO.class);
		when(cardService.getCardByCardNumber(Mockito.any())).thenReturn(new Card());
		when(cardService.getCardDTO(Mockito.any())).thenReturn(cardDTO);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(content().string(createPayload("json/CardDTO.json"))).andReturn();
	}

}
