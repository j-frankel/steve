package de.rwth.idsg.steve.web.dto.internal;

import lombok.Data;

@Data
public class MeterValuesSampledDataRequest {
    private String chargeBoxId;
    private String measurands;
}