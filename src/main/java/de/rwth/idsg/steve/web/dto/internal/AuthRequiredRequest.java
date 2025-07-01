package de.rwth.idsg.steve.web.dto.internal;

public class AuthRequiredRequest {

    private String chargeBoxId;
    private boolean enabled;

    public String getChargeBoxId() {
        return chargeBoxId;
    }
    public void setChargeBoxId(String chargeBoxId) {
        this.chargeBoxId = chargeBoxId;
    }

    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}