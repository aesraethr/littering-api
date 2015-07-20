package controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import db.Photo;
import db.PhotoDB;
import play.Logger;
import play.mvc.Result;
import java.io.File;

@Api(value = "photos", description = "the basic operations about the littering photos")
public class PhotoController extends BaseApiController {
    private static Logger.ALogger logger = Logger.of(PhotoController.class);
    private AmazonS3 s3Client = new AmazonS3Client();

    @ApiOperation(value = "List all Photos", nickname = "List", httpMethod = "GET", response = db.Photo.class)
    public Result list() {
        return JsonResponse(PhotoDB.loadAll());
    }

    public Result get(String id) {
        Photo photo = PhotoDB.load(id);
        if (photo == null) {
            return notFound();
        }else {
            return JsonResponse(photo);
        }
    }

    public Result put(){
        try{
            String name = request().body().asMultipartFormData().asFormUrlEncoded().get("name")[0];
            long longitude = Long.parseLong(request().body().asMultipartFormData().asFormUrlEncoded().get("longitude")[0]);
            long latitude = Long.parseLong(request().body().asMultipartFormData().asFormUrlEncoded().get("latitude")[0]);
            long timestamp = Long.parseLong(request().body().asMultipartFormData().asFormUrlEncoded().get("timestamp")[0]);
            String description = request().body().asMultipartFormData().asFormUrlEncoded().get("name")[0];
            File file = request().body().asMultipartFormData().getFile("image").getFile();
            s3Client.putObject(new PutObjectRequest("littering-images",timestamp+"-"+name,file));
            String url = "https://s3.eu-central-1.amazonaws.com/littering-images/"+timestamp+"-"+name;
            Photo photo = new Photo(name,url, longitude, latitude, timestamp, description);
            PhotoDB.save(photo);
            return ok();
        }catch (Exception e) {
            return internalServerError();
        }
    }

    public Result delete(String id){
        Photo photo = PhotoDB.load(id);
        if (photo == null) {
            return notFound();
        }else {
            PhotoDB.delete(photo);
            return ok();
        }
    }



}
