package delivery.ze.challenge.dao;

import delivery.ze.challenge.domain.Partner;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartnerRepository extends MongoRepository<Partner, String>, PartnerRepositoryCustom {
    Boolean existsByDocument(String document);
}
