package delivery.ze.challenge.service;

import delivery.ze.challenge.domain.Partner;

public interface PartnerService {

    Partner getById(String id);

    Partner create(Partner partner);

    Partner search(Double lng, Double lat);
}
