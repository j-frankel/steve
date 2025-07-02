package de.rwth.idsg.steve.web.api;

import de.rwth.idsg.steve.service.internal.ChargeStateService;
import de.rwth.idsg.steve.service.internal.ConfigPushService;
import de.rwth.idsg.steve.web.dto.internal.AuthRequiredRequest;
import de.rwth.idsg.steve.web.dto.internal.AuthRequiredResponse;
import de.rwth.idsg.steve.web.dto.internal.ChargeStateResponse;
import de.rwth.idsg.steve.web.api.exception.BadRequestException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal")
public class InternalApiController {

    private final ConfigPushService configPushService;
    private final ChargeStateService chargeStateService;

    public InternalApiController(ConfigPushService cfgPush,
                                 ChargeStateService chargeStateService) {
        this.configPushService = cfgPush;
        this.chargeStateService = chargeStateService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }

    @PutMapping("/config/authorization-required")
    public AuthRequiredResponse toggle(@RequestBody AuthRequiredRequest dto) {

        if (dto.getChargeBoxId() == null || dto.getChargeBoxId().isBlank()) {
            throw new BadRequestException("chargeBoxId must be provided");
        }

        try {
            return configPushService.pushAuthorizationRequired(
                    dto.getChargeBoxId(),
                    dto.isEnabled());
        } catch (Exception e) {
            throw new BadRequestException("OCPP call failed: " + e.getMessage());
        }
    }

    @GetMapping("/charge-state")
    public ChargeStateResponse getChargeState(@RequestParam(value = "chargeBoxId", required = false)
                                            String chargeBoxId) {

        if (chargeBoxId == null || chargeBoxId.isBlank()) {
            throw new BadRequestException("chargeBoxId must be provided");
        }

        return chargeStateService.getCurrentState(chargeBoxId);
    }
}