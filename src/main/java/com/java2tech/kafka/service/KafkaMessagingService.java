package com.java2tech.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.java2tech.kafka.event.InboundKey;
import com.java2tech.kafka.event.InboundPayload;
import com.java2tech.kafka.event.OutboundKey;
import com.java2tech.kafka.event.OutboundPayload;
import com.java2tech.kafka.producer.KafkaProducer;
import com.java2tech.kafka.service.util.EventData;
import org.springframework.kafka.support.SendResult;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessagingService {

	@Autowired
	private final KafkaProducer kafkaProducer;

	public void process(InboundKey key, InboundPayload payload) {

		OutboundPayload outboundPayload = OutboundPayload.builder()
				.outboundData("Processed: " + payload.getInboundData()).sequenceNumber(payload.getSequenceNumber())
				.build();
		OutboundKey outboundKey = OutboundKey.builder().id(key.getId()).build();
		
		log.info("Processed data: \n:" + " key{} , payload {}", outboundKey, outboundPayload);
		//kafkaProducer.sendMessage(outboundKey, outboundPayload);
	}
	

	// @Scheduled(fixedRate = 2000)
	public void sendAsynchronousMessage() {
		// InboundKey key = EventData.buildDemoInboundKey(new Random().nextInt(1, 6));
		InboundKey key = EventData.buildDemoInboundKey(11111111);
		InboundPayload payload = EventData.buildDemoInboundPayload(101);
		log.info("Request data: \n:" + " key{} , payload {}", key, payload);
		
		OutboundPayload outboundPayload = OutboundPayload.builder()
				.outboundData("Processed: " + payload.getInboundData()).sequenceNumber(payload.getSequenceNumber())
				.build();

		OutboundKey outboundKey = OutboundKey.builder().id(key.getId()).build();

		kafkaProducer.sendAsynchronousMessage(outboundKey, outboundPayload);
	}

	// @Scheduled(fixedRate = 2000) // every 2 seconds
	public void sendAsynchronousMessageAndConsumedMetadata() {
		InboundKey key = EventData.buildDemoInboundKey(EventData.uniqueId());
		InboundPayload payload = EventData.buildDemoInboundPayload(1001);

		OutboundPayload outboundPayload = OutboundPayload.builder()
				.outboundData("Processed: " + payload.getInboundData()).sequenceNumber(payload.getSequenceNumber())
				.build();

		OutboundKey outboundKey = OutboundKey.builder().id(key.getId()).build();

		log.info("Request data: \n:" + " key{} , payload {}", outboundKey, outboundPayload);
		try {
			SendResult<Object, Object> sendResult = kafkaProducer.sendSynchronousMessage(outboundKey, outboundPayload);

			log.info("Received new metadata. \n" + "Topic: {}, Partition: {}, Offset: {}, Timestamp: {}",
					sendResult.getRecordMetadata().topic(), sendResult.getRecordMetadata().partition(),
					sendResult.getRecordMetadata().offset(), sendResult.getRecordMetadata().timestamp());
		} catch (ExecutionException | InterruptedException e) {
			log.error("Error{}", e.getMessage());
		}
	}
}
