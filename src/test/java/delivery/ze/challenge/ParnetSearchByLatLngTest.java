package delivery.ze.challenge;

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

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ParnetSearchByLatLngTest extends IntegrationTest{

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    @Test
    public void shouldFindPointInConverageArea() throws Exception {
        Point pointA = new Point(-46.55169, -23.62617);
        Point pointB = new Point(-46.55327, -23.62746);
        Point pointC = new Point(-46.55063, -23.62812);
        Point pointD = new Point(-46.54987, -23.62686);
        Point pointE = new Point(-46.55169, -23.62617);

        Point pointA1 = new Point(-46.55149, -23.62691);
        Point pointB1 = new Point(-46.55104, -23.62755);
        Point pointC1 = new Point(-46.55079, -23.6274);
        Point pointD1 = new Point(-46.55149, -23.62691);

        Partner partner = new Partner();
        partner.setDocument(FAKER.number().digits(15));
        partner.setOwnerName(FAKER.name().fullName());
        partner.setTradingName(FAKER.company().name());
        partner.setAddress(new GeoJsonPoint(-46.55111, -23.62671));
        GeoJsonPolygon borderPoints = new GeoJsonPolygon(Arrays.asList(pointA, pointB, pointC, pointD, pointE));
        GeoJsonPolygon borderInPoints = new GeoJsonPolygon(Arrays.asList(pointA1, pointB1, pointC1, pointD1));
        partner.setCoverageArea(new GeoJsonMultiPolygon(Arrays.asList(borderPoints, borderInPoints)));
        partnerRepository.save(partner);

        Point pointCoverage = new Point(-46.55163, -23.62732);

        this.mockMvc.perform(get("/partners/search")
                .queryParam("lat", String.valueOf(pointCoverage.getY()))
                .queryParam("lng", String.valueOf(pointCoverage.getX()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(partner.getId())));

        partnerRepository.deleteById(partner.getId());
    }

    @Test
    public void shouldNotFindPointOutConverageArea() throws Exception{
        Point pointA = new Point(-46.55169, -23.62617);
        Point pointB = new Point(-46.55327, -23.62746);
        Point pointC = new Point(-46.55063, -23.62812);
        Point pointD = new Point(-46.54987, -23.62686);
        Point pointE = new Point(-46.55169, -23.62617);

        Point pointA1 = new Point(-46.55149, -23.62691);
        Point pointB1 = new Point(-46.55104, -23.62755);
        Point pointC1 = new Point(-46.55079, -23.6274);
        Point pointD1 = new Point(-46.55149, -23.62691);

        Partner partner = new Partner();
        partner.setDocument(FAKER.number().digits(15));
        partner.setOwnerName(FAKER.name().fullName());
        partner.setTradingName(FAKER.company().name());
        partner.setAddress(new GeoJsonPoint(-46.55111, -23.62671 ));
        GeoJsonPolygon borderPoints = new GeoJsonPolygon(Arrays.asList(pointA, pointB, pointC, pointD, pointE));
        GeoJsonPolygon borderInPoints = new GeoJsonPolygon(Arrays.asList(pointA1, pointB1, pointC1, pointD1));
        partner.setCoverageArea(new GeoJsonMultiPolygon(Arrays.asList(borderPoints, borderInPoints)));
        partnerRepository.save(partner);

        Point pointCoverage = new Point(-46.55075, -23.62613);

        this.mockMvc.perform(get("/partners/search")
                .queryParam("lat", String.valueOf(pointCoverage.getY()))
                .queryParam("lng", String.valueOf(pointCoverage.getX()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        partnerRepository.deleteById(partner.getId());

    }

    @Test
    public void shouldFindNearestBetweenTwoPartnerInConverageArea() throws Exception{
        Point pointA = new Point(-46.55169, -23.62617);
        Point pointB = new Point(-46.55327, -23.62746);
        Point pointC = new Point(-46.55063, -23.62812);
        Point pointD = new Point(-46.54987, -23.62686);
        Point pointE = new Point(-46.55169, -23.62617);

        Point pointA1 = new Point(-46.55149, -23.62691);
        Point pointB1 = new Point(-46.55104, -23.62755);
        Point pointC1 = new Point(-46.55079, -23.6274);
        Point pointD1 = new Point(-46.55149, -23.62691);

        GeoJsonPolygon borderPoints = new GeoJsonPolygon(Arrays.asList(pointA, pointB, pointC, pointD, pointE));
        GeoJsonPolygon borderInPoints = new GeoJsonPolygon(Arrays.asList(pointA1, pointB1, pointC1, pointD1));

        Partner partnerA = new Partner();
        partnerA.setDocument(FAKER.number().digits(15));
        partnerA.setOwnerName(FAKER.name().fullName());
        partnerA.setTradingName(FAKER.company().name());
        partnerA.setAddress(new GeoJsonPoint(-47.55111, -24.62671));
        partnerA.setCoverageArea(new GeoJsonMultiPolygon(Arrays.asList(borderPoints, borderInPoints)));
        partnerRepository.save(partnerA);

        Partner partnerB = new Partner();
        partnerB.setDocument(FAKER.number().digits(15));
        partnerB.setOwnerName(FAKER.name().fullName());
        partnerB.setTradingName(FAKER.company().name());
        partnerB.setAddress(new GeoJsonPoint(-46.55111, -23.62671));
        partnerB.setCoverageArea(new GeoJsonMultiPolygon(Arrays.asList(borderPoints, borderInPoints)));
        partnerRepository.save(partnerB);

        Point pointCoverageNearPartnerB = new Point(-46.55163, -23.62732);

        this.mockMvc.perform(get("/partners/search")
                .queryParam("lat", String.valueOf(pointCoverageNearPartnerB.getY()))
                .queryParam("lng", String.valueOf(pointCoverageNearPartnerB.getX()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(partnerB.getId())));

        partnerRepository.deleteById(partnerA.getId());
        partnerRepository.deleteById(partnerB.getId());
    }
}
