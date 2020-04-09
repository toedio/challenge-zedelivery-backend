package delivery.ze.challenge.converter;

import delivery.ze.challenge.domain.Partner;
import delivery.ze.challenge.dto.PartnerDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PartnerToPartnerDTOConverter implements Converter<Partner, PartnerDTO> {

    @Override
    public PartnerDTO convert(Partner partner) {
        return new PartnerDTO(partner.getId(), partner.getTradingName(), partner.getOwnerName(),
                partner.getDocument(), partner.getCoverageArea(), partner.getAddress());
    }
}
