package com.example.kafkaordersservice.Service;

import com.example.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class OrderService {

    private final KafkaProducerService kafkaProducerService;

    @Value("${kafka.producer.topic.name}")
    String producerTopic;
    @Autowired
    public OrderService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public String createNewOrder(OrderDTO orderDTO) {
        log.info("Создан новый заказ на продукт {}", orderDTO.getProductName());
        OrderDTO newOrderDTO = OrderDTO.builder()
                .orderId(UUID.randomUUID())
                .orderStatus("CREATED")
                .productName(orderDTO.getProductName())
                .paymentStatus("NOT_PAID")
                .shippingStatus("NOT_ASSIGNED")
                .build();
        kafkaProducerService.sendMessage(producerTopic,newOrderDTO);
        return "Order created";
    }
}
