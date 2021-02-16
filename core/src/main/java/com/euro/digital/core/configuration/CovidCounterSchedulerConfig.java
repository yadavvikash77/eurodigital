package com.euro.digital.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "EuroDigital - Scheduler configuration", description = "This configuration is used by covid counter scheduler")
public @interface CovidCounterSchedulerConfig {
    @AttributeDefinition(name = "Enable Scheduler", type = AttributeType.BOOLEAN, description = "Enable or disable scheduler")
    boolean enableScheduler() default false;

    @AttributeDefinition(name = "Crone-Job Expression", required = true, type = AttributeType.STRING)
    String scheduler_expression() default "0 20 13 * * ?";

    @AttributeDefinition(name = "Concurrent Task", type = AttributeType.BOOLEAN, description = "Concurrent task is allowed or not")
    boolean scheduler_concurrent() default false;
}
