package delivery.ze.challenge;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UnitTest {

    protected static final Faker FAKER = new Faker();

    protected static Double getRandomLat() {
        return FAKER.number().randomDouble(5, -90, 90);
    }

    protected static Double getRandomLng() {
        return FAKER.number().randomDouble(5, -180, 180);
    }
}
