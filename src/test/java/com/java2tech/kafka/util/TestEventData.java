package com.java2tech.kafka.util;

import com.java2tech.kafka.event.InboundKey;
import com.java2tech.kafka.event.InboundPayload;
import com.java2tech.kafka.event.OutboundKey;
import com.java2tech.kafka.event.OutboundPayload;

public class TestEventData {

    public static String INBOUND_DATA = "inbound event data";
    public static String OUTBOUND_DATA = "outbound event data";

    public static InboundKey buildDemoInboundKey(Integer id) {
        return InboundKey.builder()
                .id(id)
                .build();
    }

    public static InboundPayload buildDemoInboundPayload(Integer sequenceNumber) {
        return InboundPayload.builder()
                .inboundData(INBOUND_DATA)
                .sequenceNumber(sequenceNumber)
                .build();
    }

    public static OutboundKey buildDemoOutboundKey(Integer id) {
        return OutboundKey.builder()
                .id(id)
                .build();
    }

    public static OutboundPayload buildDemoOutboundPayload(Integer sequenceNumber) {
        return OutboundPayload.builder()
                .outboundData(OUTBOUND_DATA)
                .sequenceNumber(sequenceNumber)
                .build();
    }
}
