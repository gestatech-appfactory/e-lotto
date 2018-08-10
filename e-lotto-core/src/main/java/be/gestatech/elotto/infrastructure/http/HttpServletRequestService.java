package be.gestatech.elotto.infrastructure.http;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RequestScoped
public class HttpServletRequestService {

    @Inject
    HttpServletRequest httpServletRequest;

    public String getIpAddressFromRequest() {
        String ipAddress = httpServletRequest.getHeader("X-Forwarded-For");
        if (Objects.nonNull(ipAddress)) {
            ipAddress = ipAddress.split("\\s*,\\s*", 2)[0];
        } else {
            ipAddress = httpServletRequest.getRemoteAddr();
        }
        return ipAddress;
    }

    public String getUserAgentFromRequest() {
        String userAgent = httpServletRequest.getHeader("user-agent");
        if (Objects.isNull(userAgent)) {
            userAgent = httpServletRequest.getRemoteAddr();
        }
        return userAgent;
    }
}
