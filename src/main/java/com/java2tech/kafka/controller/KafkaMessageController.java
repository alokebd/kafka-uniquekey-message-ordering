package com.java2tech.kafka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java2tech.kafka.service.KafkaMessagingService;

@RestController
@RequestMapping("/kafka")
public class KafkaMessageController {

	private KafkaMessagingService kafkaUniqueMessagingService;

	public KafkaMessageController(KafkaMessagingService service) {
		kafkaUniqueMessagingService = service;
	}

	@PostMapping("/v1/messages/{totalMessages}")
	public ResponseEntity<?> publish(@PathVariable int totalMessages) {
		for (int i = 0; i < totalMessages; i++) {
			kafkaUniqueMessagingService.sendAsynchronousMessage();
		}
		return ResponseEntity.ok("Successfully published "+ totalMessages + " messages,");
	}
	
	@PostMapping("/v2/messages/{totalMessages}")
	public ResponseEntity<?> publishMessageConsumeMetadata(@PathVariable int totalMessages) {
		for (int i = 0; i < totalMessages; i++) {
			kafkaUniqueMessagingService.sendAsynchronousMessageAndConsumedMetadata();
		}
		return ResponseEntity.ok("Successfully published "+ totalMessages + " messages,");
	}

}
