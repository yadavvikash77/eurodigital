package com.euro.digital.core.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.JsonArray;
import java.util.*;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CoronaModel {

    private static final Logger logger = LoggerFactory.getLogger(AccordionModel.class);
    @Inject
    String covidContentPath;
    @SlingObject @Required
    ResourceResolver resourceResolver;

    public List<Map> getContentList() {
        return contentList;
    }

    List<Map> contentList = new ArrayList<Map>();
    Map<String,String> map;
    @PostConstruct
    protected void init(){logger.info("=====> "+covidContentPath);
        if(StringUtils.isNotBlank(covidContentPath)) {
            Resource resource = resourceResolver.getResource(covidContentPath);
            String content = resource.getValueMap().get("covidCount", String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JSONArray jsonArray =  new JSONArray(content);
                for(int i=0; i<jsonArray.length(); ++i){
                    map = new HashMap<String,String>();
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    map.put("date",jsonObject.getString("Date"));
                    map.put("count", String.valueOf(jsonObject.getInt("Cases")));
                    map.put("status", jsonObject.getString("Status"));
                    contentList.add(map);
                }

            } catch (Exception e) {
                logger.error("Exception ", e);
            }
        }

    }
}
