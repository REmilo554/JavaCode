package com.example.kafkashippingservice.service;


import com.example.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShippingService {

    private final KafkaProducerService kafkaProducerService;
    @Value("${kafka.producer.topic.name}")
    String kafkaProducerTopic;

    @Autowired
    public ShippingService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @KafkaListener(topics = "${kafka.consumer.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(OrderDTO order) {
        log.info("Получен новый заказ: {}", order);
        try{
            OrderDTO orderDTO = OrderDTO.builder()
                    .orderId(order.getOrderId())
                    .orderStatus(order.getOrderStatus())
                    .productName(order.getProductName())
                    .paymentStatus(order.getPaymentStatus())
                    .shippingStatus("DELIVERY_SCHEDULED")
                    .build();
            log.info("Доставка успешно назначена для заказа: {}", orderDTO.getOrderId());
            kafkaProducerService.sendMessage(kafkaProducerTopic, orderDTO);
        } catch (Exception e) {
            log.error("Ошибка назначения доставки для заказа № {}: {}",order.getOrderId(), e.getMessage());
        }
    }
}
