package controllers;

import db.PhotoDB;
import play.mvc.*;
import views.html.admin;

public class AdminController extends Controller{

    public Result index() {
        return ok(admin.render(PhotoDB.loadAll()));
    }
}
