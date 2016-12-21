package org.openweathermap.tests.current_weather_data

import com.ihg.middleware.test.OpenWeatherTestCase
import groovy.util.logging.Log4j
import org.openweathermap.utils.DateParser
import org.springframework.http.HttpMethod
import spock.lang.Unroll

import java.time.LocalDateTime
import static org.junit.Assert.assertNotNull

@Log4j
class TC2_CurrentWeatherDataXmlPositiveTest extends OpenWeatherTestCase  {

    def response = null

    @Unroll
    def "Test api open weather : get current weather by city name : #q"() {
        setup: "Configuration url"
        openWeatherRestClient.setHostUrl(openWeatherRestClient.getHostUrl() + "weather")

        when: "send GET request to open weather and parse response"
        response = new XmlSlurper().parseText(openWeatherRestClient.sendAndAssertResponseStatus(
                EXPECTED_STATUS: 200,
                REQUEST_METHOD : HttpMethod.GET,
                REQUEST_PARAMS_STRING : "q={q}&mode={mode}&appid={appid}",
                REQUEST_PARAMS_VARIABLES    : [
                        q : q,
                        mode : mode,
                        appid : appid
                ]
        ))

        and: "parse date from response to LocalDateTime"
        LocalDateTime sunrise = DateParser.parse(response.city.sun.@'rise')
        LocalDateTime sunset = DateParser.parse(response.city.sun.@'set')

        then: "check that response contain expected data"
        assert response.city.@'name' == expectedCity
        assert sunset > sunrise
        assert response.temperature.@'unit' == "kelvin"
        assert response == country
        assertNotNull(response.temperature.@'value')
        assertNotNull(response.temperature.@'min')
        assertNotNull(response.temperature.@'max')
        assertNotNull(response.humidity.@'value')
        assertNotNull(response.pressure.@'value')
        assertNotNull(response.wind.speed.@'value')
        assertNotNull(response.visibility.@'value')
        assertNotNull(response.lastupdate.@'value')

        where:
        q           | mode      | appid                                 |  expectedCity  | country | cod
        "Brest,By"  | "xml"     | "e887cf2c4eac937d0638a90d3b52147c"    |  "Brest"       | "BY"    | 200
        "Minsk,By"  | "xml"     | "e887cf2c4eac937d0638a90d3b52147c"    |  "Minsk"       | "BY"    | 200
    }
}
