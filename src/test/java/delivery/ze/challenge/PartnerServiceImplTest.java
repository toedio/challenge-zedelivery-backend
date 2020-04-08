package delivery.ze.challenge;

import delivery.ze.challenge.domain.Partner;
import delivery.ze.challenge.domain.PartnerRepository;
import delivery.ze.challenge.dto.PartnerDTO;
import delivery.ze.challenge.exception.BadRequestException;
import delivery.ze.challenge.exception.NotFoundException;
import delivery.ze.challenge.service.impl.PartnerServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PartnerServiceImplTest extends UnitTest{

    @InjectMocks
    private PartnerServiceImpl partnerService;

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Captor
    private ArgumentCaptor<Partner> partnerArgumentCaptor;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void resetMock() {
        Mockito.reset(partnerRepository, mongoTemplate);
    }

    @Test
    public void shouldGetById() {
        given(partnerRepository.findById(BDDMockito.anyString()))
                .willReturn(Optional.of(new Partner()));

        PartnerDTO partnerDTO = partnerService.getById("123456");
        assertNotNull(partnerDTO);

        verify(partnerRepository, times(1))
                .findById(anyString());
    }

    @Test
    public void shouldNotGetByIdNotFound() {
        given(partnerRepository.findById(BDDMockito.anyString()))
                .willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> partnerService.getById("123456"));

        verify(partnerRepository, times(1))
                .findById(anyString());
    }

    @Test
    public void shouldCreate() {
        String document = FAKER.number().digits(15);
        given(partnerRepository.findByDocument(eq(document)))
                .willReturn(Optional.empty());

        given(partnerRepository.save(any(Partner.class)))
                .willReturn(new Partner());

        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setDocument(document);
        partnerDTO.setOwnerName(FAKER.name().fullName());
        partnerDTO.setTradingName(FAKER.company().name());
        partnerDTO.setAddress(new GeoJsonPoint(getRandomLng(), getRandomLat()));
        GeoJsonPolygon points = new GeoJsonPolygon(Arrays.asList(new Point(getRandomLng(), getRandomLat()), new Point(getRandomLng(), getRandomLat())));
        partnerDTO.setCoverageArea(new GeoJsonMultiPolygon(Collections.singletonList(points)));

        partnerService.create(partnerDTO);

        verify(partnerRepository, times(1))
                .findByDocument(eq(document));

        verify(partnerRepository, times(1))
                .save(partnerArgumentCaptor.capture());

        Partner partner = partnerArgumentCaptor.getValue();
        assertEquals(partnerDTO.getDocument(), partner.getDocument());
        assertEquals(partnerDTO.getTradingName(), partner.getTradingName());
        assertEquals(partnerDTO.getOwnerName(), partner.getOwnerName());
        assertEquals(partnerDTO.getAddress(), partner.getAddress());
        assertEquals(partnerDTO.getCoverageArea(), partner.getCoverageArea());
    }

    @Test
    public void shouldNotCreateCNPJAlreadyExists() {
        String document = FAKER.number().digits(15);
        given(partnerRepository.findByDocument(eq(document)))
                .willReturn(Optional.of(new Partner()));

        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setDocument(document);
        partnerDTO.setOwnerName(FAKER.name().fullName());
        partnerDTO.setTradingName(FAKER.company().name());
        partnerDTO.setAddress(new GeoJsonPoint(getRandomLng(), getRandomLat()));
        GeoJsonPolygon points = new GeoJsonPolygon(Arrays.asList(new Point(getRandomLng(), getRandomLat()), new Point(getRandomLng(), getRandomLat())));
        partnerDTO.setCoverageArea(new GeoJsonMultiPolygon(Collections.singletonList(points)));

        assertThrows(BadRequestException.class, () -> partnerService.create(partnerDTO));

        verify(partnerRepository, times(1))
                .findByDocument(eq(document));

        verify(partnerRepository, never())
                .save(any(Partner.class));
    }

    @Test
    public void shouldFindSearchingByLatLng() {
        given(mongoTemplate.findOne(any(Query.class), eq(Partner.class)))
                .willReturn(new Partner());

        Double lat = getRandomLat();
        Double lng = getRandomLng();
        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(new Point(lng, lat));

        Criteria expectedCriteria = Criteria.where("address").near(geoJsonPoint).and("coverageArea").intersects(geoJsonPoint);
        Query expectedQuery = new Query(expectedCriteria);

        ArgumentCaptor<Query> criteriaArgumentCaptor = ArgumentCaptor.forClass(Query.class);

        partnerService.search(lat, lng);

        verify(mongoTemplate, times(1))
                .findOne(criteriaArgumentCaptor.capture(), eq(Partner.class));

        Query query = criteriaArgumentCaptor.getValue();
        assertEquals(expectedQuery, query);
    }

    @Test
    public void shouldNotFindSearchingByLatLng() {
        given(mongoTemplate.findOne(any(Query.class), eq(Partner.class)))
                .willReturn(null);

        Double lat = getRandomLat();
        Double lng = getRandomLng();
        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(new Point(lng, lat));

        Criteria expectedCriteria = Criteria.where("address").near(geoJsonPoint).and("coverageArea").intersects(geoJsonPoint);
        Query expectedQuery = new Query(expectedCriteria);

        ArgumentCaptor<Query> criteriaArgumentCaptor = ArgumentCaptor.forClass(Query.class);

        assertThrows(NotFoundException.class, () -> partnerService.search(lat, lng));

        verify(mongoTemplate, times(1))
                .findOne(criteriaArgumentCaptor.capture(), eq(Partner.class));

        Query query = criteriaArgumentCaptor.getValue();
        assertEquals(expectedQuery, query);

    }

}
