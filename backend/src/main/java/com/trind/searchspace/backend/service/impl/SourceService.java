package  com.trind.searchspace.backend.service.impl;

import com.trind.searchspace.backend.model.query.targettype.settings.QueryTargetTypeSettings;
import com.trind.searchspace.backend.mongo.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Joachim on 2014-09-29.
 */
@Service
public class SourceService {

    public static final String COLLECTION = "sources";
    @Autowired
    MongoService mongoService;

    public List<QueryTargetTypeSettings> getAllSources() {
  /*      List<QueryTargetTypeSettings> sources = new ArrayList<>();

        ElasticQueryTargetTypeSettings source = new ElasticQueryTargetTypeSettings();
        source.setId("Test1");
        source.setAddress("192.168.0.28:9300");
        source.setClusterName("elasticsearch");

        ElasticQueryTargetTypeSettings source2 = new ElasticQueryTargetTypeSettings();
        source2.setId("Test134");
        source2.setAddress("192.168.0.28:9300");
        source2.setClusterName("elasticsearch");

        sources.add(source);
        sources.add(source2);*/


        return mongoService.getAllObjects(COLLECTION, QueryTargetTypeSettings.class);
    }


    public QueryTargetTypeSettings getSourceById(String id) {
/*        for (QueryTargetTypeSettings source : getAllSources()) {
            if (source.getId().equals(id)) {
                return source;
            }
        }*/
        return mongoService.getObject(id, COLLECTION, QueryTargetTypeSettings.class);
    }

    public void save(QueryTargetTypeSettings queryTargetTypeSettings) {
        mongoService.save(queryTargetTypeSettings, queryTargetTypeSettings.getId(), COLLECTION);
    }

    public void remove(QueryTargetTypeSettings queryTargetTypeSettings) {
        mongoService.remove(queryTargetTypeSettings.getId(), COLLECTION);
    }

    public void remove(String id) {
        mongoService.remove(id, COLLECTION);
    }


}
