package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.StringWriter;
import com.wordnik.swagger.util.Json;

public class BaseApiController extends Controller {

    protected static ObjectMapper mapper = Json.mapper();

    public static Result JsonResponse(Object obj) {
        return JsonResponse(obj, 200);
    }

    public static Result JsonResponse(Object obj, int code) {
        StringWriter w = new StringWriter();
        try {
            mapper.writeValue(w, obj);
        } catch (Exception e) {
            Logger.info("Erreur :" + e);
        }

        response().setContentType("application/json");
        response().setHeader("Access-Control-Allow-Origin", "*");
        response().setHeader("Access-Control-Allow-Methods",
                "GET, POST, DELETE, PUT");
        response().setHeader("Access-Control-Allow-Headers",
                "Content-Type, api_key, Authorization");

        return ok(w.toString());
    }
}
