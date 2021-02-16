package com.euro.digital.core.service.impl;

import com.euro.digital.core.service.SampleCheckService;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = SampleCheckService.class, property = {"name=second"})
public class SecondSampleCheckServiceCall implements SampleCheckService{
    @Override
    public String returnString() {
        return "Second Service";
    }
}
