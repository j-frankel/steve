package de.rwth.idsg.steve.service.internal;

import de.rwth.idsg.steve.service.ChargePointServiceClient;
import de.rwth.idsg.steve.web.dto.ocpp.ChangeConfigurationParams;
import de.rwth.idsg.steve.ocpp.OcppCallback;
import de.rwth.idsg.steve.ocpp.OcppProtocol;
import de.rwth.idsg.steve.ocpp.ws.data.OcppJsonError;
import de.rwth.idsg.steve.repository.dto.ChargePointSelect;
import de.rwth.idsg.steve.web.dto.internal.AuthRequiredResponse;
import org.springframework.stereotype.Service;
import de.rwth.idsg.steve.repository.ChargePointRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ConfigPushService {

    private final ChargePointServiceClient client;
    private final ChargePointRepository cpRepo;

    public ConfigPushService(ChargePointRepository cpRepo,
                             ChargePointServiceClient client) {
        this.cpRepo = cpRepo;
        this.client = client;
    }

    public AuthRequiredResponse pushAuthorizationRequired(String chargeBoxId, boolean enabled)
            throws InterruptedException, TimeoutException {
        return pushChangeConfiguration(chargeBoxId, "AuthorizationRequired",
                                       Boolean.toString(enabled));
    }

    public AuthRequiredResponse pushClockAlignedDataInterval(String chargeBoxId, int seconds)
            throws InterruptedException, TimeoutException {
        return pushChangeConfiguration(chargeBoxId, "ClockAlignedDataInterval",
                                       Integer.toString(seconds));
    }

    public AuthRequiredResponse pushMeterValuesSampledData(String chargeBoxId, String measurands)
            throws InterruptedException, TimeoutException {
        return pushChangeConfiguration(chargeBoxId, "MeterValuesSampledData",
                                       measurands);
    }

    private AuthRequiredResponse pushChangeConfiguration(String chargeBoxId,
                                                        String confKey,
                                                        String value)
            throws InterruptedException, TimeoutException {

        ChargePointSelect cps = null;
        for (OcppProtocol p : OcppProtocol.values()) {
            List<ChargePointSelect> list =
                    cpRepo.getChargePointSelect(p,
                                                List.of(),
                                                List.of(chargeBoxId));
            if (!list.isEmpty()) {
                cps = list.get(0);
                break;
            }
        }
        if (cps == null) {
            throw new IllegalArgumentException("Unknown chargeBoxId " + chargeBoxId);
        }

        ChangeConfigurationParams params = new ChangeConfigurationParams();
        params.setConfKey(confKey);
        params.setValue(value);
        params.setChargePointSelectList(List.of(cps));

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        OcppCallback<String> cb = new OcppCallback<>() {
            @Override public void success(String id, String status)    { future.complete("Accepted".equalsIgnoreCase(status)); }
            @Override public void success(String id, OcppJsonError e)  { future.complete(false); }
            @Override public void failed (String id, Exception ex)     { future.completeExceptionally(ex); }
        };

        int taskId = client.changeConfiguration(params, cb);

        boolean accepted;
        try {
            accepted = future.get(30, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            throw new TimeoutException(e.getCause().getMessage());
        }

        AuthRequiredResponse resp = new AuthRequiredResponse();
        resp.setAccepted(accepted);
        resp.setTaskId(taskId);
        resp.setMessage(accepted ? "Accepted by charge point"
                                : "Rejected by charge point or timed-out");
        return resp;
    }
}