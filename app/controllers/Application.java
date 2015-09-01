package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Littering Api"));
    }

    /*
    * Define any extra CORS headers needed for option requests (see http://enable-cors.org/server.html for more info)
    */
    public Result preflight(String all) {
        response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response().setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept");
        response().setHeader("Access-Control-Allow-Origin", "*");
        return ok();
    }

}
