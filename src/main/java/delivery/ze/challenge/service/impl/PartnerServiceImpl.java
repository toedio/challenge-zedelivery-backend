package delivery.ze.challenge.service.impl;

import delivery.ze.challenge.dao.PartnerRepository;
import delivery.ze.challenge.domain.Partner;
import delivery.ze.challenge.exception.BadRequestException;
import delivery.ze.challenge.exception.NotFoundException;
import delivery.ze.challenge.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;


@Service
public class PartnerServiceImpl implements PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Cacheable("partner")
    public Partner getById(String id) {
        return partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Partner not found. Id: " + id));
    }

    @Override
    public Partner create(Partner partner) {
        Boolean foundByDocument = existsByDocument(partner.getDocument());

        if(foundByDocument)
            throw new BadRequestException("Partner already exists with this cnpj");

        return partnerRepository.save(partner);
    }

    public Partner search(Double lng, Double lat) {
        Point point = new Point(lng, lat);
        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(point);
        return partnerRepository.searchNearestAndInCoverageAreaByPoint(geoJsonPoint)
                .orElseThrow(() -> new NotFoundException("Partner not found by this lat and lng"));
    }

    private Boolean existsByDocument(String document) {
        return partnerRepository.existsByDocument(document);
    }
}
