package com.epa.inventario.drivenAdapters.bus;
import com.epa.inventario.config.RabbitConfig;
import com.epa.inventario.models.dto.ErrorDto;
import com.epa.inventario.models.dto.LogDto;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

@Component
public class RabbitMqPublisher {

    @Autowired
    private Sender sender;

    @Autowired
    private Gson gson;

    public void publishTransaccion(LogDto data){

        sender
                .send(Mono.just(new OutboundMessage(RabbitConfig.EXCHANGE_NAME,
                        RabbitConfig.ROUTING_TRANSACCION_KEY_NAME, gson.toJson(data).getBytes()))).subscribe();
    }

    public void publishError(ErrorDto data){

        sender
                .send(Mono.just(new OutboundMessage(RabbitConfig.EXCHANGE_NAME,
                        RabbitConfig.ROUTING_ERROR_KEY_NAME, gson.toJson(data).getBytes()))).subscribe();
    }


}
