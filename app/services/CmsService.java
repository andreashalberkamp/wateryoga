package services;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import models.CmsBean;

import org.apache.commons.lang3.StringUtils;

import play.Play;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;

import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAClient.Builder;
import com.contentful.java.cda.model.CDAArray;
import com.contentful.java.cda.model.CDAEntry;

public class CmsService {

    private static final String PARAM_LOCALE = "locale";
    private static final String PARAM_PAGETYPE = "content_type";
    private static final String PARAM_PAGECODE = "fields.pagecode";
    private static final String DEFAULT_PAGE = "baseLayout";
    private CDAClient client;
    private CDAClient previewClient;

    /**
     * Fetches a localized page asynchronously
     *
     * @param pageType
     *            Type of the requested cmsPage
     * @param pageID
     *            pageID of the requested cmspage. You have to use the pageId, because the normal ID is not localizable
     * @param locale
     *            locale of the content to deliver.
     * @return Optional of a promise of a cdaEntry
     **/

    public CmsBean fetchCmsPage(String pageType, String pageID, Locale locale, boolean preview) {
        initClients();
        CmsBean result = null;
        // localisation not working for the free plan
        // queryMap.put(PARAM_LOCALE, locale.getLanguage());
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put(PARAM_PAGECODE, pageID);
        queryMap.put(PARAM_PAGETYPE, pageType);
        CDAArray cdaArray = executeQuery(queryMap, locale, preview);
        if (cdaArray != null && cdaArray.getItems().size() > 0) {
            CDAEntry cdaEntry = (CDAEntry) cdaArray.getItems().get(0);

            result = new CmsBean(cdaEntry);
        }

        return result;
    }

    public CmsBean fetchCmsPageByID(String pageType, String pageID, Locale locale, boolean preview) {
        initClients();
        CDAClient currentClient = preview ? previewClient : client;
        CDAEntry cdaEntry = currentClient.entries().fetchOne(pageID);

        return cdaEntry != null ? new CmsBean(cdaEntry) : null;
    }

    /**
     * This is a helpermethod for junit-Testing, because the final class 'ModuleEntries' is not mockable.
     */
    CDAArray executeQuery(Map<String, String> queryMap, Locale locale, boolean preview) {
        CDAClient currentClient = preview ? previewClient : client;
        CDAArray cdaArray = null;
        if (currentClient != null) {
            try {
                cdaArray = currentClient.entries().fetchAll(queryMap);
            } catch (RetrofitError e) {
                play.Logger.info(
                        String.format("The cmspage with code %s (pagetype: %s)cannot be found", queryMap.get(PARAM_PAGECODE), queryMap.get(PARAM_PAGETYPE)), e);
                // Load defaultpage
                if (!StringUtils.equals(queryMap.get(PARAM_PAGECODE), DEFAULT_PAGE)) {
                    this.fetchCmsPage(DEFAULT_PAGE, DEFAULT_PAGE, locale, preview);
                }
            }

        }

        return cdaArray;
    }

    public void initClients() {
        if (client == null || previewClient == null) {
        String spaceKey = Play.configuration.getProperty("contentful.spaceKey", "");
        String accessToken = Play.configuration.getProperty("contentful.accessToken", "");
        String accessTokenPreview = Play.configuration.getProperty("contentful.accessTokenPreview", accessToken);
        Builder clientbuilder = new CDAClient.Builder().setSpaceKey(spaceKey).setAccessToken(accessToken);
        Builder previewClientbuilder = new CDAClient.Builder().setSpaceKey(spaceKey).setAccessToken(accessTokenPreview).preview();

        if (Boolean.valueOf(Play.configuration.getProperty("contentful.debug", "false"))) {
            clientbuilder.setLogLevel(LogLevel.FULL);
            previewClientbuilder.setLogLevel(LogLevel.FULL);
        }
        client = clientbuilder.build();
            previewClient = previewClientbuilder.build();
        }

    }

    public CDAClient getClient() {
        return client;
    }

    public void setClient(CDAClient client) {
        this.client = client;
    }

    public CDAClient getPreviewClient() {
        return previewClient;
    }

    public void setPreviewClient(CDAClient previewClient) {
        this.previewClient = previewClient;
    }
}