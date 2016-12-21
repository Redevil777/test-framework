package com.ihg.middleware.context

import org.springframework.context.annotation.Bean
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate

import java.util.concurrent.TimeUnit

/**
 * Base class for all contexts.
 *
 * @author ilya.lapitan@ihg.com
 */
abstract class AbstractContext {

    /**
     * Environment key for properties loading.
     */
    static final String ENV = "ENV_MTEST"

    /**
     * RestTemplate class for sending request to services.
     */
    @Bean
    RestTemplate restTemplate() {
        //setup custom error handler for RestTemplate class
        def restTemplate = new RestTemplate()
        (restTemplate.getRequestFactory() as SimpleClientHttpRequestFactory).readTimeout = TimeUnit.MINUTES.toMillis(3)
        def errorHandler = new CustomResponseErrorHandler()
        restTemplate.errorHandler = errorHandler
        restTemplate
    }

    /**
     * Custom implementation of the {@link org.springframework.web.client.ResponseErrorHandler} interface.
     * We just skip exceptions here, because we need a raw response for the future processing.
     *
     * @author ilya.lapitan@ihg.com
     */
    private class CustomResponseErrorHandler extends DefaultResponseErrorHandler {
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            //if exception occurred, skip it
            //exception will be processed by Spock
        }
    }
}
