package controllers;

import db.ConfigDB;
import db.PhotoDB;
import play.mvc.*;
import views.html.admin;

public class AdminController extends Controller{

    public Result admin() {
        return ok(admin.render(PhotoDB.loadAll(),ConfigDB.load("prod")));
    }
}
