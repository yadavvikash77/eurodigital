package com.euro.digital.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "EuroDigital - Covid Counter Service Config", description = "This configuration is used to capture api end point url")
public @interface CovidCounterServiceConfig {
    @AttributeDefinition(name = "Covid API end point", type = AttributeType.STRING, description = "Enter covid api url")
    String covidApiEndPoint() default "https://api.covid19api.com/country/germany/status/confirmed";

}
