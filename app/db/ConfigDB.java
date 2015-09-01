package db;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class ConfigDB {

    public static String TABLE_NAME = "littering-config";

    private static AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient().withRegion(Regions.EU_CENTRAL_1);
    private static DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);

    public static void save(Config config) {
        mapper.save(config);
    }

    public static Config load(String id){
        DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        return mapper.load(Config.class, id, config);
    }
}
