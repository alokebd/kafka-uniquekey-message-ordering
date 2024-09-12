package com.java2tech.kafka.service;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.java2tech.kafka.event.InboundKey;
import com.java2tech.kafka.event.InboundPayload;
import com.java2tech.kafka.event.OutboundKey;
import com.java2tech.kafka.event.OutboundPayload;
import com.java2tech.kafka.producer.KafkaProducer;
import com.java2tech.kafka.service.KafkaMessagingService;
import com.java2tech.kafka.util.TestEventData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KafkaMessagingServiceTest {

    private KafkaProducer mockKafkaDemoProducer;
    private KafkaMessagingService service;

    @BeforeEach
    public void setUp() {
        mockKafkaDemoProducer = mock(KafkaProducer.class);
        service = new KafkaMessagingService(mockKafkaDemoProducer);
    }

    /**
     * Ensure the Kafka producer is called to emit a message.
     */
    @Test
    public void testProcess() {
        InboundKey testKey = TestEventData.buildDemoInboundKey(RandomUtils.nextInt(1, 6));
        InboundPayload testPayload = TestEventData.buildDemoInboundPayload(1);

        service.process(testKey, testPayload);

        verify(mockKafkaDemoProducer, times(1)).sendMessage(any(OutboundKey.class), any(OutboundPayload.class));
    }
}
