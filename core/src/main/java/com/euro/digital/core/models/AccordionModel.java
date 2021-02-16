package com.euro.digital.core.models;

import com.day.cq.search.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "eurodigital/components/accordion")
@Exporter(name="jackson", extensions = "json", selector = "model")
public class AccordionModel {
    private static final Logger logger = LoggerFactory.getLogger(AccordionModel.class);
    @ChildResource
    private Resource slides;
    @SlingObject @Required
    private ResourceResolver resourceResolver;
    

    List<Map> slideList = new ArrayList<Map>();

    public List<Map> getSlideList() {
        return slideList;
    }
    @PostConstruct
    protected void init() {
        logger.info("=========> CALLING" + slides);
        if (slides != null) {
            Iterator<Resource> childIterator = slides.listChildren();
            while (childIterator.hasNext()) {
                Resource childResource = childIterator.next();
                String fragmentPath = childResource.getValueMap().get("fragmentPath", String.class);
                if (StringUtils.isNotBlank(fragmentPath)) {
                    String fragmentVal = readContentFragmentValues(resourceResolver, fragmentPath);
                    if (StringUtils.isNotBlank(fragmentVal)) {
                        Map<String, String> resourceProperty = new HashMap<String, String>();
                        resourceProperty.put("slideLabel", childResource.getValueMap().get("slideLabel", String.class));
                        resourceProperty.put("btnLabel", childResource.getValueMap().get("btnLabel", String.class));
                        resourceProperty.put("slideImage", childResource.getValueMap().get("slideImage", String.class));
                        resourceProperty.put("fragmentContent", fragmentVal);
                        slideList.add(resourceProperty);

                    }
                }
            }
        }
    }
    private String readContentFragmentValues(ResourceResolver resourceResolver, String fragmentPath){
        String fragmentContent = StringUtils.EMPTY;
        Resource childResource = resourceResolver.getResource(fragmentPath+"/jcr:content/renditions/original/jcr:content");
        logger.info("CHILD RESOURCE ======> {}",childResource.getPath());
        try {
            Node fragmentNode = childResource.adaptTo(Node.class);
            if (fragmentNode == null || !fragmentNode.hasProperty("jcr:data") || !fragmentNode.hasProperty("jcr:mimeType") || !fragmentNode.getProperty("jcr:mimeType").getString().equals("text/html")) {
                logger.info("Either Fragment Node is not present or jcr:data, jcr:mimeType property is missing");
            } else {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fragmentNode.getProperty("jcr:data").getBinary().getStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = StringUtils.EMPTY;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    fragmentContent = stringBuilder.toString();
            }
        }catch (Exception e){
            logger.error("Excetion while resource fetching ",e);
        }
        return fragmentContent;
    }
}
