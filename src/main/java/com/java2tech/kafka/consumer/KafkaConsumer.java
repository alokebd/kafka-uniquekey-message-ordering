package com.java2tech.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.java2tech.kafka.event.InboundKey;
import com.java2tech.kafka.event.InboundPayload;
import com.java2tech.kafka.event.OutboundKey;
import com.java2tech.kafka.event.OutboundPayload;
import com.java2tech.kafka.service.KafkaMessagingService;
import com.java2tech.kafka.service.util.EventData;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaConsumer {

	final KafkaMessagingService demoService;

	@KafkaListener(topics = EventData.INBOUND_TOPIC, groupId = "demo-consumer-group", containerFactory = "kafkaListenerContainerFactory")
	public void listen(@Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
			@Header(KafkaHeaders.RECEIVED_KEY) InboundKey key, @Payload final InboundPayload payload) {

		log.info("Received message - key id: \n" + key.getId() + " - partition: " + partition + " - payload: "
				+ payload);

		try {
			demoService.process(key, payload);

		} catch (Exception e) {
			log.error("Error processing message: " + e.getMessage());
		}
	}

	@KafkaListener(topics = EventData.OUTBOUND_TOPIC, groupId = EventData.OUTBOUND_TOPIC_GROUP_ID, containerFactory = "kafkaListenerContainerFactory")
	public void messageConsume(
			@Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
			@Header(KafkaHeaders.RECEIVED_KEY) OutboundKey key, 
			@Payload final OutboundPayload payload) {
		log.info("Consumed message  -\n" + " key id:" + key.getId() + " - partition: " + partition + " - payload: "
				+ payload);

	}

	// @KafkaListener(topics = EventData.OUTBOUND_TOPIC, groupId = "demo-consumer-group", containerFactory = "kafkaListenerContainerFactory") // works
	public void consume(ConsumerRecord<String, String> message) {
		log.info("Consumed Key: {} | Value: {}", message.key(), message.value());
		log.info("Partition: {} | Offset: {}", message.partition(), message.offset());
	}

}
