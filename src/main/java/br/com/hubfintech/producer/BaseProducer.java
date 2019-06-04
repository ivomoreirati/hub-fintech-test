package br.com.hubfintech.producer;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hubfintech.dto.CardDTO;
import br.com.hubfintech.dto.CardTransactionDTO;
import br.com.hubfintech.dto.TransactionRequestDTO;
import br.com.hubfintech.util.RabbitQueueConfig;

public abstract class BaseProducer
{

    protected static boolean VERBOSE = false;


    /**
     * Send card transactions to queue
     * @param transactionRequestDTO
     */
    public void sendTransactionRequest(TransactionRequestDTO transactionRequestDTO) {
        sendMessage(transactionRequestDTO, RabbitQueueConfig.HUBFINTECH_EXCHANGE,
                RabbitQueueConfig.TRANSACTION_REQUEST_ROUTING_KEY, RabbitQueueConfig.CARD_TRANSACTION_MESSAGE_TYPE);
    }


    /**
     * Send card to queue
     * @param cardDTO
     */
    public void sendCard(CardDTO cardDTO) {
        sendMessage(cardDTO, RabbitQueueConfig.HUBFINTECH_EXCHANGE,
                RabbitQueueConfig.CARD_ROUTING_KEY, RabbitQueueConfig.CARD_MESSAGE_TYPE);
    }

    /**
     * Send card transactions to queue
     * @param cardTransactionDTO
     */
    public void sendCardTransaction(CardTransactionDTO cardTransactionDTO) {
        sendMessage(cardTransactionDTO, RabbitQueueConfig.HUBFINTECH_EXCHANGE,
                RabbitQueueConfig.CARD_TRANSACTION_ROUTING_KEY, RabbitQueueConfig.CARD_TRANSACTION_MESSAGE_TYPE);
    }

    public void sendMessage(Object object, String exchange, String routingKey) {
        sendMessage(object, exchange, routingKey, null);
    }

    public void sendMessage(Object object, String exchange, String routingKey, String messageType) {

        if (VERBOSE) {
            try {
                getLogger().info("Sending " + routingKey + " message...");
                getLogger().info("Exchange: " + exchange);

                String message = new ObjectMapper().writeValueAsString(object);
                getLogger().info("message {}.", message);
            } catch (JsonProcessingException s) {
                getLogger().error("json processing ", s);
            }
        }

        getRabbitTemplate().convertAndSend(exchange, routingKey, object,
                m -> postProcessMessage(messageType, m));
    }

    private Message postProcessMessage(String messageType, Message m) {
        if (messageType != null) {
            m.getMessageProperties().getHeaders().put("messageType", messageType);
        }

        UUID uuId = UUID.randomUUID();
        getLogger().debug("uuId: " + uuId);
        m.getMessageProperties().getHeaders().put("id", uuId);

        // workaround to avoid class not found error when deserialize
        m.getMessageProperties().getHeaders().remove("__TypeId__");
        return m;
    }

    protected abstract RabbitTemplate getRabbitTemplate();

    protected abstract Logger getLogger();
}
