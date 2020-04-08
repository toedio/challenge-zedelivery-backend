package delivery.ze.challenge.service;

import delivery.ze.challenge.dto.PartnerDTO;

public interface PartnerService {

    PartnerDTO getById(String id);

    PartnerDTO create(PartnerDTO partnerDTO);

    PartnerDTO search(Double lat, Double lng);
}
