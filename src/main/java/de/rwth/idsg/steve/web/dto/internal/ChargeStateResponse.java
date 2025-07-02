package de.rwth.idsg.steve.web.dto.internal;

public class ChargeStateResponse {

    private String chargeBoxId;
    private String status;

    public ChargeStateResponse(String chargeBoxId, String status) {
        this.chargeBoxId = chargeBoxId;
        this.status = status;
    }
    public String getChargeBoxId() { return chargeBoxId; }
    public String getStatus()      { return status; }
}