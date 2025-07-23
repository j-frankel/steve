package de.rwth.idsg.steve.service.internal;

import de.rwth.idsg.steve.service.ChargePointServiceClient;
import de.rwth.idsg.steve.web.dto.ocpp.ChangeConfigurationParams;
import de.rwth.idsg.steve.ocpp.OcppCallback;
import de.rwth.idsg.steve.ocpp.OcppProtocol;
import de.rwth.idsg.steve.repository.dto.ChargePointSelect;
import de.rwth.idsg.steve.web.dto.internal.AuthRequiredResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.stereotype.Service;
import de.rwth.idsg.steve.ocpp.ws.data.OcppJsonError;
import java.util.concurrent.ExecutionException;

@Service
public class ConfigPushService {

    private final ChargePointServiceClient client;

    public ConfigPushService(ChargePointServiceClient client) {
        this.client = client;
    }

    public AuthRequiredResponse pushAuthorizationRequired(String chargeBoxId, boolean enabled)
            throws InterruptedException, TimeoutException {

        // build params object
        ChangeConfigurationParams params = new ChangeConfigurationParams();
        params.setConfKey("AuthorizationRequired");
        params.setValue(Boolean.toString(enabled));

        // ocpp v1.6 json
        ChargePointSelect cps = new ChargePointSelect(OcppProtocol.V_16_JSON, chargeBoxId);
        params.setChargePointSelectList(List.of(cps));

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        OcppCallback<String> cb = new OcppCallback<>() {

            @Override
            public void success(String chargeBoxId, String status) {
                future.complete("Accepted".equalsIgnoreCase(status));
            }

            @Override
            public void success(String chargeBoxId, OcppJsonError error) {
                future.complete(false);                // any ocpp error is reject
            }

            @Override
            public void failed(String chargeBoxId, Exception e) {
                future.completeExceptionally(e);
            }
        };

        int taskId = client.changeConfiguration(params, cb);

        boolean accepted;
        try {
            accepted = future.get(30, TimeUnit.SECONDS);
        } catch (ExecutionException e) {               // unwrap transport error
            throw new TimeoutException(e.getCause().getMessage());
        }

        AuthRequiredResponse resp = new AuthRequiredResponse();
        resp.setAccepted(accepted);
        resp.setTaskId(taskId);
        resp.setMessage(accepted ? "Accepted by charge point"
                                 : "Rejected by charge point or timed-out");
        return resp;
    }

    public AuthRequiredResponse pushClockAlignedDataInterval(String chargeBoxId,
                                                             int intervalSeconds)
            throws InterruptedException, TimeoutException {

        ChangeConfigurationParams params = new ChangeConfigurationParams();
        params.setConfKey("ClockAlignedDataInterval");
        params.setValue(Integer.toString(intervalSeconds));
        params.setChargePointSelectList(List.of(
                new ChargePointSelect(OcppProtocol.V_16_JSON, chargeBoxId)));

        return execute(params);
    }

    public AuthRequiredResponse pushMeterValuesSampledData(String chargeBoxId,
                                                           String measurands)
            throws InterruptedException, TimeoutException {

        ChangeConfigurationParams params = new ChangeConfigurationParams();
        params.setConfKey("MeterValuesSampledData");
        params.setValue(measurands);
        params.setChargePointSelectList(List.of(
                new ChargePointSelect(OcppProtocol.V_16_JSON, chargeBoxId)));

        return execute(params);
    }

    private AuthRequiredResponse execute(ChangeConfigurationParams params)
            throws InterruptedException, TimeoutException {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        OcppCallback<String> cb = new OcppCallback<>() {
            @Override public void success(String id, String status)   {
                future.complete("Accepted".equalsIgnoreCase(status));
            }
            @Override public void success(String id, OcppJsonError e) {
                future.complete(false);
            }
            @Override public void failed (String id, Exception ex)    {
                future.completeExceptionally(ex);
            }
        };

        int taskId = client.changeConfiguration(params, cb);

        boolean accepted;
        try {
            accepted = future.get(30, TimeUnit.SECONDS);
        } catch (ExecutionException ex) {
            throw new TimeoutException(ex.getCause().getMessage());
        }

        AuthRequiredResponse resp = new AuthRequiredResponse();
        resp.setAccepted(accepted);
        resp.setTaskId(taskId);
        resp.setMessage(accepted
                        ? "Accepted by charge point"
                        : "Rejected by charge point or timed-out");
        return resp;
    }
}