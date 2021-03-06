package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.contentful.java.cda.model.CDAAsset;
import com.contentful.java.cda.model.CDAEntry;

/** Variant attribute names */


public class CmsBean {

    private static String CONTENTTYPE = "contentType";
    private static String PAGECODE = "pagecode";

    CDAEntry entry = null;

    public CmsBean(CDAEntry cdaEntry) {
        this.entry = cdaEntry;
    }

    public Map getSys() {
        return entry.getSys();
    }

    // contentType->{sys={type=Link, linkType=ContentType, id=startpage}}

    public String getContentType() {
        String type = "cms";
        Map<String, Object> map = entry.getSys();
        if (map != null && map.containsKey(CONTENTTYPE)) {
            type = ((com.google.gson.internal.StringMap) ((com.google.gson.internal.StringMap) map.get(CONTENTTYPE)).get("sys")).get("id").toString();
        }
        return type;
    }

    public String getID() {
        return (String) entry.getSys().get("id");
    }

    public String getPageCode() {
        return getString(PAGECODE);
    }

    public CmsBean getEntry(String key) {
        CmsBean cmsBean = null;
        if (entry.getFields().containsKey(key)) {
            Object item = entry.getFields().get(key);
            if (item instanceof CDAEntry) {
                cmsBean = new CmsBean((CDAEntry) item);
            }
        }
        return cmsBean;
    }

    public CDAAsset getAsset(String key) {
        CDAAsset cdaAsset = null;
        if (entry.getFields().containsKey(key)) {
            Object item = entry.getFields().get(key);
            if (item instanceof CDAAsset) {
                cdaAsset = (CDAAsset) item;
            }
        }
        return cdaAsset;
    }

    public String getString(String key) {
        String result = "";
        if (entry.getFields().containsKey(key)) {
            Object item = entry.getFields().get(key);
            if (item instanceof String) {
                result = item.toString();
            }
        }
        return result;
    }

    public boolean containsKey(String key) {
        return entry.getFields().containsKey(key);// && !(entry.getFields().get(key) instanceof LinkedTreeMap);
    }

    public List<CmsBean> getEntryList(String key) {
        List<CmsBean> list = new ArrayList<CmsBean>();
        if (entry.getFields().containsKey(key)) {
            Object items = entry.getFields().get(key);
            if (items instanceof ArrayList) {
                for (Object item : (ArrayList) items) {
                    if (item instanceof CDAEntry) {
                        list.add(new CmsBean((CDAEntry) item));
                    }
                }

            }
        }
        return list;
    }
}
