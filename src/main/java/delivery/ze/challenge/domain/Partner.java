package delivery.ze.challenge.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import delivery.ze.challenge.utils.GeoJsonMultiPolygonSerializer;
import delivery.ze.challenge.utils.GeoJsonPointSerializer;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document
public class Partner {

    @Id
    private String id;

    private String tradingName;

    private String ownerName;

    private String document;

    @JsonSerialize(using = GeoJsonMultiPolygonSerializer.class)
    private GeoJsonMultiPolygon coverageArea;

    @JsonSerialize(using = GeoJsonPointSerializer.class)
    private GeoJsonPoint address;

    public Partner(String tradingName, String ownerName, String document, GeoJsonMultiPolygon coverageArea, GeoJsonPoint address) {
        this.tradingName = tradingName;
        this.ownerName = ownerName;
        this.document = document;
        this.coverageArea = coverageArea;
        this.address = address;
    }

}
