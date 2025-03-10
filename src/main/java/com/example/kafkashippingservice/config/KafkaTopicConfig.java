package com.example.kafkashippingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.producer.topic.name}")
    private String producerTopicName;
    @Value("${kafka.consumer.topic.name}")
    private String consumerTopicName;
    @Value("${kafka.partition.count}")
    private int partitions;
    @Value("${kafka.replicas.count}")
    private int replicas;

    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder
                .name(producerTopicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }

    @Bean
    public NewTopic dlqTopic() {
        return TopicBuilder.name(consumerTopicName + "_dlq")
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
