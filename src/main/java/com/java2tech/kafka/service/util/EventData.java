package com.java2tech.kafka.service.util;

import java.util.Random;

import com.java2tech.kafka.event.InboundKey;
import com.java2tech.kafka.event.InboundPayload;
import com.java2tech.kafka.event.OutboundKey;
import com.java2tech.kafka.event.OutboundPayload;

public class EventData {
	public static String INBOUND_DATA = "inbound event data";
    public static String OUTBOUND_DATA = "outbound event data";
    
	public final static String INBOUND_TOPIC = "demo-inbound-topic";
	public final static String OUTBOUND_TOPIC = "demo-outbound-topic";
	
	
	public final static String OUTBOUND_TOPIC_GROUP_ID = "demo-consumer-group";
	
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
    
    
    public static int uniqueId() {
    	 Random rand = new Random();
    	 int max=10000000,min=999;
    	 return rand.nextInt(max - min + 1) + min;
    }
}
