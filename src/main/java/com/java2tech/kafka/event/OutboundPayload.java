package com.java2tech.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboundPayload {

    private String outboundData;

    private Integer sequenceNumber;
}
