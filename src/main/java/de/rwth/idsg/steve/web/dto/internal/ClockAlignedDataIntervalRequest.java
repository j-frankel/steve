package de.rwth.idsg.steve.web.dto.internal;

public class ClockAlignedDataIntervalRequest {

    private String chargeBoxId;
    private int    seconds;

    public String getChargeBoxId() {
        return chargeBoxId;
    }
    public void setChargeBoxId(String chargeBoxId) {
        this.chargeBoxId = chargeBoxId;
    }

    public int getSeconds() {
        return seconds;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}