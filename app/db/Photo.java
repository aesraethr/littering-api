package db;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

@DynamoDBTable(tableName = "Photos")
public class Photo {

    private String id;
    private String name;
    private String url;
    private double longitude;
    private double latitude;
    private long timestamp;
    private String description;
    private boolean valide;

    public Photo(){}

    public Photo(String name, String url, double longitude, double latitude, long timestamp, String description) {
        this.name = name;
        this.url = url;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
        this.description = description;
    }

    public Photo(String id, String name, String url, double longitude, double latitude, long timestamp, String description, boolean valide) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
        this.description = description;
        this.valide = valide;
    }

    @DynamoDBHashKey(attributeName = "Id")
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "Url")
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @DynamoDBAttribute(attributeName = "Longitude")
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @DynamoDBAttribute(attributeName = "Latitude")
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @DynamoDBAttribute(attributeName = "Timestamp")
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDBAttribute(attributeName = "Description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "Valide")
    public boolean isValide() {
        return valide;
    }
    public void setValide(boolean valide) {
        this.valide = valide;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("url", url)
                .append("longitude", longitude)
                .append("latitude", latitude)
                .append("timestamp", timestamp)
                .append("description", description)
                .append("valide",valide)
                .toString();
    }

}
