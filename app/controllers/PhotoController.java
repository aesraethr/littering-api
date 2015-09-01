package controllers;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import db.ConfigDB;
import db.Photo;
import db.PhotoDB;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.imgscalr.Scalr;
import play.Logger;
import play.mvc.Result;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Api(value = "photos", description = "the basic operations about the littering photos")
public class PhotoController extends BaseApiController {
    private static Logger.ALogger logger = Logger.of(PhotoController.class);
    private AmazonS3 s3Client = new AmazonS3Client();

    @ApiOperation(value = "List all Photos", nickname = "List", httpMethod = "GET", response = db.Photo.class)
    public Result list() {
        return JsonResponse(ConfigDB.load("prod").isModeration()?PhotoDB.loadAllApproved():PhotoDB.loadAll());
    }

    public Result get(String id) {
        Photo photo = PhotoDB.load(id);
        if (photo == null) {
            return notFound();
        }else {
            return JsonResponse(photo);
        }
    }

    public Result approuver(String id) {
        Photo photo = PhotoDB.load(id);
        if (photo == null) {
            return notFound();
        }else {
            photo.setValide(true);
            PhotoDB.save(photo);
            return ok();
        }
    }

    public Result put(){
        response().setHeader("Access-Control-Allow-Origin", "*");
        try{
            String name = request().body().asMultipartFormData().asFormUrlEncoded().get("name")[0];
            String[] defaultDescription = { "pas de description"};
            String description = request().body().asMultipartFormData().asFormUrlEncoded().getOrDefault("description",defaultDescription)[0];
            File file = request().body().asMultipartFormData().getFile("image").getFile();
            ImageMetadata imageMetadata = Imaging.getMetadata(file);
            if(imageMetadata instanceof JpegImageMetadata) {
                JpegImageMetadata jpegImageMetadata = (JpegImageMetadata) imageMetadata;
                final TiffImageMetadata exifMetadata = jpegImageMetadata.getExif();
                if (null != exifMetadata) {
                    final TiffField timeField = jpegImageMetadata.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_DATE_TIME);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                    Date date = dateFormat.parse(timeField.getStringValue());
                    long timestamp = date.getTime();
                    final TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                    if (null != gpsInfo) {
                        double longitude = gpsInfo.getLongitudeAsDegreesEast();
                        double latitude = gpsInfo.getLatitudeAsDegreesNorth();
                        BufferedImage imageResized = Scalr.resize(ImageIO.read(file), Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,
                                706, 470, Scalr.OP_ANTIALIAS);
                        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                        ImageIO.write(imageResized, "jpg", outstream);
                        byte[] buffer = outstream.toByteArray();
                        InputStream is = new ByteArrayInputStream(buffer);
                        ObjectMetadata meta = new ObjectMetadata();
                        meta.setContentType("image/jpg");
                        meta.setContentLength(buffer.length);
                        TransferManager transferManager = new TransferManager();
                        transferManager.getAmazonS3Client().setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
                        Upload upload = transferManager.upload("littering-images", timestamp+"-"+name, is, meta);
                        // waitForCompletion blocks the current thread until the transfer completes
                        // and will throw an AmazonClientException or AmazonServiceException if
                        // anything went wrong.
                        upload.waitForCompletion();
                        is.close();
                        outstream.close();
                        String url = "https://s3.eu-central-1.amazonaws.com/littering-images/"+timestamp+"-"+name;
                        Photo photo = new Photo(name,url, longitude, latitude, timestamp, description);
                        PhotoDB.save(photo);
                        return ok();
                    }
                }
            }
            return badRequest();
        }catch (Exception e) {
            return internalServerError();
        }
    }

    public Result delete(String id){
        response().setHeader("Access-Control-Allow-Origin", "*");
        Photo photo = PhotoDB.load(id);
        if (photo == null) {
            return notFound();
        }else {
            PhotoDB.delete(photo);
            return ok();
        }
    }



}
