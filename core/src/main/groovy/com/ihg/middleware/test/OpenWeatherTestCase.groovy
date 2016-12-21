package com.ihg.middleware.test

import com.ihg.middleware.client.HttpClient
import com.ihg.middleware.context.OpenWeatherContext
import groovy.util.logging.Log4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@Log4j
@ContextConfiguration( classes = OpenWeatherContext.class )
abstract class OpenWeatherTestCase extends Specification {

    @Autowired
    HttpClient openWeatherRestClient

}
