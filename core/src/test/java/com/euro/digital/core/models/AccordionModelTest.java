package com.euro.digital.core.models;

import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(AemContextExtension.class)
class AccordionModelTest {
    private AemContext aemContext = new AemContext();
    private Page page;
    private Resource resource;
    @BeforeEach
    void setUp() {
        page = aemContext.create().page("/content/accordionPage");
        aemContext.create().resource(page,"Accordion", "sling:resourceType", "eurodigital/components/accordion");
    }

    @Test
    void getSlideList() {
    }

    @Test
    void init() {
    }
}