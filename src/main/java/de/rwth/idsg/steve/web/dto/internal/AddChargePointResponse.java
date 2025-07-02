package de.rwth.idsg.steve.web.dto.internal;

public class AddChargePointResponse {
    private int     chargeBoxPk;
    private String  message;

    public AddChargePointResponse(int chargeBoxPk, String message) {
        this.chargeBoxPk    = chargeBoxPk;
        this.message        = message;
    }
    public int getChargeBoxPk() { return chargeBoxPk; }
    public String getMessage()  { return message; }
}
