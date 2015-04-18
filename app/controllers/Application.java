package controllers;

import java.util.Map;

import javax.inject.Inject;

import models.CmsBean;
import play.mvc.Controller;
import services.CmsService;
import utils.Constants;

public class Application extends Controller {

    @Inject
    static CmsService cmsService;

    public static void index() {

        CmsBean cmsBean = cmsService.fetchCmsPage(Constants.CMS_CONTENTTYPE_STARTPAGE, Constants.CMS_CONTENTTYPE_STARTPAGE, null, false);
        Map<String, Object> map = cmsBean.getSys();
        renderArgs.put(Constants.REQUEST_CMSBEAN, cmsBean);
        renderTemplate("index.html");
    }

    public static void cmsPage(String contentType, String pageCode) {
        CmsBean cmsBean = cmsService.fetchCmsPage(contentType, pageCode, null, false);
        renderArgs.put(Constants.REQUEST_CMSBEAN, cmsBean);
        renderTemplate(String.format("/pages/%s.html", cmsBean.getContentType()));
    }



}