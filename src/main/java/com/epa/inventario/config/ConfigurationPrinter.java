package com.epa.inventario.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationPrinter {

//    public ConfigurationPrinter(
//            @Value("${spring.data.mongodb.uri}") String mongoUri,
//            @Value("${rabbit.uri}") String rabbitUri
//    ) {
//        Logger logger = LoggerFactory.getLogger(ConfigurationPrinter.class);
//        logger.info("*** CONFIGURACION ***");
//        logger.info("Mongo URI: " + mongoUri);
//        logger.info("RabbitMQ URI: " + rabbitUri);
//        logger.info("*** CONFIGURACION ***");
//    }
}