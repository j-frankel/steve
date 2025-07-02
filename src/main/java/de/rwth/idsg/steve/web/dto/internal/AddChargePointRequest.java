package de.rwth.idsg.steve.web.dto.internal;

import java.math.BigDecimal;

public class AddChargePointRequest {
    
    private String chargeBoxId; // required

    // optional- default = null
    private Boolean insertConnectorStatusAfterTransactionMsg;
    private String  registrationStatus;
    private String  street;
    private String  houseNumber;
    private String  zipCode;
    private String  city;
    private String  country;
    private String  description;
    private String  adminAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String  note;

    public String getChargeBoxId() { return chargeBoxId; }
    public void setChargeBoxId(String chargeBoxId){ this.chargeBoxId = chargeBoxId; }

    public Boolean getInsertConnectorStatusAfterTransactionMsg() {
        return insertConnectorStatusAfterTransactionMsg;
    }
    public void setInsertConnectorStatusAfterTransactionMsg(Boolean v){
        this.insertConnectorStatusAfterTransactionMsg = v;
    }

    public String getRegistrationStatus()   { return registrationStatus; }
    public void   setRegistrationStatus(String s){ this.registrationStatus = s; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAdminAddress() { return adminAddress; }
    public void setAdminAddress(String adminAddress) { this.adminAddress = adminAddress; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
