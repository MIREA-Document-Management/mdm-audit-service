package ru.mdm.audit.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import ru.mdm.kafka.model.MdmKafkaMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationPropertiesScan
public class AuditKafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public AuditKafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Конфигурация консьюмера кафки.
     */
    @Bean
    public ConsumerFactory<String, MdmKafkaMessage<Object>> auditConsumerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MdmKafkaMessageDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Конфигурация консьюмера кафки.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MdmKafkaMessage<Object>>> auditKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MdmKafkaMessage<Object>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(auditConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public CommonErrorHandler errorHandler() {
        return new DefaultErrorHandler();
    }
}
