package de.rwth.idsg.steve.service.internal;

import de.rwth.idsg.steve.service.ChargePointHelperService;
import de.rwth.idsg.steve.web.dto.ConnectorStatusForm;
import de.rwth.idsg.steve.web.dto.internal.LastStatusResponse;
import de.rwth.idsg.steve.repository.dto.ConnectorStatus;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
public class LastStatusService {

    private final ChargePointHelperService helper;

    public LastStatusService(ChargePointHelperService helper) {
        this.helper = helper;
    }

    public LastStatusResponse getLastStatus(String chargeBoxId) {

        ConnectorStatusForm form = new ConnectorStatusForm();
        form.setChargeBoxId(chargeBoxId);

        List<ConnectorStatus> list = helper.getChargePointConnectorStatus(form);

        if (list.isEmpty()) {
            return new LastStatusResponse(chargeBoxId, "UNKNOWN", "", "");
        }

        // newest by statusTimestamp
        ConnectorStatus latest = list.stream()
                                     .max(Comparator.comparing(ConnectorStatus::getStatusTimestamp))
                                     .orElse(list.get(0));

        return new LastStatusResponse(
                chargeBoxId,
                latest.getStatus(),
                latest.getErrorCode(),
                latest.getStatusTimestamp().toString()
        );
    }
}