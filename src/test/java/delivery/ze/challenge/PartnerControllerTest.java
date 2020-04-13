package delivery.ze.challenge;

import com.mongodb.client.model.geojson.GeoJsonObjectType;
import delivery.ze.challenge.domain.Partner;
import delivery.ze.challenge.dto.PartnerDTO;
import delivery.ze.challenge.exception.ErrorDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PartnerControllerTest extends IntegrationTest{

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldGetById() throws Exception {

        String document = FAKER.number().digits(14);
        String ownerName = FAKER.name().fullName();
        String tradingName = FAKER.company().name();

        GeoJsonPoint address = new GeoJsonPoint(getRandomLng(), getRandomLat());

        Point pointA = new Point(getRandomLng(), getRandomLat());
        Point pointB = new Point(getRandomLng(), getRandomLat());
        Point pointC = new Point(getRandomLng(), getRandomLat());
        Point pointD = new Point(getRandomLng(), getRandomLat());

        GeoJsonPolygon points = new GeoJsonPolygon(Arrays.asList(pointA, pointB, pointC, pointD));
        GeoJsonMultiPolygon coverageArea = new GeoJsonMultiPolygon(Collections.singletonList(points));

        Partner partner = new Partner(tradingName, ownerName, document, coverageArea, address);
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
    public void shouldNotGetIdNotFound() throws Exception {
        mockMvc.perform(get("/partners/5555555555")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetPointInCoverageArea() throws Exception {

        String document = FAKER.number().digits(14);
        String ownerName = FAKER.name().fullName();
        String tradingName = FAKER.company().name();

        Point pointA = new Point(-46.55169, -23.62617);
        Point pointB = new Point(-46.55327, -23.62746);
        Point pointC = new Point(-46.55063, -23.62812);
        Point pointD = new Point(-46.54987, -23.62686);
        Point pointE = new Point(-46.55169, -23.62617);

        Point pointA1 = new Point(-46.55149, -23.62691);
        Point pointB1 = new Point(-46.55104, -23.62755);
        Point pointC1 = new Point(-46.55079, -23.6274);
        Point pointD1 = new Point(-46.55149, -23.62691);

        GeoJsonPoint address = new GeoJsonPoint(-46.55111, -23.62671);

        GeoJsonPolygon borderOutPoints = new GeoJsonPolygon(Arrays.asList(pointA, pointB, pointC, pointD, pointE));
        GeoJsonPolygon borderInPoints = new GeoJsonPolygon(Arrays.asList(pointA1, pointB1, pointC1, pointD1));
        GeoJsonMultiPolygon coverageArea = new GeoJsonMultiPolygon(Arrays.asList(borderOutPoints, borderInPoints));

        Partner partner = new Partner(tradingName, ownerName, document, coverageArea, address);
        partnerRepository.save(partner);

        Point pointInCoverageArea = new Point(-46.55163, -23.62732);

        this.mockMvc.perform(get("/partners")
                .queryParam("lat", String.valueOf(pointInCoverageArea.getY()))
                .queryParam("lng", String.valueOf(pointInCoverageArea.getX()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(partner.getId())));

        partnerRepository.deleteById(partner.getId());
    }

    @Test
    public void shouldNotGetPointLatLngOutCoverageArea() throws Exception{
        String document = FAKER.number().digits(14);
        String ownerName = FAKER.name().fullName();
        String tradingName = FAKER.company().name();

        Point pointA = new Point(-46.55169, -23.62617);
        Point pointB = new Point(-46.55327, -23.62746);
        Point pointC = new Point(-46.55063, -23.62812);
        Point pointD = new Point(-46.54987, -23.62686);
        Point pointE = new Point(-46.55169, -23.62617);

        Point pointA1 = new Point(-46.55149, -23.62691);
        Point pointB1 = new Point(-46.55104, -23.62755);
        Point pointC1 = new Point(-46.55079, -23.6274);
        Point pointD1 = new Point(-46.55149, -23.62691);

        GeoJsonPoint address = new GeoJsonPoint(-46.55111, -23.62671);

        GeoJsonPolygon borderPoints = new GeoJsonPolygon(Arrays.asList(pointA, pointB, pointC, pointD, pointE));
        GeoJsonPolygon borderInPoints = new GeoJsonPolygon(Arrays.asList(pointA1, pointB1, pointC1, pointD1));
        GeoJsonMultiPolygon coverageArea = new GeoJsonMultiPolygon(Arrays.asList(borderPoints, borderInPoints));

        Partner partner = new Partner(tradingName, ownerName, document, coverageArea, address);
        partnerRepository.save(partner);

        Point pointCoverage = new Point(-49.55075, -27.62613);

        this.mockMvc.perform(get("/partners")
                .queryParam("lat", String.valueOf(pointCoverage.getY()))
                .queryParam("lng", String.valueOf(pointCoverage.getX()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        partnerRepository.deleteById(partner.getId());
    }

    @Test
    public void shouldGetNearestBetweenTwoPartnerInCoverageArea() throws Exception{

        String documentPartnerA = FAKER.number().digits(14);
        String ownerNamePartnerA = FAKER.name().fullName();
        String tradingNamePartnerA = FAKER.company().name();
        GeoJsonPoint addressPartnerA = new GeoJsonPoint(-47.55111, -24.62671);

        String documentPartnerB = FAKER.number().digits(14);
        String ownerNamePartnerB = FAKER.name().fullName();
        String tradingNamePartnerB = FAKER.company().name();
        GeoJsonPoint addressPartnerB = new GeoJsonPoint(-46.55111, -23.62671);

        Point pointA = new Point(-46.55169, -23.62617);
        Point pointB = new Point(-46.55327, -23.62746);
        Point pointC = new Point(-46.55063, -23.62812);
        Point pointD = new Point(-46.54987, -23.62686);
        Point pointE = new Point(-46.55169, -23.62617);

        Point pointA1 = new Point(-46.55149, -23.62691);
        Point pointB1 = new Point(-46.55104, -23.62755);
        Point pointC1 = new Point(-46.55079, -23.6274);
        Point pointD1 = new Point(-46.55149, -23.62691);

        GeoJsonPolygon borderOutPoints = new GeoJsonPolygon(Arrays.asList(pointA, pointB, pointC, pointD, pointE));
        GeoJsonPolygon borderInPoints = new GeoJsonPolygon(Arrays.asList(pointA1, pointB1, pointC1, pointD1));
        GeoJsonMultiPolygon coverageArea = new GeoJsonMultiPolygon(Arrays.asList(borderOutPoints, borderInPoints));

        Partner partnerA = new Partner(tradingNamePartnerA, ownerNamePartnerA,documentPartnerA, coverageArea, addressPartnerA);
        partnerRepository.save(partnerA);

        Partner partnerB = new Partner(tradingNamePartnerB, ownerNamePartnerB, documentPartnerB, coverageArea, addressPartnerB);
        partnerRepository.save(partnerB);

        Point pointInCoverageNearPartnerB = new Point(-46.55163, -23.62732);

        this.mockMvc.perform(get("/partners")
                .queryParam("lat", String.valueOf(pointInCoverageNearPartnerB.getY()))
                .queryParam("lng", String.valueOf(pointInCoverageNearPartnerB.getX()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(partnerB.getId())));

        partnerRepository.deleteById(partnerA.getId());
        partnerRepository.deleteById(partnerB.getId());
    }

    @Test
    public void shouldCreatePartner() throws Exception {

        String tradingName = FAKER.company().name();
        String ownerName = FAKER.name().fullName();
        String document = "21.687.442/0001-28";

        List<Point> pointList1 = new ArrayList<>(10);
        IntStream.range(0, 10).forEach(i -> pointList1.add(new Point(getRandomLng(), getRandomLat())));

        List<Point> pointList2 = new ArrayList<>(5);
        IntStream.range(0, 5).forEach(i -> pointList2.add(new Point(getRandomLng(), getRandomLat())));

        GeoJsonPoint address = new GeoJsonPoint(getRandomLng(), getRandomLat());

        GeoJsonPolygon points = new GeoJsonPolygon(pointList1);
        GeoJsonPolygon points2 = new GeoJsonPolygon(pointList2);
        GeoJsonMultiPolygon coverageArea = new GeoJsonMultiPolygon(Arrays.asList(points, points2));

        PartnerDTO partnerDTO = new PartnerDTO(null, tradingName, ownerName, document, coverageArea, address);

        String contentAsString = this.mockMvc.perform(post("/partners")
                .content(objectMapper.writeValueAsString(partnerDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PartnerDTO partnerResponseDTO = objectMapper.readValue(contentAsString, PartnerDTO.class);

        assertNotNull(partnerResponseDTO.getId());
        assertEquals(document, partnerResponseDTO.getDocument());
        assertEquals(ownerName, partnerResponseDTO.getOwnerName());
        assertEquals(tradingName, partnerResponseDTO.getTradingName());
        assertEquals(coverageArea, partnerResponseDTO.getCoverageArea());
        assertEquals(address, partnerResponseDTO.getAddress());
    }

    @Test
    public void shouldNotCreateCNPJAlreadyExists() throws Exception {

        String tradingName = FAKER.company().name();
        String ownerName = FAKER.name().fullName();
        String document = "95.616.272/0001-27";

        GeoJsonPoint address = new GeoJsonPoint(getRandomLng(), getRandomLat());

        GeoJsonPolygon points = new GeoJsonPolygon(Arrays.asList(new Point(getRandomLng(), getRandomLat()), new Point(getRandomLng(), getRandomLat())));
        GeoJsonMultiPolygon coverageArea = new GeoJsonMultiPolygon(Collections.singletonList(points));

        Partner partner = new Partner(tradingName, ownerName, document, coverageArea, address);
        partnerRepository.save(partner);

        String tradingNameDTO = FAKER.company().name();
        String ownerNameDTO = FAKER.name().fullName();

        GeoJsonPoint addressDTO = new GeoJsonPoint(getRandomLng(), getRandomLat());

        GeoJsonPolygon pointsDTO = new GeoJsonPolygon(Arrays.asList(new Point(getRandomLng(), getRandomLat()), new Point(getRandomLng(), getRandomLat())));
        GeoJsonMultiPolygon coverageAreaDTO = new GeoJsonMultiPolygon(Collections.singletonList(pointsDTO));

        PartnerDTO partnerDTO = new PartnerDTO(null, tradingNameDTO, ownerNameDTO, document, coverageAreaDTO, addressDTO);

        String contentAsString = this.mockMvc.perform(post("/partners")
                .content(objectMapper.writeValueAsString(partnerDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorDTO[] errorDTOS = objectMapper.readValue(contentAsString, ErrorDTO[].class);

        Optional<ErrorDTO> optionalErrorDTO = Arrays.stream(errorDTOS).findFirst();
        assertTrue(optionalErrorDTO.isPresent());

        ErrorDTO errorDTO = optionalErrorDTO.get();
        assertTrue(errorDTO.isKnown());
        assertEquals("Partner already exists with this cnpj", errorDTO.getMessage());
    }
}
