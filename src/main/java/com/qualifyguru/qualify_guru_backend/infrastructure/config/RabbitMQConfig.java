package com.qualifyguru.qualify_guru_backend.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String GENERATION_EXCHANGE = "generation.exchange";
    public static final String RESUME_GENERATION_QUEUE = "resume.generation.queue";
    public static final String RESUME_GENERATION_ROUTING_KEY = "resume.generation.routing.key";

    @Bean
    public Queue resumeGenerationQueue() {
        return new Queue(RESUME_GENERATION_QUEUE, true);
    }

    @Bean
    public DirectExchange generationExchange() {
        return new DirectExchange(GENERATION_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue resumeGenerationQueue, DirectExchange generationExchange) {
        return BindingBuilder.bind(resumeGenerationQueue)
                .to(generationExchange)
                .with(RESUME_GENERATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
