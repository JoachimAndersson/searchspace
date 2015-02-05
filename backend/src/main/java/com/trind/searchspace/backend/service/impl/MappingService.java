package  com.trind.searchspace.backend.service.impl;

import com.trind.searchspace.backend.model.mapping.FieldMapping;
import com.trind.searchspace.backend.mongo.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Joachim on 2014-09-29.
 */
@Service
public class MappingService {

    public static final String COLLECTION = "mappings";

    @Autowired
    MongoService mongoService;

    public List<FieldMapping> getAllMappings() {
        return mongoService.getAllObjects(COLLECTION, FieldMapping.class);
    }


    public FieldMapping getMappingById(String id) {
        return mongoService.getObject(id, COLLECTION, FieldMapping.class);
    }

    public void save(FieldMapping fieldMapping) {
        mongoService.save(fieldMapping, fieldMapping.getMappingName(), COLLECTION);
    }

    public void remove(FieldMapping fieldMapping) {
        mongoService.remove(fieldMapping.getMappingName(), COLLECTION);
    }

    public void remove(String id) {
        mongoService.remove(id, COLLECTION);
    }


}
