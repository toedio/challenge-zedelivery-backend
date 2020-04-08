package delivery.ze.challenge.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PartnerRepository extends MongoRepository<Partner, String> {

    Optional<Partner> findByDocument(String document);
}
