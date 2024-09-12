package com.java2tech.kafka.producer;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import com.java2tech.kafka.event.OutboundKey;
import com.java2tech.kafka.event.OutboundPayload;
import com.java2tech.kafka.producer.KafkaProducer;
import com.java2tech.kafka.properties.KafkaProperties;
import com.java2tech.kafka.util.TestEventData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KafkaProducerTest {

    private KafkaProperties propertiesMock;
    private KafkaTemplate kafkaTemplateMock;
    private KafkaProducer kafkaDemoProducer;

    @BeforeEach
    public void setUp() {
        propertiesMock = mock(KafkaProperties.class);
        kafkaTemplateMock = mock(KafkaTemplate.class);
        kafkaDemoProducer = new KafkaProducer(propertiesMock, kafkaTemplateMock);
    }

    /**
     * Ensure the Kafka client is called to emit a message.
     */
    @Test
    public void testSendMessage_Success() {
        OutboundKey testKey = TestEventData.buildDemoOutboundKey(RandomUtils.nextInt(1, 6));
        OutboundPayload testPayload = TestEventData.buildDemoOutboundPayload(1);
        String topic = "test-outbound-topic";

        when(propertiesMock.getOutboundTopic()).thenReturn(topic);

        kafkaDemoProducer.sendMessage(testKey, testPayload);

        verify(kafkaTemplateMock, times(1)).send(topic, testKey, testPayload);
    }

    /**
     * Ensure that an exception thrown on the send is cleanly handled.
     */
    @Test
    public void testSendMessage_ExceptionOnSend() {
        OutboundKey testKey = TestEventData.buildDemoOutboundKey(RandomUtils.nextInt(1, 6));
        OutboundPayload testPayload = TestEventData.buildDemoOutboundPayload(1);
        String topic = "test-outbound-topic";

        when(propertiesMock.getOutboundTopic()).thenReturn(topic);
        doThrow(new RuntimeException("Kafka send failure")).when(kafkaTemplateMock).send(topic, testKey, testPayload);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            kafkaDemoProducer.sendMessage(testKey, testPayload);
        });

        verify(kafkaTemplateMock, times(1)).send(topic, testKey, testPayload);
        assertThat(exception.getMessage(), equalTo("Error sending message to topic " + topic));
    }
}
