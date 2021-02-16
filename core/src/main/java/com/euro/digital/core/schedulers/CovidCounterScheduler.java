package com.euro.digital.core.schedulers;

import com.euro.digital.core.configuration.CovidCounterSchedulerConfig;
import com.euro.digital.core.service.CovidCounterService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Designate(ocd = CovidCounterSchedulerConfig.class)
@Component(service = Runnable.class, immediate = true)
public class CovidCounterScheduler implements Runnable {

    private boolean enableScheduler = false;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    CovidCounterService covidCounterService;

    @Override
    public void run() {
        logger.info("SCHEDULER RUNNING=======>" + enableScheduler);
        if (enableScheduler) {
            String apiCallResponse = covidCounterService.callCovidApi();
        }
    }

    @Activate
    protected void activate(final CovidCounterSchedulerConfig config) {
        enableScheduler = config.enableScheduler();
    }
}
