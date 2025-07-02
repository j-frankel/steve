package de.rwth.idsg.steve.service.internal;

import de.rwth.idsg.steve.repository.ChargePointRepository;
import de.rwth.idsg.steve.web.api.exception.BadRequestException;
import de.rwth.idsg.steve.web.dto.Address;
import de.rwth.idsg.steve.web.dto.ChargePointForm;
import de.rwth.idsg.steve.web.dto.internal.AddChargePointRequest;
import de.rwth.idsg.steve.web.dto.internal.AddChargePointResponse;
import org.springframework.stereotype.Service;

import com.neovisionaries.i18n.CountryCode;

import jakarta.transaction.Transactional;

@Service
public class AddChargePointService {

    private final ChargePointRepository repo;

    public AddChargePointService(ChargePointRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public AddChargePointResponse add(AddChargePointRequest in) {

        ChargePointForm form = new ChargePointForm();
        form.setChargeBoxId(in.getChargeBoxId());

        // defaults
        form.setRegistrationStatus(
                in.getRegistrationStatus() != null ? in.getRegistrationStatus()
                                                   : "Accepted");
        form.setInsertConnectorStatusAfterTransactionMsg(
                in.getInsertConnectorStatusAfterTransactionMsg() != null
                        ? in.getInsertConnectorStatusAfterTransactionMsg()
                        : Boolean.FALSE);

        form.setDescription(in.getDescription());
        form.setAdminAddress(in.getAdminAddress());
        form.setLocationLatitude(in.getLatitude());
        form.setLocationLongitude(in.getLongitude());
        form.setNote(in.getNote());

        Address addr = new Address();
        form.setAddress(addr);

        boolean anyAddressFieldPresent =
                in.getStreet()      != null ||
                in.getHouseNumber() != null ||
                in.getZipCode()     != null ||
                in.getCity()        != null ||
                in.getCountry()     != null;

        if (anyAddressFieldPresent) {

            addr.setStreet(in.getStreet());
            addr.setHouseNumber(in.getHouseNumber());
            addr.setZipCode(in.getZipCode());
            addr.setCity(in.getCity());

            if (in.getCountry() != null) {
                try {
                    addr.setCountry(CountryCode.valueOf(in.getCountry()));
                } catch (IllegalArgumentException ex) {
                    throw new BadRequestException("Invalid country code: " + in.getCountry());
                }
            }
        }

        int pk = repo.addChargePoint(form);
        return new AddChargePointResponse(pk, "created");
    }
}