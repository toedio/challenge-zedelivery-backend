package delivery.ze.challenge;

import com.mongodb.client.model.geojson.GeoJsonObjectType;
import delivery.ze.challenge.domain.Partner;
import delivery.ze.challenge.dto.PartnerDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PartnerCreateTest extends IntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldCreatePartner() throws Exception {

        List<Point> pointList1 = new ArrayList<>(10);
        IntStream.range(0, 10).forEach(i -> pointList1.add(new Point(getRandomLng(), getRandomLat())));

        List<Point> pointList2 = new ArrayList<>(5);
        IntStream.range(0, 5).forEach(i -> pointList2.add(new Point(getRandomLng(), getRandomLat())));
        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setDocument("21.687.442/0001-28");
        partnerDTO.setOwnerName(FAKER.name().fullName());
        partnerDTO.setTradingName(FAKER.company().name());
        partnerDTO.setAddress(new GeoJsonPoint(getRandomLng(), getRandomLat()));
        GeoJsonPolygon points = new GeoJsonPolygon(pointList1);
        GeoJsonPolygon points2 = new GeoJsonPolygon(pointList2);
        partnerDTO.setCoverageArea(new GeoJsonMultiPolygon(Arrays.asList(points, points2)));

        this.mockMvc.perform(post("/partners")
                .content(objectMapper.writeValueAsString(partnerDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.document", is(partnerDTO.getDocument())))
                .andExpect(jsonPath("$.ownerName", is(partnerDTO.getOwnerName())))
                .andExpect(jsonPath("$.tradingName", is(partnerDTO.getTradingName())))
                .andExpect(jsonPath("$.coverageArea.type", is(GeoJsonObjectType.MULTI_POLYGON.getTypeName())))

                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][0][0]", is(points.getPoints().get(0).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][0][1]", is(points.getPoints().get(0).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][1][0]", is(points.getPoints().get(1).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][1][1]", is(points.getPoints().get(1).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][2][0]", is(points.getPoints().get(2).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][2][1]", is(points.getPoints().get(2).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][3][0]", is(points.getPoints().get(3).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][3][1]", is(points.getPoints().get(3).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][4][0]", is(points.getPoints().get(4).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][4][1]", is(points.getPoints().get(4).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][5][0]", is(points.getPoints().get(5).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][5][1]", is(points.getPoints().get(5).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][6][0]", is(points.getPoints().get(6).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][6][1]", is(points.getPoints().get(6).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][7][0]", is(points.getPoints().get(7).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][7][1]", is(points.getPoints().get(7).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][8][0]", is(points.getPoints().get(8).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][8][1]", is(points.getPoints().get(8).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][9][0]", is(points.getPoints().get(9).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][9][1]", is(points.getPoints().get(9).getY())))

                .andExpect(jsonPath("$.coverageArea.coordinates[1][0][0][0]", is(points2.getPoints().get(0).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[1][0][0][1]", is(points2.getPoints().get(0).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[1][0][1][0]", is(points2.getPoints().get(1).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[1][0][1][1]", is(points2.getPoints().get(1).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[1][0][2][0]", is(points2.getPoints().get(2).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[1][0][2][1]", is(points2.getPoints().get(2).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[1][0][3][0]", is(points2.getPoints().get(3).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[1][0][3][1]", is(points2.getPoints().get(3).getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[1][0][4][0]", is(points2.getPoints().get(4).getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[1][0][4][1]", is(points2.getPoints().get(4).getY())))

                .andExpect(jsonPath("$.address.type", is(GeoJsonObjectType.POINT.getTypeName())))
                .andExpect(jsonPath("$.address.coordinates[0]", is(partnerDTO.getAddress().getX())))
                .andExpect(jsonPath("$.address.coordinates[1]", is(partnerDTO.getAddress().getY())));

    }

    @Test
    public void shouldNotCreateCNPJAlreadyExists() throws Exception {
        Partner partner = new Partner();
        partner.setDocument("95.616.272/0001-27");
        partner.setOwnerName(FAKER.name().fullName());
        partner.setTradingName(FAKER.company().name());
        partner.setAddress(new GeoJsonPoint(getRandomLng(), getRandomLat()));
        GeoJsonPolygon points = new GeoJsonPolygon(Arrays.asList(new Point(getRandomLng(), getRandomLat()), new Point(getRandomLng(), getRandomLat())));
        partner.setCoverageArea(new GeoJsonMultiPolygon(Collections.singletonList(points)));
        partnerRepository.save(partner);

        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setDocument("95.616.272/0001-27");
        partnerDTO.setOwnerName(FAKER.name().fullName());
        partnerDTO.setTradingName(FAKER.company().name());
        partnerDTO.setAddress(new GeoJsonPoint(getRandomLng(), getRandomLat()));
        GeoJsonPolygon pointsDTO = new GeoJsonPolygon(Arrays.asList(new Point(getRandomLng(), getRandomLat()), new Point(getRandomLng(), getRandomLat())));
        partnerDTO.setCoverageArea(new GeoJsonMultiPolygon(Collections.singletonList(pointsDTO)));

        this.mockMvc.perform(post("/partners")
                .content(objectMapper.writeValueAsString(partnerDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].known", is(true)))
                .andExpect(jsonPath("$.[0].message", is("Partner already exists with this cnpj")));
    }
}

