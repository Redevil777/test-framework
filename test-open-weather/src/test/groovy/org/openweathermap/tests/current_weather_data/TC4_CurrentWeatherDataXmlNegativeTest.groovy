package org.openweathermap.tests.current_weather_data

import com.ihg.middleware.test.OpenWeatherTestCase
import groovy.json.JsonSlurper
import org.springframework.http.HttpMethod
import spock.lang.Unroll

class TC4_CurrentWeatherDataXmlNegativeTest extends OpenWeatherTestCase {
    def response = null

    @Unroll
    def "Test api open weather : get current weather by city name : #q"() {
        setup: "Configuration url"
        openWeatherRestClient.setHostUrl(openWeatherRestClient.getHostUrl() + "weather")

        when: "send GET request to open weather with incorrect data and parse response"
        response = new JsonSlurper().parseText(openWeatherRestClient.sendAndAssertResponseStatus(
                EXPECTED_STATUS: cod,
                REQUEST_METHOD: HttpMethod.GET,
                REQUEST_PARAMS_STRING: "q={q}&mode={mode}&appid={appid}",
                REQUEST_PARAMS_VARIABLES: [
                        q    : q,
                        mode : mode,
                        appid: appid
                ]
        ))

        then: "check error message"
        assert response.message == message
        assert response.cod.toInteger() == cod

        where:
        q                       | mode      | appid                                             |  cod    | message
        "Brest,By"              | "1123"    | "e887cf2c4eac937d0638a90d3b52147c"                | 400     | "Error: mode isn't supported"
        ""                      | "xml"     | "e887cf2c4eac937d0638a90d3b52147c"                | 502     | "Error: Not found city"
        "*!@)"                  | "xml"     | "e887cf2c4eac937d0638a90d3b52147c"                | 502     | "Error: Not found city"
        "Brest,By"              | "xml"     | "qwefghgf"                                        | 401     | "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info."
    }
}
