package com.example.kafkaordersservice.Service;

import com.example.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableRetry
public class KafkaProducerService {
    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;
    @Value("${kafka.consumer.topic.name:#{null}}")
    private String consumerTopicName;
    @Value("${kafka.producer.topic.name}")
    private String producerTopicName;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, OrderDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void sendMessage(String topic, OrderDTO orderDTO) {
        kafkaTemplate.send(topic,String.valueOf(orderDTO.getOrderId()), orderDTO)
                .thenAccept(result -> log.info("Сообщение отправлено: topic={}, offset={}, partition={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition()))
                .exceptionally(throwable -> {
                    throw new RuntimeException(throwable);
                });
    }

    //???? уточнить
    @Recover
    public void recover(RuntimeException e, String topic, OrderDTO orderDTO) {
        log.error("Не удалось отправить сообщение после всех попыток: {}", e.getMessage());
        sendToDlq(orderDTO);
    }

    private void sendToDlq(OrderDTO orderDTO) {
        String dlqTopic = (consumerTopicName != null ? consumerTopicName : producerTopicName) + "_dlq";
        kafkaTemplate.send(dlqTopic, orderDTO);
        log.info("Сообщение отправлено в DLQ: {}", dlqTopic);
    }
}
