package db;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoDB {

    public static String TABLE_NAME = "Photos";

    private static AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient().withRegion(Regions.EU_CENTRAL_1);
    private static DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);

    public static void save(Photo photo) {
        mapper.save(photo);
    }

    public static void delete(Photo photo) {
        mapper.delete(photo);
    }

    public static Photo load(String id){
        DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        return mapper.load(Photo.class, id, config);
    }

    public static List<Photo> loadAll() {
        List<Photo> photos = new ArrayList<>();

        ScanResult scanResult = dynamoDBClient.scan(new ScanRequest().withTableName(TABLE_NAME));
        for(Map<String, AttributeValue> item : scanResult.getItems()) {
            Photo photo = new Photo(
                    item.get("Id").getS(),
                    item.get("Name").getS(),
                    item.get("Url").getS(),
                    Double.parseDouble(item.get("Longitude").getN()),
                    Double.parseDouble(item.get("Latitude").getN()),
                    Long.parseLong(item.get("Timestamp").getN()),
                    item.getOrDefault("Description", new AttributeValue("Pas de description")).getS(),
                    "1".equals(item.getOrDefault("Valide", new AttributeValue().withN("0")).getN())
                            );
            photos.add(photo);
        }

        return photos;
    }

    public static List<Photo> loadAllApproved() {
        List<Photo> photos = new ArrayList<>();
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":val", new AttributeValue().withN("1"));
        ScanResult scanResult = dynamoDBClient.scan(new ScanRequest().withTableName(TABLE_NAME).withFilterExpression("Valide = :val").withExpressionAttributeValues(expressionAttributeValues));
        for(Map<String, AttributeValue> item : scanResult.getItems()) {
            Photo photo = new Photo(
                    item.get("Id").getS(),
                    item.get("Name").getS(),
                    item.get("Url").getS(),
                    Double.parseDouble(item.get("Longitude").getN()),
                    Double.parseDouble(item.get("Latitude").getN()),
                    Long.parseLong(item.get("Timestamp").getN()),
                    item.getOrDefault("Description", new AttributeValue("Pas de description")).getS(),
                    "1".equals(item.getOrDefault("Valide", new AttributeValue().withN("0")).getN())
            );
            photos.add(photo);
        }

        return photos;
    }
}
