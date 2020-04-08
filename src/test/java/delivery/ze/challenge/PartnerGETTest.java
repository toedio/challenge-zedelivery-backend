package delivery.ze.challenge;

import com.mongodb.client.model.geojson.GeoJsonObjectType;
import delivery.ze.challenge.domain.Partner;
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

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PartnerGETTest extends IntegrationTest{

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldGetById() throws Exception {

        Point pointA = new Point(getRandomLng(), getRandomLat());
        Point pointB = new Point(getRandomLng(), getRandomLat());
        Point pointC = new Point(getRandomLng(), getRandomLat());
        Point pointD = new Point(getRandomLng(), getRandomLat());

        Partner partner = new Partner();
        partner.setId(FAKER.number().digits(5));
        partner.setDocument(FAKER.number().digits(14));
        partner.setOwnerName(FAKER.name().fullName());
        partner.setTradingName(FAKER.company().name());
        partner.setAddress(new GeoJsonPoint(getRandomLng(), getRandomLat()));
        GeoJsonPolygon points = new GeoJsonPolygon(Arrays.asList(pointA, pointB, pointC, pointD));
        partner.setCoverageArea(new GeoJsonMultiPolygon(Collections.singletonList(points)));
        partnerRepository.save(partner);

        mockMvc.perform(get("/partners/"+partner.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(partner.getId())))
                .andExpect(jsonPath("$.document", is(partner.getDocument())))
                .andExpect(jsonPath("$.ownerName", is(partner.getOwnerName())))
                .andExpect(jsonPath("$.tradingName", is(partner.getTradingName())))
                .andExpect(jsonPath("$.coverageArea.type", is(GeoJsonObjectType.MULTI_POLYGON.getTypeName())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][0][0]", is(pointA.getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][0][1]", is(pointA.getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][1][0]", is(pointB.getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][1][1]", is(pointB.getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][2][0]", is(pointC.getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][2][1]", is(pointC.getY())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][3][0]", is(pointD.getX())))
                .andExpect(jsonPath("$.coverageArea.coordinates[0][0][3][1]", is(pointD.getY())))
                .andExpect(jsonPath("$.address.type", is(GeoJsonObjectType.POINT.getTypeName())))
                .andExpect(jsonPath("$.address.coordinates[0]", is(partner.getAddress().getX())))
                .andExpect(jsonPath("$.address.coordinates[1]", is(partner.getAddress().getY())));
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/partners/5555555555")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
