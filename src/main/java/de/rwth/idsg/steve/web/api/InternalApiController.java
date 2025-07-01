package de.rwth.idsg.steve.web.api;

import de.rwth.idsg.steve.service.internal.ConfigPushService;
import de.rwth.idsg.steve.web.dto.internal.AuthRequiredRequest;
import de.rwth.idsg.steve.web.dto.internal.AuthRequiredResponse;
import de.rwth.idsg.steve.web.api.exception.BadRequestException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal")
public class InternalApiController {

    private final ConfigPushService configPushService;

    public InternalApiController(ConfigPushService configPushService) {
        this.configPushService = configPushService;
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
}