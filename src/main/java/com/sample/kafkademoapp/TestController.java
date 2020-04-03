package com.sample.kafkademoapp;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@RestController
public class TestController {

    private final KafkaTemplate<String, Object> template;
    private final String topicName;
    private CountDownLatch latch;

    public TestController(final KafkaTemplate<String, Object> template,
                          @Value("${app.kafka.topic-name}") final String topicName) {
        this.template = template;
        this.topicName = topicName;
    }

    @GetMapping("/hello")
    public String hello() throws Exception {
        int count = 10;
        latch = new CountDownLatch(count);
        IntStream.range(0, count)
                .forEach(i -> this.template.send(topicName, String.valueOf(i),
                        new Message(i, "Test message"))
                );
        latch.await(60, TimeUnit.SECONDS);

        log.info("All messages received");

        return "Hello world!";
    }

    @KafkaListener(topics = "sample-topic", clientIdPrefix = "json",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObject(ConsumerRecord<String, Message> cr, @Payload Message payload) {

        log.info("Record id: {}, {}", payload.getId(), payload.getContent());
        latch.countDown();
    }
}
