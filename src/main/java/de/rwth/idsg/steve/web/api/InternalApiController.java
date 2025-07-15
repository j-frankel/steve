package de.rwth.idsg.steve.web.api;

import de.rwth.idsg.steve.service.internal.ChargeStateService;
import de.rwth.idsg.steve.service.internal.ConfigPushService;
import de.rwth.idsg.steve.web.dto.internal.AuthRequiredRequest;
import de.rwth.idsg.steve.web.dto.internal.AuthRequiredResponse;
import de.rwth.idsg.steve.web.dto.internal.ChargeStateResponse;
import de.rwth.idsg.steve.web.api.exception.BadRequestException;

import java.util.concurrent.TimeoutException;

import org.springframework.web.bind.annotation.*;
import de.rwth.idsg.steve.service.internal.LastStatusService;
import de.rwth.idsg.steve.web.dto.internal.LastStatusResponse;
import de.rwth.idsg.steve.service.internal.AddChargePointService;
import de.rwth.idsg.steve.web.dto.internal.AddChargePointRequest;
import de.rwth.idsg.steve.web.dto.internal.AddChargePointResponse;
import de.rwth.idsg.steve.service.internal.DeleteChargePointService;
import de.rwth.idsg.steve.web.dto.internal.DeleteChargePointResponse;
import de.rwth.idsg.steve.web.dto.internal.ClockAlignedDataIntervalRequest;
import de.rwth.idsg.steve.web.dto.internal.MeterValuesSampledDataRequest;

@RestController
@RequestMapping("/api/v1/internal")
public class InternalApiController {

    private final ConfigPushService configPushService;
    private final ChargeStateService chargeStateService;
    private final LastStatusService  lastStatusService;
    private final AddChargePointService addCpService;
    private final DeleteChargePointService deleteCpService;

    public InternalApiController(ConfigPushService  configPushService,
                                ChargeStateService chargeStateService,
                                LastStatusService  lastStatusService,
                                AddChargePointService addCpService,
                                DeleteChargePointService deleteCpService) {
        this.configPushService = configPushService;
        this.chargeStateService = chargeStateService;
        this.lastStatusService  = lastStatusService;
        this.addCpService       = addCpService;
        this.deleteCpService    = deleteCpService;
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

    @GetMapping("/last-status")
    public LastStatusResponse getLastStatus(
            @RequestParam(value = "chargeBoxId", required = false) String chargeBoxId) {

        if (chargeBoxId == null || chargeBoxId.isBlank()) {
            throw new BadRequestException("chargeBoxId must be provided");
        }
        return lastStatusService.getLastStatus(chargeBoxId);
    }

    @PostMapping("/charge-point")
    public AddChargePointResponse addChargePoint(
            @RequestBody AddChargePointRequest body) {

        if (body.getChargeBoxId() == null || body.getChargeBoxId().isBlank()) {
            throw new BadRequestException("chargeBoxId must be provided");
        }
        return addCpService.add(body);
    }

    @DeleteMapping("/charge-point")
    public DeleteChargePointResponse deleteChargePoint(
            @RequestParam(value = "chargeBoxId", required = false) String chargeBoxId) {

        if (chargeBoxId == null || chargeBoxId.isBlank()) {
            throw new BadRequestException("chargeBoxId must be provided");
        }
        return deleteCpService.delete(chargeBoxId);
    }

    @PutMapping("/clock-aligned-data-interval")
    public AuthRequiredResponse pushClockAlignedDataInterval(
            @RequestBody ClockAlignedDataIntervalRequest body)
            throws InterruptedException, TimeoutException {

        if (body.getChargeBoxId() == null || body.getChargeBoxId().isBlank()) {
            throw new BadRequestException("chargeBoxId must be provided");
        }
        return configPushService.pushClockAlignedDataInterval(
                body.getChargeBoxId(), body.getInterval());
    }

    @PutMapping("/meter-values-sampled-data")
    public AuthRequiredResponse pushMeterValuesSampledData(
            @RequestBody MeterValuesSampledDataRequest body)
            throws InterruptedException, TimeoutException {

        if (body.getChargeBoxId() == null || body.getChargeBoxId().isBlank()) {
            throw new BadRequestException("chargeBoxId must be provided");
        }
        return configPushService.pushMeterValuesSampledData(
                body.getChargeBoxId(), body.getMeasurands());
    }
}