package com.example.kafkapaymentservice.service;

import com.example.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {

    private final KafkaProducerService kafkaProducerService;
    @Value("${kafka.producer.topic.name}")
    String kafkaProducerTopic;

    @Autowired
    public PaymentService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @KafkaListener(topics = "${kafka.consumer.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(OrderDTO orderDTO) {
        log.info("Получен заказ на оплату: {}", orderDTO.getOrderId());
        try {
            OrderDTO newOrderDTO = OrderDTO.builder()
                    .orderId(orderDTO.getOrderId())
                    .orderStatus(orderDTO.getOrderStatus())
                    .productName(orderDTO.getProductName())
                    .paymentStatus("PAID")
                    .shippingStatus("NOT_ASSIGNED")
                    .build();
            log.info("Заказ успешно оплачен: {}", newOrderDTO.getOrderId());
            kafkaProducerService.sendMessage(kafkaProducerTopic, newOrderDTO);
        } catch (Exception e) {
            log.error("Ошибка оплаты заказа № {}: {}", orderDTO.getOrderId(), e.getMessage());
        }
    }
}
