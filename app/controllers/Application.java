package controllers;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import play.mvc.Controller;
import services.CmsService;
import utils.Constants;
public class Application extends Controller {

    @Inject
    static CmsService cmsService;

    public static void index() {
        Map photoMap = new HashMap<String, String>();
        photoMap.put("http://www.fuchs-physiotherapie.com/wp-content/uploads/2014/11/yoga.jpg", "wateryoga");
        renderArgs.put(Constants.REQUEST_CMSBEAN,
                cmsService.fetchCmsPage(Constants.CMS_CONTENTTYPE_STARTPAGE, Constants.CMS_CONTENTTYPE_STARTPAGE, null, false));
        renderArgs.put("photos", photoMap);
        renderTemplate("index.html");
    }

    public static void gallery() {
        Map photoMap = new HashMap<String, String>();
        photoMap.put("http://www.lanzarotesurf.com/wp-content/uploads/2014/11/yoga-1024x433.jpg", "wateryoga");
        photoMap.put("http://www.fuchs-physiotherapie.com/wp-content/uploads/2014/11/yoga.jpg", "wateryoga");

        renderArgs.put("photos", photoMap);
        renderTemplate("gallery.html");
    }
}