package delivery.ze.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import delivery.ze.challenge.domain.Partner;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
abstract class IntegrationTest {

    @Autowired
    protected MongoRepository<Partner, String> partnerRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext wac;

    protected static final Faker FAKER = new Faker();

    protected static Double getRandomLat() {
        return FAKER.number().randomDouble(5, -90, 90);
    }

    protected static Double getRandomLng() {
        return FAKER.number().randomDouble(5, -180, 180);
    }



}
