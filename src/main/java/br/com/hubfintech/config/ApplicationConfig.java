package br.com.hubfintech.config;

import br.com.hubfintech.services.CardService;
import br.com.hubfintech.services.CardTransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig
{
	@Bean
	public CardService getCardService(){

		return new CardService();
	}

	@Bean
	public CardTransactionService getCardTransactionService(){

		return new CardTransactionService();
	}

}
