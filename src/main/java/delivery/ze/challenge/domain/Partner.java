package delivery.ze.challenge.domain;

import delivery.ze.challenge.dto.PartnerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@NoArgsConstructor
public class Partner {

    @Id
    private String id;

    private String tradingName;

    private String ownerName;

    private String document;

    private GeoJsonMultiPolygon coverageArea;

    private GeoJsonPoint address;

    public Partner(PartnerDTO partnerDTO) {
        this.tradingName = partnerDTO.getTradingName();
        this.ownerName = partnerDTO.getOwnerName();
        this.document = partnerDTO.getDocument();
        this.coverageArea = partnerDTO.getCoverageArea();
        this.address = partnerDTO.getAddress();
    }

}
