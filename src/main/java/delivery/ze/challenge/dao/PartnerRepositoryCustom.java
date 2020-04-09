package delivery.ze.challenge.dao;

import delivery.ze.challenge.domain.Partner;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Optional;

public interface PartnerRepositoryCustom {

    Optional<Partner> searchNearestAndInCoverageAreaByPoint(GeoJsonPoint point);
}
