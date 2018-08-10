package be.gestatech.elotto.infrastructure.rest;

import be.gestatech.elotto.business.configuration.control.Configurable;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public abstract class AbstractRestResource {

    public static final String MEDIA_TYPE_JSON_STRING = "application/json;charset=UTF-8"; // have to be public
    public static final String MEDIA_TYPE_XML_STRING = "application/xml;charset=UTF-8"; // have to be public

    private static final MediaType MEDIA_TYPE_JSON = new MediaType("application", "json", "UTF-8");
    private static final MediaType MEDIA_TYPE_XML = new MediaType("application", "xml", "UTF-8");

    private static List<Variant> acceptableMediaTypes = Variant.mediaTypes(MEDIA_TYPE_JSON, MEDIA_TYPE_XML).build();

    @Context
    protected UriInfo info;

    @Context
    protected HttpHeaders httpHeaders;

    @Context
    protected HttpServletRequest httpServletRequest;

    @Context
    protected HttpServletResponse httpServletResponse;

    @Context
    protected Request request;

    @Inject
    @Configurable(value = "server_base_uri", defaultValue = "http://localhost:8080")
    protected Instance<String> configServerBaseUri;


    protected String getHeaderValue(final String headerKey) {
        return httpHeaders.getHeaderString(headerKey);
    }

    public Locale getRequestLocale() {
        return httpServletRequest.getLocale();
    }

    protected MediaType getNegotiatedMediaType() {
        final Variant selectedMediaType = request.selectVariant(acceptableMediaTypes);
        if (Objects.isNull(selectedMediaType)) {
            return MEDIA_TYPE_JSON;
        }
        return selectedMediaType.getMediaType();
    }
}
