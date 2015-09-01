package controllers;

import db.Config;
import db.ConfigDB;
import play.mvc.Result;
import static play.mvc.Results.ok;

/**
 * Created by Aesraethr on 23/07/2015.
 */
public class ConfigController {

    public Result toggleModeration() {

        Config config = ConfigDB.load("prod");
        config.setModeration(!config.isModeration());
        ConfigDB.save(config);
        return ok();

    }
}
