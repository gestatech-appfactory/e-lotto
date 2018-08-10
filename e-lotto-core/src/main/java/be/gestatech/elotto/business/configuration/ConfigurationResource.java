package be.gestatech.elotto.business.configuration;

import be.gestatech.elotto.business.configuration.control.ConfigurationController;
import be.gestatech.elotto.infrastructure.rest.AbstractRestResource;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("platform/configuration")
@Produces({AbstractRestResource.MEDIA_TYPE_JSON_STRING, AbstractRestResource.MEDIA_TYPE_XML_STRING})
@Consumes({AbstractRestResource.MEDIA_TYPE_JSON_STRING, AbstractRestResource.MEDIA_TYPE_XML_STRING})
@RequestScoped
public class ConfigurationResource extends AbstractRestResource {

    @Inject
    ConfigurationController configurationController;

    public ConfigurationResource() {
        // Intentionally left blank
    }

    /**
     * Triggers the configuration to update
     */
    @GET
    @Path("refresh")
    @RolesAllowed({RoleId.ADMIN_STRING})
    public Response refreshDatabaseConfiguration() {
        configurationController.refreshConfiguration();
        return Response.ok().build();
    }

}
