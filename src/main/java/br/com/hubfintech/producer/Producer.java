package br.com.hubfintech.producer;

import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Producer extends BaseProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }
}
