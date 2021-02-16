package com.euro.digital.core.service.impl;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.euro.digital.core.configuration.CovidCounterServiceConfig;
import com.euro.digital.core.service.CovidCounterService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component(service = CovidCounterService.class, immediate = true)
@Designate(ocd = CovidCounterServiceConfig.class)
public class CovidCounterServiceImpl implements CovidCounterService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String endPointUrl = StringUtils.EMPTY;
    public final String SUBSERVICE = "euroservice";
    public final String REPOPATH = "/content/eurodigital/jcr:content";
    public final String COUNTERPROPERTY = "covidCount";

    @Reference
    ResourceResolverFactory resourceResolverFactory;
    @Reference
    Replicator replicator;

    @Activate
    protected void activate(final CovidCounterServiceConfig config){

        endPointUrl = config.covidApiEndPoint();
    }

    @Override
    public String callCovidApi() {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        String responseString = StringUtils.EMPTY;
        try{
            if(StringUtils.isNotBlank(endPointUrl)) {
                URIBuilder uriBuilder = new URIBuilder(endPointUrl);
                URI uri = uriBuilder.build();
                HttpGet httpGet = new HttpGet(uri);
                HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                responseString = EntityUtils.toString(httpEntity);
                writeInNodeProperties(responseString);
                /*logger.info("Covid API Response =====> " + responseString);*/
            }
        }catch(Exception e){
            logger.error("Exception calling covid api",e);
        }finally {
            try {
                if(closeableHttpClient != null) {closeableHttpClient.close();}
            }catch (IOException ioException){
                logger.error("Exception while closing connection",ioException);
            }
        }
        return responseString;
    }

    private void writeInNodeProperties(String responseString){

        ResourceResolver resourceResolver = null;
        Session session;
        logger.info("WRITING IN REPOSITORY =======>");
        try{
            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
            if(resourceResolver != null){
                session = resourceResolver.adaptTo(Session.class);
                Resource resource = resourceResolver.getResource(REPOPATH);
                if(resource != null){
                    Node jcrContent = resource.adaptTo(Node.class);
                    jcrContent.setProperty(COUNTERPROPERTY, responseString);
                    replicator.replicate(session, ReplicationActionType.ACTIVATE, REPOPATH);
                }
                session.save();
            }
        }catch (Exception e){
            logger.error("Exception while writing to repository",e);
        }finally {
            if(resourceResolver != null){
                resourceResolver.close();
            }

        }
    }

}
