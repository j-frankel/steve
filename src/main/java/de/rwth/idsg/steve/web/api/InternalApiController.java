package de.rwth.idsg.steve.web.api;

import de.rwth.idsg.steve.service.internal.ChargeStateService;
import de.rwth.idsg.steve.service.internal.ConfigPushService;
import de.rwth.idsg.steve.web.dto.internal.AuthRequiredRequest;
import de.rwth.idsg.steve.web.dto.internal.AuthRequiredResponse;
import de.rwth.idsg.steve.web.dto.internal.ChargeStateResponse;
import de.rwth.idsg.steve.web.api.exception.BadRequestException;
import org.springframework.web.bind.annotation.*;
import de.rwth.idsg.steve.service.internal.LastStatusService;
import de.rwth.idsg.steve.web.dto.internal.LastStatusResponse;
import de.rwth.idsg.steve.service.internal.AddChargePointService;
import de.rwth.idsg.steve.web.dto.internal.AddChargePointRequest;
import de.rwth.idsg.steve.web.dto.internal.AddChargePointResponse;
import de.rwth.idsg.steve.service.internal.DeleteChargePointService;
import de.rwth.idsg.steve.web.dto.internal.DeleteChargePointResponse;
import java.util.concurrent.TimeoutException;
import de.rwth.idsg.steve.web.dto.internal.ClockAlignedDataIntervalRequest;
import de.rwth.idsg.steve.web.dto.internal.ClockAlignedDataIntervalResponse;
import de.rwth.idsg.steve.web.dto.internal.MeterValuesSampledDataRequest;
import de.rwth.idsg.steve.web.dto.internal.MeterValuesSampledDataResponse;

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
    public ClockAlignedDataIntervalResponse pushClockAlignedDataInterval(
            @RequestBody ClockAlignedDataIntervalRequest body)
            throws InterruptedException, TimeoutException {

        if (body.getChargeBoxId() == null || body.getChargeBoxId().isBlank()) {
            throw new BadRequestException("chargeBoxId must be provided");
        }

        var serviceResp = configPushService.pushClockAlignedDataInterval(
                body.getChargeBoxId(), body.getSeconds());

        ClockAlignedDataIntervalResponse resp = new ClockAlignedDataIntervalResponse();
        resp.setAccepted(serviceResp.isAccepted());
        resp.setTaskId(serviceResp.getTaskId());
        resp.setMessage(serviceResp.getMessage());
        return resp;
    }

    @PutMapping("/meter-values-sampled-data")
    public MeterValuesSampledDataResponse pushMeterValuesSampledData(
            @RequestBody MeterValuesSampledDataRequest body)
            throws InterruptedException, TimeoutException {

        if (body.getChargeBoxId() == null || body.getChargeBoxId().isBlank()) {
            throw new BadRequestException("chargeBoxId must be provided");
        }

        var serviceResp = configPushService.pushMeterValuesSampledData(
                body.getChargeBoxId(), body.getMeasurands());

        MeterValuesSampledDataResponse resp = new MeterValuesSampledDataResponse();
        resp.setAccepted(serviceResp.isAccepted());
        resp.setTaskId(serviceResp.getTaskId());
        resp.setMessage(serviceResp.getMessage());
        return resp;
    }
}