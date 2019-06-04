package br.com.hubfintech.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.hubfintech.util.RabbitQueueConfig;

@Configuration
@EnableRabbit
public class RabbitConfig
{

    @Bean
    public TopicExchange processorExchange() {
        return new TopicExchange(RabbitQueueConfig.HUBFINTECH_EXCHANGE);
    }

    @Bean
    public Binding cardBinding(TopicExchange topicExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(
                RabbitQueueConfig.CARD_ROUTING_KEY);
    }

    @Bean
    public Queue cardQueue() {
        return QueueBuilder.durable(RabbitQueueConfig.CARD_QUEUE_NAME).build();
    }

    @Bean
    public Binding cardTransactionBinding(TopicExchange topicExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(
                RabbitQueueConfig.CARD_TRANSACTION_ROUTING_KEY);
    }

    @Bean
    public Queue cardTransactionQueue() {
        return QueueBuilder.durable(RabbitQueueConfig.CARD_TRANSACTION_QUEUE_NAME).build();
    }

    @Bean
    public Binding cardTransactionRequestBinding(TopicExchange topicExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(
                RabbitQueueConfig.TRANSACTION_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Queue cardTransactionRequestQueue() {
        return QueueBuilder.durable(RabbitQueueConfig.TRANSACTION_REQUEST_QUEUE_NAME).build();
    }

}
