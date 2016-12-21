package org.openweathermap.tests.forecast_daily

import com.ihg.middleware.test.OpenWeatherTestCase
import groovy.json.JsonSlurper
import org.springframework.http.HttpMethod
import spock.lang.Unroll

import static org.junit.Assert.assertNotNull

class TC3_16Day_dailyForecastJsonNegativeTest extends OpenWeatherTestCase {
    def response = null

    @Unroll
    def "Test api open weather : get weather 5 days: #q"() {
        setup: "Configuration url"
        openWeatherRestClient.setHostUrl(openWeatherRestClient.getHostUrl() + "forecast/daily")

        when: "send GET request to open weather and parse response"
        response = new JsonSlurper().parseText(openWeatherRestClient.sendAndAssertResponseStatus(
                EXPECTED_STATUS: cod,
                REQUEST_METHOD: HttpMethod.GET,
                REQUEST_PARAMS_STRING: "q={q}&mode={mode}&units={units}&cnt={cnt}&appid={appid}",
                REQUEST_PARAMS_VARIABLES: [
                        q    : q,
                        mode : mode,
                        units: unit,
                        cnt  : 5,
                        appid: appid
                ]
        ))

        then: "check error message"

        assert response.message == message
        assert response.cod.toInteger() == cod

        where:
        q           | mode      | unit          |   appid                                   | cod       | message
        ""          | "json"    | "Metric"      | "e887cf2c4eac937d0638a90d3b52147c"        | 502        | "Error: Not found city"
        "*!@)"      | "json"    | "Kelvin"      | "e887cf2c4eac937d0638a90d3b52147c"        | 502        | "Error: Not found city"
        "Brest,By"  | "json"    | "Kelvin"      | "qwefghgf"                                | 401        | "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info."
        "Brest,By"  | "1123"    | "Metric"      | "e887cf2c4eac937d0638a90d3b52147c"        | 400        | "Error: mode isn't supported"
    }
}
