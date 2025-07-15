package de.rwth.idsg.steve.web.dto.internal;

import lombok.Data;

@Data
public class ClockAlignedDataIntervalRequest {
    private String chargeBoxId;
    private int    interval;
}