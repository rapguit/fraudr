package com.globo.globosat.config;

import com.globo.globosat.FraudrApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by raphael on 22/12/16.
 */
public class WebInitializer  extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FraudrApplication.class);
    }
}
