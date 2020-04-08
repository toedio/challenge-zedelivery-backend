package delivery.ze.challenge.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import delivery.ze.challenge.domain.Partner;
import delivery.ze.challenge.utils.GeoJsonMultiPolygonSerializer;
import delivery.ze.challenge.utils.GeoJsonPointSerializer;
import delivery.ze.challenge.validator.groups.Create;
import delivery.ze.challenge.validator.groups.Update;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@NoArgsConstructor
public class PartnerDTO {

    @Null(groups = Create.class)
    @NotBlank(groups = Update.class)
    private String id;

    @NotBlank(groups = {Create.class, Update.class})
    private String tradingName;

    @NotBlank(groups = {Create.class, Update.class})
    private String ownerName;

    @NotBlank(groups = {Create.class, Update.class})
    @CNPJ(groups = {Create.class, Update.class})
    private String document;

    @NotNull(groups = {Create.class, Update.class})
    @JsonSerialize(using = GeoJsonMultiPolygonSerializer.class)
    private GeoJsonMultiPolygon coverageArea;

    @NotNull(groups = {Create.class, Update.class})
    @JsonSerialize(using = GeoJsonPointSerializer.class)
    private GeoJsonPoint address;

    public PartnerDTO (Partner partner) {
        this.id = partner.getId();
        this.tradingName = partner.getTradingName();
        this.ownerName = partner.getOwnerName();
        this.document = partner.getDocument();
        this.coverageArea = partner.getCoverageArea();
        this.address = partner.getAddress();
    }
}
