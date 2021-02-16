package com.euro.digital.core.service;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.apache.sling.auth.core.spi.AuthenticationInfoPostProcessor;
import org.apache.sling.jcr.base.util.AccessControlUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

@Component(service = AuthenticationInfoPostProcessor.class)
public class UserLoginPostProcessor implements AuthenticationInfoPostProcessor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
    ResourceResolver resourceResolver;

    @Override
    public void postProcess(AuthenticationInfo authenticationInfo, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws LoginException {
        logger.info("========>Auth Type "+authenticationInfo.getAuthType());
        logger.info("========>Get Country "+authenticationInfo.get("country"));
        logger.info("========>UserName "+authenticationInfo.getUser());
        logger.info("========>Password "+authenticationInfo.getPassword());
        String authType = authenticationInfo.getAuthType();
        String username = authenticationInfo.getUser();
        char[] pwd = authenticationInfo.getPassword();
        Session session = resourceResolver.adaptTo(Session.class);
        try {
            ValueFactory valueFactory = session.getValueFactory();
            UserManager userManager = AccessControlUtil.getUserManager(session);
            Authorizable authorizable = userManager.getAuthorizable(session.getUserID());
            String id = authorizable.getID();
            String path = authorizable.getPath();
            Iterator<String> propertyNames = authorizable.getPropertyNames();
            while (propertyNames.hasNext()){
                Value[] propertyVal = authorizable.getProperty(propertyNames.next());
            }

        } catch (RepositoryException e) {
            e.printStackTrace();
        } finally {
        }

    }
}
