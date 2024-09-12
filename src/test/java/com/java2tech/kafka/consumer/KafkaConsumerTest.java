package com.java2tech.kafka.consumer;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.java2tech.kafka.consumer.KafkaConsumer;
import com.java2tech.kafka.event.InboundKey;
import com.java2tech.kafka.event.InboundPayload;
import com.java2tech.kafka.service.KafkaMessagingService;
import com.java2tech.kafka.util.TestEventData;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KafkaConsumerTest {

    private KafkaMessagingService serviceMock;
    private KafkaConsumer consumer;

    @BeforeEach
    public void setUp() {
        serviceMock = mock(KafkaMessagingService.class);
        consumer = new KafkaConsumer(serviceMock);
    }

    /**
     * Ensure that the JSON message is successfully passed on to the service.
     */
    @Test
    public void testListen_Success() {
        InboundKey testKey = TestEventData.buildDemoInboundKey(RandomUtils.nextInt(1, 6));
        InboundPayload testPayload = TestEventData.buildDemoInboundPayload(1);

        consumer.listen(0, testKey, testPayload);

        verify(serviceMock, times(1)).process(testKey, testPayload);
    }

    /**
     * If an exception is thrown, an error is logged but the processing completes successfully.
     *
     * This ensures the consumer offsets are updated so that the message is not redelivered.
     */
    @Test
    public void testListen_ServiceThrowsException() {
        InboundKey testKey = TestEventData.buildDemoInboundKey(RandomUtils.nextInt(1, 6));
        InboundPayload testPayload = TestEventData.buildDemoInboundPayload(1);

        doThrow(new RuntimeException("Service failure")).when(serviceMock).process(testKey, testPayload);

        consumer.listen(0, testKey, testPayload);

        verify(serviceMock, times(1)).process(testKey, testPayload);
    }
}
