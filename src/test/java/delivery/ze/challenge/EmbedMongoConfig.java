package delivery.ze.challenge;

import delivery.ze.challenge.domain.Partner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

@Configuration
@Profile("test")
public class EmbedMongoConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void ensureIndex() {
           IndexOperations indexOperations = mongoTemplate.indexOps(Partner.class);
           indexOperations.ensureIndex(new GeospatialIndex("address").typed(GeoSpatialIndexType.GEO_2DSPHERE));
           indexOperations.ensureIndex(new Index("document", Sort.Direction.ASC).unique());
   }
}
