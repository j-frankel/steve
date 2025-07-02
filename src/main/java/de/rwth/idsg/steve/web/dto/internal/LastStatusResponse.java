package de.rwth.idsg.steve.web.dto.internal;

public class LastStatusResponse {

    private String chargeBoxId;
    private String status;
    private String errorCode;
    private String timestamp;

    public LastStatusResponse(String chargeBoxId,
                              String status,
                              String errorCode,
                              String timestamp) {
        this.chargeBoxId = chargeBoxId;
        this.status      = status;
        this.errorCode   = errorCode;
        this.timestamp   = timestamp;
    }
    public String getChargeBoxId() { return chargeBoxId; }
    public String getStatus()      { return status; }
    public String getErrorCode()   { return errorCode; }
    public String getTimestamp()   { return timestamp; }
}