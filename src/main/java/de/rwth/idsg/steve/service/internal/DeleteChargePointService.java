package de.rwth.idsg.steve.service.internal;

import de.rwth.idsg.steve.repository.ChargePointRepository;
import de.rwth.idsg.steve.web.dto.internal.DeleteChargePointResponse;
import de.rwth.idsg.steve.web.api.exception.BadRequestException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.Map;

@Service
public class DeleteChargePointService {

    private final ChargePointRepository repo;

    public DeleteChargePointService(ChargePointRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public DeleteChargePointResponse delete(String chargeBoxId) {

        Map<String,Integer> map =
                repo.getChargeBoxIdPkPair(Collections.singletonList(chargeBoxId));

        Integer pk = map.get(chargeBoxId);
        if (pk == null) {
            throw new BadRequestException("ChargeBoxId not found: " + chargeBoxId);
        }

        repo.deleteChargePoint(pk);
        return new DeleteChargePointResponse(chargeBoxId, "deleted");
    }
}