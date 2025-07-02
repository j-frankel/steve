package de.rwth.idsg.steve.service.internal;

import de.rwth.idsg.steve.service.ChargePointHelperService;
import de.rwth.idsg.steve.web.dto.ConnectorStatusForm;
import de.rwth.idsg.steve.web.dto.internal.ChargeStateResponse;
import de.rwth.idsg.steve.repository.dto.ConnectorStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChargeStateService {

    private final ChargePointHelperService helper;

    public ChargeStateService(ChargePointHelperService helper) {
        this.helper = helper;
    }

    public ChargeStateResponse getCurrentState(String chargeBoxId) {

        ConnectorStatusForm form = new ConnectorStatusForm();
        form.setChargeBoxId(chargeBoxId);

        List<ConnectorStatus> list =
                helper.getChargePointConnectorStatus(form);

        if (list.isEmpty()) {
            return new ChargeStateResponse(chargeBoxId, "UNKNOWN");
        }
        // only connector 1 for now (AC)
        String state = list.get(0).getStatus();
        return new ChargeStateResponse(chargeBoxId, state);
    }
}