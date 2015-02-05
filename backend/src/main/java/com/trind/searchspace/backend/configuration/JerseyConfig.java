package com.trind.searchspace.backend.configuration;

import com.trind.searchspace.backend.rest.SearchResourceRESTService;
import com.trind.searchspace.backend.rest.TemplateResourceRESTService;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

/**
 * Created by Joachim on 2015-02-05.
 */
@Component
@ApplicationPath("rest")
public class JerseyConfig  extends ResourceConfig {

    public JerseyConfig() {
        register(SearchResourceRESTService.class);
        register(TemplateResourceRESTService.class);
    }

}