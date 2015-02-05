package  com.trind.searchspace.backend.mongo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joachim on 2014-10-26.
 */
@Service
public class MongoService {
    private static ObjectMapper mapper = null;
    private static DB db = null;

    public synchronized ObjectMapper getObjectMapper() {
        if (mapper != null) return mapper;

        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    public synchronized DB getMongoDB() throws UnknownHostException {
        if (db != null) return db;
        MongoClient mongoClient = new MongoClient("192.168.0.28");
        db = mongoClient.getDB("omnisearch");
        return db;
    }


    public void save(Object ob, String id, String collection) {
        try {
            String json = getObjectMapper().writeValueAsString(ob);
            DBCollection dbCollection = getMongoDB().getCollection(collection);
            DBObject dbObject = (DBObject) JSON.parse(json);
            dbObject.put("_id", id);
            dbCollection.save(dbObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(String id, String collection) {
        try {
            DBCollection dbCollection = getMongoDB().getCollection(collection);
            DBObject dbObject = new BasicDBObject();
            dbObject.put("_id", id);
            dbCollection.remove(dbObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T extends Object> T getObject(String id, String collection, Class<T> tClass) {
        try {
            DBCollection dbCollection = getMongoDB().getCollection(collection);
            DBObject dbObject = new BasicDBObject();
            dbObject.put("_id", id);

            DBObject one = dbCollection.findOne(dbObject);

            String json = JSON.serialize(one);
            if (tClass.equals(String.class)) {
                return (T) json;
            }

            return getObjectMapper().readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends Object> List<T> getAllObjects(String collection, Class<T> tClass) {
        try {
            DBCollection dbCollection = getMongoDB().getCollection(collection);

            DBCursor dbObjects = dbCollection.find();

            List<T> objects = new ArrayList<>();
            for (DBObject dbObject : dbObjects) {
                try {
                    String json = JSON.serialize(dbObject);
                    if (tClass.equals(String.class)) {
                        objects.add((T) json);
                    } else {
                        objects.add(getObjectMapper().readValue(json, tClass));
                    }
                } catch (IOException e) {
                }
            }
            return objects;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
