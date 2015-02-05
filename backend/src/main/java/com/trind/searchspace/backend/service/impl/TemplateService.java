package  com.trind.searchspace.backend.service.impl;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.trind.searchspace.backend.mongo.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joachim on 2014-10-26.
 */
@Service
public class TemplateService {

    public static final String COLLECTION = "templates";

    @Autowired
    MongoService mongoService;

    public List<String> getTemplates() {
        List<String> allObjects = mongoService.getAllObjects(COLLECTION, String.class);

        List<String> templates = new ArrayList<>();
        for (String allObject : allObjects) {
            DBObject parse = (DBObject) JSON.parse(allObject);
            templates.add(parse.get("templateName").toString());
        }


        return templates;
    }

    public String getById(String id) {
        return mongoService.getObject(id, COLLECTION, String.class);
    }

    public void save(String template) {
        DBObject parse = (DBObject) JSON.parse(template);
        mongoService.save(parse, parse.get("templateName").toString(), COLLECTION);
    }

    public void remove(String templateName) {
        mongoService.remove(templateName, COLLECTION);
    }
}
