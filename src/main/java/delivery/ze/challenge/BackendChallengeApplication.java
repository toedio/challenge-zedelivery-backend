package delivery.ze.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.ze.challenge.domain.Partner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
@EnableCaching
public class BackendChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendChallengeApplication.class, args);
	}

	@Bean
	@Primary
	public RedisCacheConfiguration defaultCacheConfig(ObjectMapper objectMapper) {
		Jackson2JsonRedisSerializer<Partner> serializer = new Jackson2JsonRedisSerializer<>(Partner.class);
		serializer.setObjectMapper(objectMapper);
		return RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(SerializationPair.fromSerializer(serializer))
				.prefixKeysWith("");
	}
}
