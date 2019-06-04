package br.com.hubfintech.util;

public class RabbitQueueConfig
{

    public static final String HUBFINTECH_EXCHANGE = "hubfintech.processor";

    public static final String CARD_QUEUE_NAME = "processor.card.update";

    public static final String CARD_TRANSACTION_QUEUE_NAME = "processor.card.transaction.update";

    public static final String CARD_ROUTING_KEY = CARD_QUEUE_NAME;

    public static final String CARD_TRANSACTION_ROUTING_KEY = CARD_TRANSACTION_QUEUE_NAME;

    public static final String CARD_MESSAGE_TYPE = "card";

    public static final String CARD_TRANSACTION_MESSAGE_TYPE = "card_transaction";


    public static final String TRANSACTION_REQUEST_QUEUE_NAME = "processor.transaction.insert";

    public static final String TRANSACTION_REQUEST_ROUTING_KEY = TRANSACTION_REQUEST_QUEUE_NAME;

}
