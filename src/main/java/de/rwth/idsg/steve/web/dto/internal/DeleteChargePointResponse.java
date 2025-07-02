package de.rwth.idsg.steve.web.dto.internal;

public class DeleteChargePointResponse {

    private String chargeBoxId;
    private String message;

    public DeleteChargePointResponse(String chargeBoxId, String message) {
        this.chargeBoxId = chargeBoxId;
        this.message     = message;
    }
    public String getChargeBoxId() { return chargeBoxId; }
    public String getMessage()     { return message; }
}