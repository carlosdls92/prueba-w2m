package es.com.w2m.dls.utils;

import lombok.Generated;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public final class WebRequestUtils {
    public static String getEndpoint(WebRequest request) {
        String endPointException = null;
        if (request instanceof ServletWebRequest srequest) {
            String var10000 = String.valueOf(srequest.getHttpMethod());
            endPointException = "[" + var10000 + "] " + srequest.getRequest().getRequestURI();
        }

        return endPointException;
    }

    @Generated
    private WebRequestUtils() {
    }
}

