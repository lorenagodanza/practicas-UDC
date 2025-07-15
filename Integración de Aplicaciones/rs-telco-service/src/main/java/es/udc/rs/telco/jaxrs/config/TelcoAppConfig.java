package es.udc.rs.telco.jaxrs.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class TelcoAppConfig extends ResourceConfig {
    public TelcoAppConfig() {
        register(JacksonFeature.class);
    }

}