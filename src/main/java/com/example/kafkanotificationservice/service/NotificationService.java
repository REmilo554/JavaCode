package com.example.kafkanotificationservice.service;

import com.example.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    private final KafkaProducerService kafkaProducerService;
    @Value("${kafka.producer.topic.name}")
    String kafkaProducerTopic;

    @Autowired
    public NotificationService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @KafkaListener(topics = "${kafka.consumer.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(OrderDTO order) {
        log.info("Заказ полностью и успешно обработан: {}", order);
        try{
            OrderDTO orderDTO = OrderDTO.builder()
                    .orderId(order.getOrderId())
                    .orderStatus("COMPLETED")
                    .productName(order.getProductName())
                    .paymentStatus(order.getPaymentStatus())
                    .shippingStatus("DELIVERED")
                    .build();
            kafkaProducerService.sendMessage(kafkaProducerTopic, orderDTO);
        } catch (Exception e) {
            log.error("При отправке уведомления о заказе {} произошла ошибка: {}",order.getOrderId(),e.getMessage());
        }
    }

}
