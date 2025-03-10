package com.example.kafkaordersservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.producer.topic.name}")
    private String topicName;
    @Value("${kafka.partition.count}")
    private int partitions;
    @Value("${kafka.replicas.count}")
    private int replicas;

    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }

    @Bean
    public NewTopic dlqTopic() {
        return TopicBuilder.name(topicName + "_dlq")
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
