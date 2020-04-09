package delivery.ze.challenge.converter;

import delivery.ze.challenge.domain.Partner;
import delivery.ze.challenge.dto.PartnerDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PartnerDTOToPartnerConverter implements Converter<PartnerDTO, Partner> {

    @Override
    public Partner convert(PartnerDTO partnerDTO) {
        return new Partner(partnerDTO.getTradingName(), partnerDTO.getOwnerName(),
                partnerDTO.getDocument(), partnerDTO.getCoverageArea(), partnerDTO.getAddress());
    }
}
