package br.com.hubfintech.listener;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import br.com.hubfintech.exceptions.ProcessorBadRequestException;
import br.com.hubfintech.producer.IProcessor;
import br.com.hubfintech.producer.MessagesTypeEnum;
import br.com.hubfintech.util.RabbitQueueConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProcessorListener
{

    private static final String MESSAGE_ID = "id";

    private static final String MESSAGE_TYPE_HEADER_KEY = "messageType";

    @Autowired
    private List<IProcessor> processors;

    @RabbitListener(queues = RabbitQueueConfig.CARD_QUEUE_NAME, concurrency = "1-50")
    public void cardsReceiver(final Message message,
                                 @Header(value = MESSAGE_TYPE_HEADER_KEY, required = false)
                                         String messageType,
                                 @Header(value = MESSAGE_ID, required = false) String messageId)
            throws Exception {

        log.debug("Card receiver message id [{}].", messageId);
        receiver(messageType, message);
    }

    @RabbitListener(queues = RabbitQueueConfig.CARD_TRANSACTION_QUEUE_NAME, concurrency = "1")
    public void cardTransactionsReceiver(final Message message,
                                 @Header(value = MESSAGE_TYPE_HEADER_KEY, required = false)
                                         String messageType,
                                 @Header(value = MESSAGE_ID, required = false) String messageId)
            throws Exception {

        log.debug("Card Transaction receiver message id [{}].", messageId);
        receiver(messageType, message);
    }

    private void receiver(final String messageType, final Message message) throws Exception {

        if (Arrays.stream(MessagesTypeEnum.values()).noneMatch(v -> v.getValue().equals(messageType))) {
            log.warn("[WARNING] Invalid message type: " + messageType);
            return;
        }

        IProcessor processor = getProcessor(MessagesTypeEnum.valueOf(messageType.toUpperCase()));

        if (Objects.nonNull(processor)) {
            processor.processMessage(new String(message.getBody(), StandardCharsets.UTF_8));
        }
    }

    private IProcessor getProcessor(final MessagesTypeEnum messageType) {
        return processors.stream().filter(iProcessor -> iProcessor.getMessageStatus().equals(messageType)).findFirst()
                .orElseThrow(ProcessorBadRequestException::new);
    }
}
