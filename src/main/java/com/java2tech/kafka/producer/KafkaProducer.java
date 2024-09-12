package com.java2tech.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import com.java2tech.kafka.event.OutboundKey;
import com.java2tech.kafka.event.OutboundPayload;
import com.java2tech.kafka.properties.KafkaProperties;
import com.java2tech.kafka.service.util.EventData;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {
    @Autowired
    private final KafkaProperties properties;

    @Autowired
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * Sends events asynchronously.
     */
    public void sendMessage(OutboundKey key, OutboundPayload event) {
        try {
            kafkaTemplate.send(properties.getOutboundTopic(), key, event);
            log.info("Emitted message - key: " + key + " - payload: " + event.getOutboundData());
        } catch (Exception e) {
            String message = "Error sending message to topic " + properties.getOutboundTopic();
            log.error(message);
            throw new RuntimeException(message, e);
        }
    }
    
    public void sendAsynchronousMessage(OutboundKey key, OutboundPayload event) {
    	 log.info("Emitted message - key: " + key + " - payload: " + event.getOutboundData());
    	 kafkaTemplate.send(EventData.OUTBOUND_TOPIC, key, event);
    }
    
    public SendResult<Object,Object> sendSynchronousMessage(OutboundKey key, OutboundPayload event) throws ExecutionException, InterruptedException {
    	log.info("Emitted message - key: " + key + " - payload: " + event.getOutboundData());
        SendResult<Object,Object> sendResult = kafkaTemplate.send(EventData.OUTBOUND_TOPIC, key, event).get();
        
        return sendResult;
    }
    
    
    
    
    //2. NewTopic
    @Bean
    public NewTopic messageTopic(){
        //return new NewTopic(EventData.OUTBOUND_TOPIC,3,(short) 1); //name, partition, replica
    	return TopicBuilder.name(EventData.OUTBOUND_TOPIC)
    	          .partitions(10)
    	          .replicas(3)
    	          .compact()
    	          .build();
    }

}
