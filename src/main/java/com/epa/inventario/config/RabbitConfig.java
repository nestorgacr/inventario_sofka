package com.epa.inventario.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_ERROR_NAME = "error-queue";
    public static final String QUEUE_TRANSACCION_NAME = "transactions-queue";
    public static final String EXCHANGE_NAME = "transactions-exchange";

    public static final String ROUTING_ERROR_KEY_NAME = "error.routing.key";
    public static final String ROUTING_TRANSACCION_KEY_NAME = "transactions.routing.key";


    @Value("${rabbit.uri}")
    public static String URI_NAME;

    @Bean
    public static URI uri(@Value("${rabbit.uri}") String uri) {
        URI_NAME = uri;
        return URI.create(uri);
    }
    @Bean
    public AmqpAdmin amqpAdmin(URI uri) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(uri);
        var amqpAdmin =  new RabbitAdmin(connectionFactory);

        var exchange = new TopicExchange(EXCHANGE_NAME);

        var queue_error = new Queue(QUEUE_ERROR_NAME, true, false, false);
        var queue_transaccion = new Queue(QUEUE_TRANSACCION_NAME, true, false, false);

        amqpAdmin.declareExchange(exchange);

        amqpAdmin.declareQueue(queue_error);
        amqpAdmin.declareQueue(queue_transaccion);

        amqpAdmin.declareBinding(BindingBuilder.bind(queue_error).to(exchange).with(ROUTING_ERROR_KEY_NAME));
        amqpAdmin.declareBinding(BindingBuilder.bind(queue_transaccion).to(exchange).with(ROUTING_TRANSACCION_KEY_NAME));

        return amqpAdmin;
    }


    @Bean
    public ConnectionFactory connectionFactory(URI uri) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();
        connectionFactory.setUri(uri);
        return connectionFactory;
    }

    @Bean
    public Mono<Connection> connectionMono(@Value("spring.application.name") String name, ConnectionFactory connectionFactory)  {
        return Mono.fromCallable(() -> connectionFactory.newConnection(name)).cache();
    }

    @Bean
    public SenderOptions senderOptions(Mono<Connection> connectionMono) {
        return new SenderOptions()
                .connectionMono(connectionMono)
                .resourceManagementScheduler(Schedulers.boundedElastic());
    }

    @Bean
    public Sender sender(SenderOptions senderOptions) {
        return RabbitFlux.createSender(senderOptions);
    }


    @Bean
    public ReceiverOptions receiverOptions(Mono<Connection> connectionMono) {
        return new ReceiverOptions()
                .connectionMono(connectionMono);
    }

    @Bean
    public Receiver receiver(ReceiverOptions receiverOptions) {
        return RabbitFlux.createReceiver(receiverOptions);
    }
}
