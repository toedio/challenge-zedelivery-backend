package delivery.ze.challenge.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import delivery.ze.challenge.utils.GeoJsonMultiPolygonSerializer;
import delivery.ze.challenge.utils.GeoJsonPointSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Getter
@NoArgsConstructor
public class PartnerDTO implements Serializable {

    private static final long serialVersionUID = 1010225128202819155L;

    @Null
    private String id;

    @NotBlank
    private String tradingName;

    @NotBlank
    private String ownerName;

    @NotBlank
    @CNPJ
    private String document;

    @NotNull
    @JsonSerialize(using = GeoJsonMultiPolygonSerializer.class)
    private GeoJsonMultiPolygon coverageArea;

    @NotNull
    @JsonSerialize(using = GeoJsonPointSerializer.class)
    private GeoJsonPoint address;

    public PartnerDTO (String id, String tradingName, String ownerName, String document, GeoJsonMultiPolygon coverageArea, GeoJsonPoint address) {
        this.id = id;
        this.tradingName = tradingName;
        this.ownerName = ownerName;
        this.document = document;
        this.coverageArea = coverageArea;
        this.address = address;
    }
}
