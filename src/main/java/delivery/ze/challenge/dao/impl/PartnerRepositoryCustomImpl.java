package delivery.ze.challenge.dao.impl;

import delivery.ze.challenge.dao.PartnerRepositoryCustom;
import delivery.ze.challenge.domain.Partner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

@RequiredArgsConstructor
public class PartnerRepositoryCustomImpl implements PartnerRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Partner> searchNearestAndInCoverageAreaByPoint(GeoJsonPoint point) {
        Criteria criteria = Criteria.where("address").near(point).and("coverageArea").intersects(point);
        Query query = new Query(criteria);

        return Optional.ofNullable(mongoTemplate.findOne(query, Partner.class));
    }
}
