package de.rwth.idsg.steve.web.dto.internal;

public class MeterValuesSampledDataRequest {

    private String chargeBoxId;
    private String measurands;

    public String getChargeBoxId() {
        return chargeBoxId;
    }
    public void setChargeBoxId(String chargeBoxId) {
        this.chargeBoxId = chargeBoxId;
    }

    public String getMeasurands() {
        return measurands;
    }
    public void setMeasurands(String measurands) {
        this.measurands = measurands;
    }
}