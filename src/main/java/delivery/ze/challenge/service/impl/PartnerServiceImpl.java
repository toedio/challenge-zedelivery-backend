package delivery.ze.challenge.service.impl;

import delivery.ze.challenge.domain.Partner;
import delivery.ze.challenge.domain.PartnerRepository;
import delivery.ze.challenge.dto.PartnerDTO;
import delivery.ze.challenge.exception.BadRequestException;
import delivery.ze.challenge.exception.NotFoundException;
import delivery.ze.challenge.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartnerServiceImpl implements PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PartnerDTO getById(String id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Partner not found. Id: " + id));
        return new PartnerDTO(partner);
    }

    @Override
    public PartnerDTO create(PartnerDTO partnerDTO) {
        Optional<Partner> foundByDocument = findByDocument(partnerDTO.getDocument());

        if(foundByDocument.isPresent())
            throw new BadRequestException("Partner already exists with this cnpj");

        Partner partner = new Partner(partnerDTO);
        return new PartnerDTO(partnerRepository.save(partner));
    }

    @Override
    public PartnerDTO search(Double lat, Double lng) {
        Point point = new Point(lng, lat);
        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(point);

        Criteria criteria = Criteria.where("address").near(geoJsonPoint).and("coverageArea").intersects(geoJsonPoint);

        Partner partner = Optional.ofNullable(mongoTemplate.findOne(new Query(criteria), Partner.class))
                .orElseThrow(() -> new NotFoundException("Partner not found for this location"));

        return new PartnerDTO(partner);
    }

    private Optional<Partner> findByDocument(String document) {
        return partnerRepository.findByDocument(document);
    }
}
