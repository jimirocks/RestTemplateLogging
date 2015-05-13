
/*
 * Copyright (C) 2007-2015, GoodData(R) Corporation. All rights reserved.
 */

package rocks.jimi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * TODO
 */
public class RestTemplateLogging {



    public static void main(String[] args) {
        RestTemplate rest = new RestTemplate();
        rest.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(
                new LoggingClientHttpRequestInterceptor()));
        rest.getForEntity("https://google.com", String.class).getBody();
        System.out.println("GET from google OK");
    }

    private static class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
        private final Logger logger = LoggerFactory.getLogger(RestTemplateLogging.class);

        public ClientHttpResponse intercept(HttpRequest request,
                                            byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            final ClientHttpResponse response = new ReReadableResponse(execution.execute(request, body));
            logger.info("response_body={" + StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()) + "}");
            return response;
        }
    }

    private static class ReReadableResponse implements ClientHttpResponse {
        private final ClientHttpResponse wrapped;
        private byte[] body;

        public ReReadableResponse(ClientHttpResponse wrapped) {
            this.wrapped = wrapped;
        }

        public HttpStatus getStatusCode() throws IOException {
            return wrapped.getStatusCode();
        }

        public int getRawStatusCode() throws IOException {
            return wrapped.getRawStatusCode();
        }

        public String getStatusText() throws IOException {
            return wrapped.getStatusText();
        }

        public void close() {
            wrapped.close();
        }

        public InputStream getBody() throws IOException {
            if (body == null) {
                body = StreamUtils.copyToByteArray(wrapped.getBody());
            }
            return new ByteArrayInputStream(this.body);
        }

        public HttpHeaders getHeaders() {
            return wrapped.getHeaders();
        }
    }
}
